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
    }

    List<ForceInstance> forces = new ArrayList<>();

    public void addForce(double[] vector, double force)
    {
        forces.add(new ForceInstance(vector, force));
    }

    //returns a magnitude vector, perhaps not the correct term, ie not a unit vector
    public double[] findResultantForce()
    {
        double sumX = 0, sumY = 0;

        for (ForceInstance f: forces)
        {
            double[] v = f.vector;

            sumX += v[0] * f.force;
            sumY += v[1] * f.force;
        }

        forces.clear();

        return new double[]{sumX, sumY};
    }
}
