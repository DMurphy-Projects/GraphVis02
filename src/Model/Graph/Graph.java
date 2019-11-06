package Model.Graph;

import Model.Chunk;
import Model.ChunkTranslate;

import java.awt.*;
import java.util.ArrayList;

public class Graph {

    ArrayList<Relationship> relationships = new ArrayList<>();

    Chunk[][] chunks;
    int chunkSize;

    ChunkTranslate translate;

    public int width;
    public int height;

    public Graph(int width, int height, int chunkSize)
    {
        chunks = new Chunk[width][height];
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

                newX = Math.max(newX, 0);
                newY = Math.max(newY, 0);

                newX = Math.min(newX, width-1);
                newY = Math.min(newY, height-1);

                return new Point((int)newX, (int) newY);
            }
        };
        translate.setup(chunkSize*(width/2), chunkSize*(height/2), chunkSize, chunkSize);

        //init chunks
        for (int i=0;i<width;i++)
        {
            for (int ii=0;ii<height;ii++)
            {
                chunks[i][ii] = new Chunk(i, ii, chunkSize);
            }
        }
    }

    public void putNode(Node n)
    {
        Point chunkLoc = translate.translate(n.getX(), n.getY());

        Chunk chunk = chunks[chunkLoc.x][chunkLoc.y];

        System.out.println(chunkLoc);

        chunk.addNode(n);
    }

    public void putRelationship(Relationship r)
    {
        relationships.add(r);
    }

    public void updateNode(Node n)
    {
        Chunk belongs = n.getBelongsTo();

        Point chunkLoc = translate.translate(n.getX(), n.getY());

        if (!belongs.isThisChunk(chunkLoc))
        {
            belongs.removeNode(n);

            putNode(n);
        }
    }

    public Chunk[][] getAllChunks() {
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
