package cfh.race;

import static java.util.Objects.*;
import static java.lang.Math.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;


public class RacePanel extends JPanel {

    final Race race;
    private final BufferedImage carImage;

    Car selected = null;
    
    public RacePanel(Race race, BufferedImage carImage) {
        this.race = requireNonNull(race);
        this.carImage = requireNonNull(carImage);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() < 2)
                    return;
                var x = e.getX();
                var y = e.getY();
                Car next = null;
                double dist = Double.MAX_VALUE;
                for (Car car : race.cars) {
                    var dx = car.x - x;
                    var dy = car.y - y;
                    var d = dx*dx + dy*dy;
                    if (d < 144 && d < dist) {
                        next = car;
                        dist = d;
                    }
                }
                selected = next;
                fireSelected(next);
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        var cox = carImage.getWidth() / 2.0;
        var coy = carImage.getHeight() / 2.0;
        
        var gg = (Graphics2D) g.create();
        try {
            
            gg.drawImage(race.loop, 0, 0, this);
            
            AffineTransform xform = new AffineTransform();
            for (var car : race.cars) {
                if (car == selected) {
                    int x1 = (int) round(car.x);
                    int y1 = (int) round(car.y);
                    gg.setColor(Color.BLUE);
                    for (var ang = car.dir-90; ang <= car.dir+90; ang += 45) {
                        Point p = race.sensor(x1, y1, ang);
                        if (p != null) {
                            gg.drawLine(x1, y1, p.x, p.y);
                        }
                    }
                    gg.setXORMode(Color.CYAN);
                }
                xform.setToIdentity();
                xform.translate(car.x - cox, car.y-coy);
                xform.rotate(toRadians(car.dir), cox, coy);
                gg.drawImage(carImage, xform, this);
                gg.setPaintMode();
            }
            
        } finally {
            gg.dispose();
        }
        
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(race.loop.getWidth(this), race.loop.getHeight(this));
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static interface Listener {
        public void selected(Car car);
    }
    
    private final List<Listener> listeners = new ArrayList<>();
    
    public void addListener(Listener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }
    
    private void fireSelected(Car car) {
        for (Listener listener : listeners) {
            listener.selected(car);
        }
    }
}
