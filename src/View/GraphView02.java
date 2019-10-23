package View;

import Model.Chunk;
import Model.Graph.Node;
import Model.Graph.Relationship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GraphView02 extends JPanel implements MouseMotionListener, MouseListener, KeyListener{

    GraphViewController controller;

    int prevX, prevY;

    public GraphView02(GraphViewController vCon)
    {
        controller = vCon;
    }

    public void update()
    {
        controller.update();

        this.repaint();
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

        ArrayList<Chunk> loadedChunks = controller.getLoadedChunks();
        for (Chunk c: loadedChunks)
        {
            double size = c.getSize();

            double[] point = controller.transform(c.getX() * size, c.getY() * size);
            double[] dim = controller.transform(c.getX(1) * size, c.getY(1) * size);

            int x = (int)(point[0]*w);
            int y = (int)(point[1]*h);
            int _w = (int)(dim[0]*w) - x;
            int _h = (int)(dim[1]*h) - y;

            c.draw(g, x, y, _w, _h);

            for (Node n : c.getNodes()) {
                float relX = n.getRelativeX() / c.getSize();
                float relY = n.getRelativeY() / c.getSize();

                n.draw(g, (int) (relX * _w), (int) (relY * _h));
            }
        }

        //draw loaded relationships
        for (Relationship r: controller.getLoadedRelationships())
        {
            Node n1 = r.getN1();
            Node n2 = r.getN2();

            g.drawLine(n1.getDrawX(), n1.getDrawY(), n2.getDrawX(), n2.getDrawY());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        prevX = e.getXOnScreen();
        prevY = e.getYOnScreen();
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
    public void mouseDragged(MouseEvent e) {
        double xOff = e.getXOnScreen() - prevX;
        double yOff = e.getYOnScreen() - prevY;

        controller.move(xOff / getWidth(), yOff / getHeight());

        prevX = e.getXOnScreen();
        prevY = e.getYOnScreen();

        update();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == '=')//where the + is
        {
            controller.zoom(0.25);
            update();
        }
        else if (e.getKeyChar() == '-')
        {
            controller.zoom(-0.25);
            update();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
