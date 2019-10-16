package View;

import Model.Chunk;
import Model.Graph.Graph;
import Model.Graph.Node;
import Model.Graph.Relationship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphView extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener{

    Graph model;

    int size, xOff=0, yOff=0, prevX=0, prevY=0;

    public GraphView(Graph model)
    {
        this.model = model;
        size = model.getAllChunks().length;

        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addMouseWheelListener(this);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.black);

        int w = getWidth(), h = getHeight();

        int chunkWidth = w / size;
        int chunkHeight = h / size;

        boolean[][] loadMap = new boolean[model.width][model.height];
        //find all chunks that are on screen
        for (Chunk[] strip: model.getAllChunks())
        {
            for (Chunk chunk: strip)
            {
                int chunkX = (chunk.getX()*chunkWidth) + xOff;
                int chunkY = (chunk.getY()*chunkHeight) + yOff;

                boolean visible = chunkX+chunkWidth > 0 && chunkX < w && chunkY+chunkHeight > 0 && chunkY < h;
                chunk.setVisible(visible);

                //load if visible
                loadMap[chunk.getX()][chunk.getY()] = visible;
            }
        }
        //find chunks that need to be loaded due to relationships
        for (Relationship r: model.getAllRelationships()) {
            Node n1 = r.getN1();
            Node n2 = r.getN2();

            boolean n1Vis = n1.getBelongsTo().isVisible();
            boolean n2Vis = n2.getBelongsTo().isVisible();

            if (n1Vis && !n2Vis)
            {
                //need to load n2
                loadMap[n2.getBelongsTo().getX()][n2.getBelongsTo().getY()] = true;
            }
            else if (!n1Vis && n2Vis)
            {
                //need to load n1
                loadMap[n1.getBelongsTo().getX()][n1.getBelongsTo().getY()] = true;
            }
        }

        //draw loaded chunks
        for (Chunk[] strip: model.getAllChunks())
        {
            for (Chunk chunk: strip)
            {
                if (loadMap[chunk.getX()][chunk.getY()]) {
                    int chunkX = (chunk.getX() * chunkWidth) + xOff;
                    int chunkY = (chunk.getY() * chunkHeight) + yOff;

                    chunk.draw(g, chunkX, chunkY, chunkWidth, chunkHeight);

                    for (Node n : chunk.getNodes()) {
                        float relX = n.getRelativeX() / chunk.getSize();
                        float relY = n.getRelativeY() / chunk.getSize();

                        n.draw(g, (int) (relX * chunkWidth), (int) (relY * chunkHeight));
                    }
                }
            }
        }
        //draw loaded relationships
        for (Relationship r: model.getAllRelationships())
        {
            Node n1 = r.getN1();
            Node n2 = r.getN2();

            if (n1.getBelongsTo().isVisible() || n2.getBelongsTo().isVisible())
            {
                g.drawLine(n1.getDrawX(), n1.getDrawY(), n2.getDrawX(), n2.getDrawY());
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        this.xOff += e.getXOnScreen() - prevX;
        this.yOff += e.getYOnScreen() - prevY;

        this.prevX = e.getXOnScreen();
        this.prevY = e.getYOnScreen();

        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.prevX = e.getXOnScreen();
        this.prevY = e.getYOnScreen();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int d = e.getWheelRotation();

        if (d > 0)
        {
            size = Math.max(1, size - 1);
        }
        else if (d < 0)
        {
            size = Math.min(10, size + 1);
        }

        this.repaint();
    }
}
