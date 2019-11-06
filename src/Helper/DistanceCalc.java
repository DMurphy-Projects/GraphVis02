package Helper;

public class DistanceCalc {

    public static double getDistanceNoRoot(double x1, double y1, double x2, double y2)
    {
        double x = x1 - x2;
        double y = y1 - y2;

        return (x*x) + (y*y);
    }
}
