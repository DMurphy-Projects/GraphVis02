package Model;

import java.awt.*;

public interface ChunkTranslate {

    void setup(int x_translate, int y_translate, int x_scale, int y_scale);

    Point translate(double x, double y);
}
