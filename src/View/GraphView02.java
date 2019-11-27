package View;

import Model.Chunk;
import Model.Graph.ForceNode;
import Model.Graph.Node;
import Model.Graph.Relationship;
import Model.Graph.ViewNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;

public class GraphView02 extends JPanel implements MouseMotionListener, MouseListener, KeyListener{

    GraphViewController controller;

    int prevX, prevY;

    boolean lock = false;

    ForceNode grabbed = null;

    public GraphView02(GraphViewController vCon)
    {
        controller = vCon;
    }

    public void update()
    {
        lock = true;

        this.repaint();

        sync();
    }

    public void sync()
    {
        while (lock)
        {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }

    @Override
    public void paint(Graphics g) {

        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.black);

        int w = getWidth(), h = getHeight();

        Collection<Chunk> loadedChunks = controller.getLoadedChunks();
        for (Chunk c: loadedChunks)
        {
            int size = c.getSize();
            double[] point = controller.transform(c.getRelativeX(), c.getRelativeY());
            double[] dim = controller.transform(c.getRelativeX(size), c.getRelativeY(size));

            int x = (int)(point[0]*w);
            int y = (int)(point[1]*h);
            int _w = (int)(dim[0]*w) - x;
            int _h = (int)(dim[1]*h) - y;

            g.setColor(Color.gray.brighter());
            c.draw(g, x, y, _w, _h);
            g.setColor(Color.black);

            for (Node _n : c.getNodes()) {

                ViewNode n = (ViewNode)_n;

                float relX = n.getRelativeX() / c.getSize();
                float relY = n.getRelativeY() / c.getSize();

                n.draw(g, (int) (relX * _w), (int) (relY * _h));
            }
        }

        //draw loaded relationships
        ArrayList<Relationship> loadedRelationships = controller.getLoadedRelationships();
        for (Relationship r: loadedRelationships)
        {
            ViewNode n1 = (ViewNode) r.getN1();
            ViewNode n2 = (ViewNode) r.getN2();

            g.drawLine(n1.getDrawX(), n1.getDrawY(), n2.getDrawX(), n2.getDrawY());
        }

        lock = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    private void grabANode(double x, double y)
    {
        ForceNode n = (ForceNode) controller.getNodeAtPosition(x, y);
        grabbed = n;
    }

    private boolean tickGrabbedNode(double x, double y)
    {
        if (grabbed != null)
        {
            double[] v = new double[]{
                    x - grabbed.getX(),
                    y - grabbed.getY()
            };

            grabbed.addForce(v, 0.1);
            return true;
        }
        return false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        prevX = e.getXOnScreen();
        prevY = e.getYOnScreen();

        double[] p = controller.reversePoint(e.getX(), e.getY(), getWidth(), getHeight());
        grabANode(p[0], p[1]);
        tickGrabbedNode(p[0], p[1]);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        grabbed = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (grabbed != null) {
            double[] p = controller.reversePoint(e.getX(), e.getY(), getWidth(), getHeight());
            tickGrabbedNode(p[0], p[1]);
        }
        else
        {
            double xOff = e.getXOnScreen() - prevX;
            double yOff = e.getYOnScreen() - prevY;

            controller.move(xOff / getWidth(), yOff / getHeight());

            prevX = e.getXOnScreen();
            prevY = e.getYOnScreen();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == '=')//where the + is
        {
            controller.zoom(0.75);
        }
        else if (e.getKeyChar() == '-')
        {
            controller.zoom(1.33);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
