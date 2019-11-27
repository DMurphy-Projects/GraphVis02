package Model.Graph;

import java.util.ArrayList;
import java.util.List;

public class ForceNode extends ViewNode{

    double velocity, c;
    double decay = 0.001;

    public ForceNode(int x, int y) {
        super(x, y);

        //larger the value, less explosive
        c = 1000;

        velocity = 0;
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
        int total = forces.size();

        for (ForceInstance f: new ArrayList<>(forces))
        {
            double[] v = f.vector;
            double force = f.force / total;

            sumX += v[0] * force;
            sumY += v[1] * force;
        }

        double[] newVec = new double[]{sumX, sumY};

        if (sumX == 0 && sumY == 0)
        {
            return new ForceInstance(newVec, 0);
        }
        else
        {
            double scale = c / Math.max(c, Math.pow(velocity, 2));

            double vLen = Math.sqrt((newVec[0]*newVec[0]) + (newVec[1]*newVec[1]));

            newVec[0] /= vLen;
            newVec[1] /= vLen;

            forces.clear();
            //adds a flat amount of decay
            velocity = Math.max(vLen - decay, 0);

            forces.add(new ForceInstance(newVec, velocity));

            return new ForceInstance(newVec, velocity*scale);
        }
    }
}
