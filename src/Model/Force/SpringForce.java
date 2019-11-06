package Model.Force;

import Helper.DistanceCalc;
import Model.Graph.ForceNode;

public class SpringForce implements IForce {

    int length, length_2;
    double sqrt2_2 = 2 * Math.sqrt(2);

    public SpringForce(int l)
    {
        length = l;
        length_2 = length * length;
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

        //nodes move in pairs, with this force
        double force = distFromOptimal / 2;
        if (vLen > length)
        {
            //needs to be smaller

            n1.addForce(vec, -force);
            n2.addForce(vec, force);

//            System.out.println("Smaller: "+vLen);
        }
        else if (vLen < length)
        {
            //needs to be bigger

            n1.addForce(vec, force);
            n2.addForce(vec, -force);

//            System.out.println("Bigger: "+vLen);
        }
    }
}
