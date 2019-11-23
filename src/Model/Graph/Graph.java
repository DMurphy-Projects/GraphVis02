package Model.Graph;

import Model.Chunk;
import Model.ChunkTranslate;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Graph {

    ArrayList<Relationship> relationships = new ArrayList<>();

    HashMap<String, Chunk> chunks;
    int chunkSize;

    ChunkTranslate translate;
    public int width;
    public int height;

    public Graph(int width, int height, int chunkSize)
    {
        chunks = new HashMap<>();
        this.chunkSize = chunkSize;
        this.width = width;
        this.height = height;

        translate = new ChunkTranslate() {
            int x_t, y_t, x_s, y_s;
            @Override
            public void setup(int x_translate, int y_translate, int x_scale, int y_scale) {
                x_t = x_translate;
                y_t = y_translate;

                x_s = x_scale;
                y_s = y_scale;
            }

            @Override
            public Point translate(double x, double y) {
                double newX = (x + x_t) / x_s;
                double newY = (y + y_t) / y_s;

                newX = Math.floor(newX);
                newY = Math.floor(newY);

                return new Point((int)newX, (int) newY);
            }
        };
        translate.setup(0, 0, chunkSize, chunkSize);

        int w = width/2;
        int h = height/2;
        //init chunks
        for (int i=-w;i<width-w;i++)
        {
            for (int ii=-h;ii<height-h;ii++)
            {
                Point pos = translate.translate(i*chunkSize, ii*chunkSize);
                String id = pos.x+":"+pos.y;
                chunks.put(id, new Chunk(i, ii, chunkSize, id));
            }
        }
    }

    public void putNode(Node n)
    {
        Point pos = translate.translate(n.getX(), n.getY());
        String posId = pos.x+":"+pos.y;

        if (chunks.containsKey(posId))
        {
            chunks.get(posId).addNode(n);
        }
        else
        {
            Chunk newChunk = new Chunk(pos.x, pos.y, chunkSize, posId);
            chunks.put(posId, newChunk);

            newChunk.addNode(n);
        }
    }

    public void putRelationship(Relationship r)
    {
        relationships.add(r);
    }

    public void updateNode(Node n)
    {
        Chunk belongs = n.getBelongsTo();

        if (!belongs.isInsideChunk(n))
        {
            belongs.removeNode(n);

            putNode(n);
        }
    }

    public Collection<Chunk> getAllChunks() {
        return chunks.values();
    }

    public ArrayList<Relationship> getAllRelationships() {
        return relationships;
    }

    public Dimension getGraphDimension()
    {
        return new Dimension(width * chunkSize, height * chunkSize);
    }

    public Chunk getChunk(String id)
    {
        return chunks.get(id);
    }
}
