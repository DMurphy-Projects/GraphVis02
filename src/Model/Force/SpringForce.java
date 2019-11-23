package Model.Force;

import Model.Graph.ForceNode;

public class SpringForce implements IForce {

    int length, length_2;

    double tightness;

    public SpringForce(int l, double t)
    {
        length = l;
        length_2 = length * length;

        tightness = 1d / t;
    }

    @Override
    public void applyForceForFirst(ForceNode n1, ForceNode n2) {
        System.out.println("Not Implemented\n"+ Thread.currentThread().getStackTrace());
    }

    @Override
    public void applyForceForBoth(ForceNode n1, ForceNode n2) {

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

        double distFromOptimal = Math.abs(vLen-this.length);

        double force = distFromOptimal / tightness;
        if (vLen > length)
        {
            //needs to be smaller

            n1.addForce(vec, -force);
            n2.addForce(vec, force);
        }
        else if (vLen < length)
        {
            //needs to be bigger

            n1.addForce(vec, force);
            n2.addForce(vec, -force);
        }
    }
}
