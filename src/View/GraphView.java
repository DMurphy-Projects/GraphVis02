package View;

import Model.Chunk;
import Model.Graph.Graph;
import Model.Graph.Node;
import Model.Graph.Relationship;

import javax.swing.*;
import java.awt.*;

public class GraphView extends JPanel {

    Graph model;

    int size, xOff=0, yOff=0;

    public GraphView(Graph model)
    {
        this.model = model;
        size = model.getAllChunks().length;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.black);

        int w = getWidth(), h = getHeight();

        int chunkWidth = w / size;
        int chunkHeight = h / size;

        for (Chunk[] strip: model.getAllChunks())
        {
            for (Chunk chunk: strip)
            {
                int chunkX = (chunk.getX()*chunkWidth) + xOff;
                int chunkY = (chunk.getY()*chunkHeight) + yOff;

                boolean notVisible = chunkX+chunkWidth <= 0 || chunkX >= w || chunkY+chunkHeight <= 0 || chunkY >= h;
                chunk.setVisible(!notVisible);//ugh
                if (notVisible)
                {
//                    System.out.println(chunk.getX()+" "+chunk.getY());
                    continue;
                }

                g.drawRect(chunkX, chunkY, chunkWidth, chunkHeight);

                for (Node n:chunk.getNodes())
                {
                    float relX = n.getRelativeX() / chunk.getSize();
                    float relY = n.getRelativeY() / chunk.getSize();

                    g.drawOval((int)(relX * chunkWidth) + chunkX - 3, (int) (relY * chunkHeight) + chunkY - 3, 6, 6);
                }
            }
        }

        //turns out relationships are harder to render this way
        for (Relationship r: model.getAllRelationships())
        {
            if (r.getN1().getBelongsTo().isVisible() || r.getN2().getBelongsTo().isVisible())
            {
                Chunk c1 = r.getN1().getBelongsTo();
                Chunk c2 = r.getN2().getBelongsTo();

                int c1X = (c1.getX()*chunkWidth) + xOff;
                int c1Y = (c1.getY()*chunkHeight) + yOff;

                int c2X = (c2.getX()*chunkWidth) + xOff;
                int c2Y = (c2.getY()*chunkHeight) + yOff;

                float rel1X = r.getN1().getRelativeX() / c1.getSize();
                float rel1Y = r.getN1().getRelativeY() / c1.getSize();

                float rel2X = r.getN2().getRelativeX() / c2.getSize();
                float rel2Y = r.getN2().getRelativeY() / c2.getSize();

                g.drawLine(
                        (int)(rel1X * chunkWidth) + c1X,
                        (int)(rel1Y * chunkWidth) + c1Y,
                        (int)(rel2X * chunkWidth) + c2X,
                        (int)(rel2Y * chunkWidth) + c2Y
                );

            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }
}
