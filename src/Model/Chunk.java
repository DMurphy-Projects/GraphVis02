package Model;

import Model.Graph.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Chunk {

    ArrayList<Node> nodes = new ArrayList<>();

    int x, y, size;

    int dx, dy, dw, dh;

    boolean visible;

    public Chunk(int x, int y, int size)
    {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void addNode(Node n)
    {
        nodes.add(n);

        n.belongsTo(this);
    }

    public void removeNode(Node n)
    {
        nodes.remove(n);
        n.belongsTo(null);
    }

    public void draw(Graphics g, int x, int y, int w, int h)
    {
        this.dx = x;
        this.dy = y;
        this.dw = w;
        this.dh = h;

        g.drawRect(x, y, w, h);
    }

    public boolean isThisChunk(Point p)
    {
        return p.x == x && p.y == y;
    }

    public int getX() {
        return x;
    }
    public int getX(int off) {
        return getX() + off;
    }


    public int getY(int off) {
        return getY() + off;
    }
    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public List<Node> getNodes() {
        return (List<Node>) nodes.clone();
    }

    public void setVisible(boolean b)
    {
        visible = b;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getDrawX() {
        return dx;
    }

    public int getDrawY() {
        return dy;
    }
}
