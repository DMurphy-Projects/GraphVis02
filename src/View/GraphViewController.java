package View;

import Model.Chunk;
import Model.Graph.Graph;
import Model.Graph.Node;
import Model.Graph.Relationship;

import java.awt.*;
import java.util.*;

//is this just a controller at this point? not only used for view
public class GraphViewController {

    double xPos = 0, yPos = 0, zoom = 1, screenSize, defaultScreenSize = 100, globalOffX, globalOffY;

    Graph graph;

    int loadMapWidth, loadMapHeight;
    HashMap<String, Boolean> loadMap;

    ArrayList<Chunk> loadedChunks = new ArrayList<>();
    ArrayList<Relationship> loadedRelationships = new ArrayList<>();

    public GraphViewController(Graph g)
    {
        graph = g;

        loadMapWidth = g.width;
        loadMapHeight = g.height;
        loadMap = new HashMap<>();
    }

    private void updateScreenSize()
    {
        screenSize = defaultScreenSize * zoom;

        Dimension graphDim = graph.getGraphDimension();
        globalOffX = (screenSize / 2) - (graphDim.getWidth() / 2);
        globalOffY = (screenSize / 2) - (graphDim.getHeight() / 2);
    }

    private boolean isInsideScreen(double x, double y, double width, double height)
    {
        boolean b1 = x + width > 0;
        boolean b2 = y + height > 0;
        boolean b3 = x < screenSize;
        boolean b4 = y < screenSize;

        return b1 && b2 && b3 && b4;
    }

    private void loadSimple()
    {
        for (Chunk c: graph.getAllChunks())
        {
            int x = (int) c.getRelativeX();
            int y = (int) c.getRelativeY();
            int size = c.getSize();

            loadMap.put(c.getID(), isInsideScreen(x + xPos + globalOffX, y + yPos + globalOffY, size, size));
        }
    }

    private void loadFromRelationships()
    {
        HashMap<String, Boolean> tempMap = new HashMap<>();

        for (Relationship r: graph.getAllRelationships()) {
            Node n1 = r.getN1();
            Node n2 = r.getN2();

            Chunk c1 = n1.getBelongsTo();
            Chunk c2 = n2.getBelongsTo();

            boolean n1Vis = loadMap.get(c1.getID());
            boolean n2Vis = loadMap.get(c2.getID());

            if (n1Vis && !n2Vis)
            {
                //need to load n2
                tempMap.put(c2.getID(), true);
            }
            else if (!n1Vis && n2Vis)
            {
                //need to load n1
                tempMap.put(c1.getID(), true);
            }
        }

        for (Map.Entry<String, Boolean> chunkToLoad: tempMap.entrySet())
        {
            loadMap.put(chunkToLoad.getKey(), chunkToLoad.getValue());
        }
    }

    private void updateLoadMap()
    {
        loadSimple();
        loadFromRelationships();
    }

    private void updateChunkList()
    {
        loadedChunks.clear();
        for (Chunk c: graph.getAllChunks()) {
            boolean loaded = loadMap.get(c.getID());
            c.setVisible(loaded);
            if (loaded)
            {
                loadedChunks.add(c);
            }
        }
    }

    public void updateRelationshipList()
    {
        loadedRelationships.clear();
        for (Relationship r: graph.getAllRelationships())
        {
            Node n1 = r.getN1();
            Node n2 = r.getN2();

            if (n1.getBelongsTo().isVisible() || n2.getBelongsTo().isVisible())
            {
                loadedRelationships.add(r);
            }
        }
    }

    public void update()
    {
        updateScreenSize();

        updateLoadMap();

        updateChunkList();

        updateRelationshipList();

//        System.out.println("Loaded Chunks: "+loadedChunks.size());
//        System.out.println("Loaded Relations: "+loadedRelationships.size()+"\n");
    }

    public double[] transform(double x, double y)
    {
        double tempX = x + xPos + globalOffX;
        double tempY = y + yPos + globalOffY;

        double relX = tempX / screenSize;
        double relY = tempY / screenSize;

        return new double[]{relX, relY};
    }

    private void addChunk(ArrayList<Chunk> chunks, int x, int y)
    {
        String toFind = x + ":" + y;
        if (isLoaded(toFind))
        {
            chunks.add(graph.getChunk(toFind));
        }
    }

    private void addNearby(ArrayList<Chunk> chunks, double rX, double rY, int chunkX, int chunkY)
    {
        int x = 0, y = 0;
        if (rX > 0.5)
        {
            addChunk(chunks, chunkX+1, chunkY);
            x = 1;
        }
        else
        {
            addChunk(chunks, chunkX-1, chunkY);
            x = -1;
        }
        if (rY > 0.5)
        {
            addChunk(chunks, chunkX, chunkY+1);
            y = 1;
        }
        else
        {
            addChunk(chunks, chunkX, chunkY-1);
            y = -1;
        }

        //add the corner
        addChunk(chunks, chunkX+x, chunkY+y);

    }

    //sx = ((rx + xPos + globalX)/screenSize) * screenWidth
    //sx / screenWidth = (rx + xPos + globalX)/screenSize
    //screenSize * (sx / screenWidth) = rx + xPos + globalX
    //screenSize * (sx / screenWidth) - xPos - globalX = rx
    public double[] reversePoint(double x, double y, double screenWidth, double screenHeight)
    {
        double _x = screenSize * (x / screenWidth) - xPos - globalOffX;
        double _y = screenSize * (y / screenHeight) - yPos - globalOffY;

        return new double[]{_x, _y};
    }

    public Node getNodeAtPosition(double _x, double _y)
    {
        int chunkSize = graph.getChunkSize();

        double rX = _x % chunkSize;
        rX /= chunkSize;
        if (rX < 0) rX += 1;

        double rY = _y % chunkSize;
        rY /= chunkSize;
        if (rY < 0) rY += 1;

        int chunkX = (int) Math.floor (_x / chunkSize);
        int chunkY = (int) Math.floor(_y / chunkSize);

        String chunkToFind = chunkX + ":" + chunkY;
        Chunk centre = graph.getChunk(chunkToFind);

        //may not exist
        if (centre != null)
        {
            double dist = Integer.MAX_VALUE;
            Node nearest = null;

            ArrayList<Chunk> toCheck = new ArrayList<>();
            addNearby(toCheck, rX, rY, chunkX, chunkY);

            toCheck.add(centre);

            //find nearest node
            for (Chunk chunk: toCheck) {
                for (Node n : chunk.getNodes()) {
                    double[] v = new double[]{
                            n.getX() - _x,
                            n.getY() - _y
                    };
                    double d = (v[0] * v[0]) + (v[1] * v[1]);

                    if (d < dist) {
                        dist = d;
                        nearest = n;
                    }
                }

                if (nearest != null) {
                    return nearest;
                }
            }
        }
        return null;
    }

    public void move(double x, double y)
    {
        xPos += x * screenSize;
        yPos += y * screenSize;
    }

    public void zoom(double z)
    {
        zoom *= z;
    }

    public ArrayList<Chunk> getLoadedChunks() {
        return new ArrayList<>( loadedChunks);
    }

    public ArrayList<Relationship> getLoadedRelationships() {
        return new ArrayList<>(loadedRelationships);
    }

    public boolean isLoaded(String id)
    {
        boolean result = loadMap.containsKey(id);
        if (result)
        {
            result = loadMap.get(id);
        }
        return result;
    }
}
