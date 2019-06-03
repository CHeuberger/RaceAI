package cfh.race;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import cfh.race.mind.StraightSensor;


public class Main {

    public static void main(String[] args) {
        try {
            new Main();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private final Race race;
    private final BufferedImage carImage;
    
    private RacePanel loop;
    private CockpitPanel cockpit;
    private Simulation simulation;
    private JToggleButton activeButton;
    
    
    private Main() throws IOException {
        var name = "Oval.png";
        var carName = "Car.png";
        
        race = readRace(name);
        carImage = ImageIO.read(ClassLoader.getSystemResourceAsStream(carName));
        
        SwingUtilities.invokeLater(this::init);
    }
    
    private void init() {
        cockpit = new CockpitPanel();
        
        var restartButton = new JButton("Restart");
        restartButton.addActionListener(this::doRestart);
        
        activeButton = new JToggleButton("Active");
        activeButton.addActionListener(this::doActive);
        activeButton.setFocusable(false);
        
        var buttons = Box.createHorizontalBox();
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(activeButton);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(restartButton);
        buttons.add(Box.createHorizontalStrut(10));
        
        loop = new RacePanel(race, carImage);
        loop.addListener(cockpit::car);
        
        var frame = new JFrame("Race");
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(buttons, BorderLayout.PAGE_START);
        frame.add(new JScrollPane(loop), BorderLayout.CENTER);
        frame.add(cockpit, BorderLayout.PAGE_END);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        simulation = new Simulation(race, frame);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                simulation.cancel(true);
            }
        });
        doRestart(null);
        simulation.execute();
    }

    private Race readRace(String name) throws IOException {
        try (var inp = ClassLoader.getSystemResourceAsStream(name)) {
            if (inp == null) 
                throw new IOException("unable to find \"" + name + "\"");

            var img = ImageIO.read(inp);
            return new Race(name, img);
        }
    }
    
    private void doActive(ActionEvent ev) {
        simulation.active(((JToggleButton) ev.getSource()).isSelected());
    }
    
    private void doRestart(ActionEvent ev) {
        race.cars.clear();
        loop.selected = null;
        var x = 300;
        var y = 100;
        for (var i = 0; i < 5; i++) {
            var car = new Car(Integer.toString(i), new StraightSensor(), 200, 2, x, y);
            race.cars.add(car);
            y += 19;
        }
        simulation.active(false);
        activeButton.setSelected(false);
        loop.repaint();
    }
}
