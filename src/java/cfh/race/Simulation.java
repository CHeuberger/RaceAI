package cfh.race;

import static java.lang.Math.*;
import static java.util.Objects.*;

import java.awt.Component;
import java.util.List;

import javax.swing.SwingWorker;


public class Simulation extends SwingWorker<Void, Long> {

    private final long MIN_DELTA = 50;
    
    final Race race;
    final Component component;
    
    public Simulation(Race race, Component component) {
        this.race = requireNonNull(race);
        this.component = requireNonNull(component);
    }
    
    @Override
    protected Void doInBackground() throws Exception {
        var time = System.currentTimeMillis();
        while (!Thread.interrupted()) {
            var delta = System.currentTimeMillis() - time;
            if (delta < MIN_DELTA) {
                Thread.sleep(MIN_DELTA - delta);
                delta = System.currentTimeMillis() - time;
            }
            for (Car car : race.cars) {
                if (car.x < 0) {
                    car.x = 0;
                    continue;
                } else if (car.x >= race.loop.getWidth()) {
                    car.x = race.loop.getWidth()-1;
                    continue;
                } else if (car.y < 0) {
                    car.y = 0;
                    continue;
                } else if (car.y > race.loop.getHeight()) {
                    car.y = race.loop.getHeight()-1;
                    continue;
                }
                var rgb = race.loop.getRGB((int) round(car.x), (int) round(car.y));
                var save = true;
                if ((rgb & 0xFF0000) < 253 || (rgb & 0x00FF00) < 253 || (rgb & 0x0000FF) < 253) {
                    if (car.vel > car.maxVel/20) {
                        car.vel = car.maxVel/20;
                    }
                    save = false;
                }
                car.move(delta);
                rgb = race.loop.getRGB((int) round(car.x), (int) round(car.y));
                if (save && ((rgb & 0xFF0000) < 253 || (rgb & 0x00FF00) < 253 || (rgb & 0x0000FF) < 253)) {
                    car.vel = 0;
                    car.accel = 0;
                }
            }
            time += delta;
            publish(time);
        }
        return null;
    }
    
    @Override
    protected void process(List<Long> chunks) {
        component.repaint();
        super.process(chunks);
    }   
}
