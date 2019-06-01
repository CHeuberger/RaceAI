package cfh.race;

import static java.lang.Math.*;
import static java.util.Objects.*;

import java.awt.Point;
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
    
    public Point sensor(int x1, int y1, float ang) {
        switch ((round(ang) / 45) % 8) {
            case -7:
            case  0:
            case  7:
            {
                var x = x1;
                var y = y1;
                var ix = 1;
                var iy = tan(toRadians(ang));
                var err = 0.0;
                while (free(x, y)) {
                    x += ix;
                    err += iy;
                    if (err >= 0.5 ) {
                        y += 1;
                        err -= 1;
                    } else if (err <= -0.5) {
                        y -= 1;
                        err += 1;
                    }
                }
                return new Point(x, y);
            }
            case -6:
            case -5:
            case  1:
            case  2:
            {
                var x = x1;
                var y = y1;
                var ix = tan(toRadians(90-ang));
                var iy = 1;
                var err = 0.0;
                while (free(x, y)) {
                    y += iy;
                    err += ix;
                    if (err >= 0.5 ) {
                        x += 1;
                        err -= 1;
                    } else if (err <= -0.5) {
                        x -= 1;
                        err += 1;
                    }
                }
                return new Point(x, y);
            }
            case -4:
            case -3:
            case  3:
            case  4:
            {
                var x = x1;
                var y = y1;
                var ix = -1;
                var iy = -tan(toRadians(ang));
                var err = 0.0;
                while (free(x, y)) {
                    x += ix;
                    err += iy;
                    if (err >= 0.5 ) {
                        y += 1;
                        err -= 1;
                    } else if (err <= -0.5) {
                        y -= 1;
                        err += 1;
                    }
                }
                return new Point(x, y);
            }
            case -2:
            case -1:
            case  5:
            case  6:
            {
                var x = x1;
                var y = y1;
                var ix = -tan(toRadians(90-ang));
                var iy = -1;
                var err = 0.0;
                while (free(x, y)) {
                    y += iy;
                    err += ix;
                    if (err >= 0.5 ) {
                        x += 1;
                        err -= 1;
                    } else if (err <= -0.5) {
                        x -= 1;
                        err += 1;
                    }
                }
                return new Point(x, y);
            }
        }
        return null;
    }
}
