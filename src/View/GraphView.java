package View;

import Model.Chunk;
import Model.Graph.Graph;
import Model.Graph.Node;
import Model.Graph.Relationship;
import Model.Graph.ViewNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphView extends JPanel implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener{

    Graph model;

    int size, xOff=0, yOff=0, prevX=0, prevY=0, sizeMax;

    public GraphView(Graph model)
    {
        this.model = model;
        sizeMax = model.getAllChunks().size();;
        size = Math.min(5, sizeMax);

        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        this.addMouseWheelListener(this);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.black);

        double w = getWidth(), h = getHeight();

        double chunkWidth = w / size;
        double chunkHeight = h / size;

        int chunkOffX = -sizeMax/2, chunkOffY = -sizeMax/2;

        boolean[][] loadMap = new boolean[model.width][model.height];
        //find all chunks that are on screen
        for (Chunk chunk: model.getAllChunks())
        {
            double chunkX = (chunk.getRelativeX(chunkOffX)*chunkWidth) + xOff;
            double chunkY = (chunk.getRelativeY(chunkOffY)*chunkHeight) + yOff;

            boolean visible = chunkX+chunkWidth > 0 && chunkX < w && chunkY+chunkHeight > 0 && chunkY < h;
            chunk.setVisible(visible);

            //load if visible
            loadMap[chunk.getRelativeX()][chunk.getRelativeY()] = visible;
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
                loadMap[n2.getBelongsTo().getRelativeX()][n2.getBelongsTo().getRelativeY()] = true;
            }
            else if (!n1Vis && n2Vis)
            {
                //need to load n1
                loadMap[n1.getBelongsTo().getRelativeX()][n1.getBelongsTo().getRelativeY()] = true;
            }
        }

        //draw loaded chunks
        for (Chunk chunk: model.getAllChunks())
        {
            if (loadMap[chunk.getRelativeX()][chunk.getRelativeY()]) {
                int chunkX = (int)(chunk.getRelativeX(chunkOffX) * chunkWidth) + xOff;
                int chunkY = (int)(chunk.getRelativeY(chunkOffY) * chunkHeight) + yOff;

                chunk.draw(g, chunkX, chunkY, (int)chunkWidth, (int)chunkHeight);

                for (Node _n : chunk.getNodes()) {
                    ViewNode n = (ViewNode)_n;

                    float relX = n.getRelativeX() / chunk.getSize();
                    float relY = n.getRelativeY() / chunk.getSize();

                    n.draw(g, (int) (relX * chunkWidth), (int) (relY * chunkHeight));
                }
            }
        }
        //draw loaded relationships
        for (Relationship r: model.getAllRelationships())
        {
            ViewNode n1 = (ViewNode) r.getN1();
            ViewNode n2 = (ViewNode) r.getN2();

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

    private void zoomIn()
    {
        size = Math.max(1, size - 1);
    }

    private void zoomOut()
    {
        size = Math.min(sizeMax, size + 1);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int d = e.getWheelRotation();

        if (d > 0)
        {
            zoomIn();
        }
        else if (d < 0)
        {
            zoomOut();
        }

        this.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == '=')//where the + is
        {
            zoomIn();
        }
        else if (e.getKeyChar() == '-')
        {
            zoomOut();
        }

        this.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
