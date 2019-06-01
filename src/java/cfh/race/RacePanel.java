package cfh.race;

import static java.util.Objects.*;
import static java.lang.Math.*;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class RacePanel extends JPanel {

    final Race race;
    
    private final BufferedImage carImage;
    
    public RacePanel(Race race, BufferedImage carImage) {
        this.race = requireNonNull(race);
        this.carImage = requireNonNull(carImage);
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
                xform.setToIdentity();
                xform.translate(car.x - cox, car.y-coy);
                xform.rotate(toRadians(car.dir), cox, coy);
                gg.drawImage(carImage, xform, this);
            }
            
        } finally {
            gg.dispose();
        }
        
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(race.loop.getWidth(this), race.loop.getHeight(this));
    }
}
