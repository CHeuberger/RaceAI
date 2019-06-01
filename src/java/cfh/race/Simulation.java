package cfh.race;

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
                var save = true;
                if (!race.free(car.x,  car.y)) {
                    if (car.vel > car.maxVel/20) {
                        car.vel = car.maxVel/20;
                    }
                    save = false;
                }
                
                car.move(delta);

                if (save && !race.free(car.x, car.y)) {
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
