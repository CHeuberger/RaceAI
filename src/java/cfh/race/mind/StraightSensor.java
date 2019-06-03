package cfh.race.mind;

import cfh.race.Mind;


public class StraightSensor implements Mind {

    @Override
    public Output control(long delta, float vel, float accel, float turn, double[] distances) {
        float t = turn(turn, distances);
        float a = acceleration(vel, distances);
        return new Output(a, t);
    }

    private float turn(float turn, double[] distances) {
        var l = distances[1];
        var f = distances[2];
        var r = distances[3];
        var aim = (float) (1*(r - l) / f);
        return aim;
    }
    
    private float acceleration(float vel, double[] distances) {
        var d = (float) distances[2];
        var aim = 5 * (d-10) / 5;
        float a = aim - vel;
        return a;
    }
}
