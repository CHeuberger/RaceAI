package cfh.race;

import static java.util.Objects.*;

import java.awt.Point;

import static java.lang.Math.*;


public class Car {

    final String id;
    final float maxVel;
    final float maxTurn;
    
    float x;
    float y;
    
    float vel;
    float dir;
    
    float accel;
    float turn;
    
    final Point[] sensors = new Point[5];
    
    
    public Car(String id, float maxVel, float maxTurn, float x, float y) {
        this.id = requireNonNull(id);
        this.maxVel = requirePositive(maxVel, "maxVel must be greater than zero: ");
        this.maxTurn = requirePositive(maxTurn, "maxTurn must be greater than zero: ");
        this.x = x;
        this.y = y;
    }
    
    private float requirePositive(float value, String message) {
        if (value <= 0)
            throw new IllegalArgumentException(message + value);
        return value;
    }
    
    public void move(long delta) {
        var v = vel * delta / 1_000.0;
        var d = toRadians(dir);
        x += v * cos(d);
        y += v * sin(d);
        
        vel += accel * delta / 1_000.0;
        if (vel < 0) {
            vel = 0;
            accel = 0;
        } else if (vel > maxVel) {
            vel = maxVel;
            accel = 0;
        }
        if (turn < -maxTurn) turn = -maxTurn;
        else if (turn > maxTurn) turn = maxTurn;
        dir += turn  * v;
        dir %= 360;
    }
}
