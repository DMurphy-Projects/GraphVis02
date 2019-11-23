package Controller.Routines;

import Model.Chunk;
import Model.Force.IForce;
import Model.Graph.ForceNode;
import Model.Graph.Graph;
import Model.Graph.Node;
import View.GraphViewController;

import java.util.ArrayList;
import java.util.List;

public class ChunkRadiusRoutine implements IForceRoutine {

    GraphViewController viewController;
    Graph graph;

    IForce force;

    //pseudo radius
    int chunkRadius;

    public ChunkRadiusRoutine(GraphViewController vCon, Graph model, IForce f, int cRad)
    {
        viewController = vCon;
        graph = model;

        force = f;

        chunkRadius = cRad;
    }

    private List<Chunk> getRadiusList(int x, int y)
    {
        ArrayList<Chunk> chunks = new ArrayList<>();

        int sideLength = (chunkRadius) * 2 + 1;

        for (int _w = 0;_w<sideLength;_w++)
        {
            int w = x - chunkRadius + _w;

            for (int _h = 0;_h<sideLength;_h++)
            {
                int h = y - chunkRadius + _h;

                //find chunk at (w, h)
                String id = w + ":" + h;
                if (viewController.isLoaded(id))
                {
                    chunks.add(graph.getChunk(id));
                }
            }
        }

        return chunks;
    }

    private List<Node> createNodeList(List<Chunk> chunks)
    {
        ArrayList<Node> nodes = new ArrayList<>();

        for (Chunk c: chunks)
        {
            nodes.addAll(c.getNodes());
        }

        return nodes;
    }

    @Override
    public void doRoutine() {
        for (Chunk chunk: viewController.getLoadedChunks())
        {
            //create list of chunks in radius
            List<Chunk> radChunks = getRadiusList((int)chunk.getX(), (int) chunk.getY());
            List<Node> radNodes = createNodeList(radChunks);

            for (Node n1: chunk.getNodes())
            {
                for (Node n2: radNodes)
                {
                    if (n1.ID() == n2.ID()) continue;

                    force.applyForceForFirst((ForceNode) n1, (ForceNode) n2);
                }
            }
        }
    }
}
