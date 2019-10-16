package Model;

import Model.Graph.Node;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Chunk {

    List<Node> nodes = new ArrayList<>();

    int x, y, size;

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

    public boolean isThisChunk(Point p)
    {
        return p.x == x && p.y == y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setVisible(boolean b)
    {
        visible = b;
    }

    public boolean isVisible() {
        return visible;
    }
}
