package cfh.race;

import static java.util.Objects.*;
import static java.lang.Math.*;


public class Car {

    final String id;
    final double maxVel;
    final double maxTurn;
    
    double x;
    double y;
    
    double vel;
    double dir;
    
    double accel;
    double turn;
    
    public Car(String id, double maxVel, double maxTurn, double x, double y) {
        this.id = requireNonNull(id);
        this.maxVel = requirePositive(maxVel, "maxVel must be greater than zero: ");
        this.maxTurn = requirePositive(maxTurn, "maxTurn must be greater than zero: ");
        this.x = x;
        this.y = y;
    }
    
    private double requirePositive(double value, String message) {
        if (value <= 0)
            throw new IllegalArgumentException(message + value);
        return value;
    }
    
    public void move(long delta) {
        var v = vel * delta / 1_000.0;
        x += cos(dir) * v;
        y += sin(dir) * v;
        
        vel += accel * delta / 1_000.0;
        if (vel < 0) vel = 0;
        else if (vel > maxVel) vel = maxVel;
        if (turn < -maxTurn) turn = -maxTurn;
        else if (turn > maxTurn) turn = maxTurn;
        dir += turn  * v;
    }
}
