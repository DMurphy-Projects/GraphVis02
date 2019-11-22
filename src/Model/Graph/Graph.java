package Model.Graph;

import Model.Chunk;
import Model.ChunkTranslate;

import java.awt.*;
import java.util.ArrayList;

public class Graph {

    ArrayList<Relationship> relationships = new ArrayList<>();

    ArrayList<Chunk> chunks;
    int chunkSize;

    ChunkTranslate translate;
    public int width;
    public int height;

    public Graph(int width, int height, int chunkSize)
    {
        chunks = new ArrayList<>();
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
        //init chunks
        for (int i=0;i<width;i++)
        {
            for (int ii=0;ii<height;ii++)
            {
                Point pos = translate.translate(i*chunkSize, ii*chunkSize);
                chunks.add(new Chunk(i, ii, chunkSize, pos.x+":"+pos.y));
            }
        }
    }

    public void putNode(Node n)
    {
        Point pos = translate.translate(n.getX(), n.getY());
        String posId = pos.x+":"+pos.y;

        boolean found = false;
        for (Chunk c: chunks)
        {
            if (c.isId(posId))
            {
                found = true;

                c.addNode(n);
            }
        }

        if (!found)
        {
            System.out.println("Adding Chunk");

            Chunk newChunk = new Chunk(pos.x, pos.y, chunkSize, posId);
            chunks.add(newChunk);

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
            System.out.println("Remove " + belongs.getID());

            belongs.removeNode(n);

            putNode(n);
        }
    }

    public ArrayList<Chunk> getAllChunks() {
        return chunks;
    }

    public ArrayList<Relationship> getAllRelationships() {
        return relationships;
    }

    public Dimension getGraphDimension()
    {
        return new Dimension(width * chunkSize, height * chunkSize);
    }
}
