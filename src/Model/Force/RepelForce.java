package Model.Force;

import Model.Graph.ForceNode;

public class RepelForce implements IForce {

    int radius;
    double force;

    public RepelForce(int r, double f)
    {
        radius = r;
        force = f;
    }

    @Override
    public void applyForceForFirst(ForceNode n1, ForceNode n2) {
        double x1, y1, x2, y2;
        x1 = n1.getX();
        y1 = n1.getY();
        x2 = n2.getX();
        y2 = n2.getY();

        double[] vec = new double[] {
                x1 - x2,
                y1 - y2
        };

        double vLen = Math.sqrt((vec[0]*vec[0]) + (vec[1]*vec[1]));
        vec[0] /= vLen;
        vec[1] /= vLen;

        if (vLen < radius)
        {
            n1.addForce(vec, this.force);
        }
    }

    @Override
    public void applyForceForBoth(ForceNode n1, ForceNode n2) {
        System.out.println("Not Implemented\n"+ Thread.currentThread().getStackTrace());
    }
}
