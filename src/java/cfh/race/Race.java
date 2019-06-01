package cfh.race;

import static java.lang.Math.*;
import static java.util.Objects.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class Race {

    final String name;
    final BufferedImage loop;
    final List<Car> cars;
    
    public Race(String name, BufferedImage loop) {
        this.name = requireNonNull(name);
        this.loop = requireNonNull(loop);
        this.cars = new ArrayList<>();
    }
    
    public boolean free(double x, double y) {
        return free((int) round(x), (int) round(y));
    }
    
    public boolean free(int x, int y) {
        var rgb = loop.getRGB(x, y);
        return (rgb & 0xFF0000) > 253 && (rgb & 0x00FF00) > 253 && (rgb & 0x0000FF) > 253;
    }
}
