package cfh.race;

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
}
