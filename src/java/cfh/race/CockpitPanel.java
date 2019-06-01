package cfh.race;

import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_SHIFT;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_W;
import static java.lang.Math.*;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JPanel;

public class CockpitPanel extends JPanel {
    
    @SuppressWarnings("hiding")
    private static final int HEIGHT = 50;
    @SuppressWarnings("hiding")
    private static final int WIDTH = 50;
    
    private static final int BORDER = 20;
    private static final int SCALE = 5;

    private Car car = null;
    
    public CockpitPanel() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @SuppressWarnings("incomplete-switch")
            @Override
            public void eventDispatched(AWTEvent event) {
                if (car == null) 
                    return;
                KeyEvent e = (KeyEvent) event;
                if (e.getID() == e.KEY_PRESSED) {
                    switch (e.getKeyCode()) {
                        case VK_SPACE: car.accel = -200; break;
                        case VK_W: car.accel += 2; break;
                        case VK_S: car.accel -= 2; break;
                        case VK_SHIFT: car.turn = 0; break;
                        case VK_A: car.turn -= 0.2; break;
                        case VK_D: car.turn += 0.2; break;
                    }
                } else if (e.getID() == e.KEY_RELEASED) {
                    switch (e.getKeyCode()) {
                        case VK_SPACE: car.accel = 0; break;
                        case VK_W: car.accel = 0; break;
                        case VK_S: car.accel = 0; break;
                    }
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        var gg = (Graphics2D) g.create();
        try {
            if (car != null) {
                gg.drawString(car.id, 4, 12);
            }
            velocity(gg);
            direction(gg);
        } finally {
            gg.dispose();
        }
    }
    
    void car(Car car) {
        this.car = car;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 2*(HEIGHT+BORDER));
    }

    private void velocity(Graphics2D gg) {
        int cx = BORDER + WIDTH;
        int cy = BORDER + HEIGHT;
        
        gg.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        gg.setColor(Color.BLACK);
        gg.drawOval(BORDER, BORDER, WIDTH+WIDTH, HEIGHT+HEIGHT);
        gg.drawOval(cx-2, cy-2, 5, 5);
        
        for (var ang : List.of(135, 45)) {
            var a = toRadians(ang);
            var x1 = cx + (int) (cos(a) * WIDTH);
            var y1 = cy + (int) (sin(a) * HEIGHT);
            var x2 = cx + (int) (cos(a) * (WIDTH-SCALE));
            var y2 = cy + (int) (sin(a) * (HEIGHT-SCALE));
            gg.drawLine(x1, y1, x2, y2);
        }
        
        if (car != null) {
            var a = toRadians(135 + 270 * car.vel / car.maxVel);
            var x2 = cx + (int) (cos(a) * (WIDTH-SCALE));
            var y2 = cy + (int) (sin(a) * (HEIGHT-SCALE));
            gg.drawLine(cx, cy, x2, y2);
        }
    }
    
    private void direction(Graphics2D gg) {
        int w = getWidth()/2 - 2 * (BORDER + WIDTH);
        int h = HEIGHT + HEIGHT + BORDER;
        int cx  = getWidth() / 2;
        int cy = BORDER + h;
        
        gg.setColor(Color.GRAY);
        gg.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        gg.drawOval(cx - w, cy - h, w+w, h+h);
        
        gg.setStroke(new BasicStroke(14, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        if (car != null) {
            var ang = 90 - (int) (80 * car.turn / car.maxTurn);
            var x2 = cx + (int) (cos(toRadians(ang)) * w);
            var y2 = cy - (int) (sin(toRadians(ang)) * h);
            gg.drawLine(cx, cy, x2, y2);
            gg.setColor(Color.BLACK);
            gg.drawArc(cx - w, cy - h, w+w, h+h, ang-1, 3);
        }
    }
}
