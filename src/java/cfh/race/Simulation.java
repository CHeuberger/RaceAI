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
                car.move(delta);
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
