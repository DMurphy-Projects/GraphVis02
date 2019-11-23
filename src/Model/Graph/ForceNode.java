package Model.Graph;

import java.util.ArrayList;
import java.util.List;

public class ForceNode extends ViewNode{

    public ForceNode(int x, int y) {
        super(x, y);
    }

    public class ForceInstance
    {
        double[] vector;
        double force;

        public ForceInstance(double[] v, double f)
        {
            vector = v;
            force = f;
        }

        public double[] getForceVector()
        {
            return new double[]{
                    vector[0] * force,
                    vector[1] * force,
            };
        }

        public double[] getVector() {
            return vector;
        }

        public double getForce() {
            return force;
        }
    }

    List<ForceInstance> forces = new ArrayList<>();

    public void addForce(double[] vector, double force)
    {
        forces.add(new ForceInstance(vector, force));
    }

    //returns a magnitude vector, perhaps not the correct term, ie not a unit vector
    public ForceInstance findResultantForce()
    {
        double sumX = 0, sumY = 0;

        for (ForceInstance f: forces)
        {
            double[] v = f.vector;

            sumX += v[0] * f.force;
            sumY += v[1] * f.force;
        }

        double[] newVec = new double[]{sumX, sumY};

        if (sumX == 0 && sumY == 0)
        {
            return new ForceInstance(newVec, 0);
        }
        else
        {
            double vLen = Math.sqrt((newVec[0]*newVec[0]) + (newVec[1]*newVec[1]));

            newVec[0] /= vLen;
            newVec[1] /= vLen;

            forces.clear();
            //adds a flat amount of decay
            double decay = 0.1;
            forces.add(new ForceInstance(newVec, Math.max(vLen - decay, 0)));

            return new ForceInstance(newVec, vLen);
        }
    }
}
