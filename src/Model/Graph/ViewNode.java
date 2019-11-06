package Model.Graph;

import Model.Chunk;

import java.awt.*;

public class ViewNode extends Node {

    int dx, dy;
    float rX, rY;

    int diam = 2, rad;

    public ViewNode(int x, int y)
    {
        super(x, y);

        rad = diam / 2;
    }

    @Override
    public void belongsTo(Chunk c) {
        super.belongsTo(c);

        updateRelative();
    }

    @Override
    public void move(double x, double y) {
        super.move(x, y);

        updateRelative();
    }

    private void updateRelative()
    {
        if (belongsTo != null) {
            int size = belongsTo.getSize();
            rX = (float) (this.x % size);
            rY = (float) (this.y % size);
        }
    }

    public void draw(Graphics g, int x, int y)
    {
        this.dx = x + belongsTo.getDrawX();
        this.dy = y + belongsTo.getDrawY();

        g.drawOval(dx - rad, dy - rad, diam, diam);
    }

    public float getRelativeX() {
        return rX;
    }

    public float getRelativeY() {
        return rY;
    }

    public int getDrawX() {
        return dx;
    }

    public int getDrawY() {
        return dy;
    }
}
