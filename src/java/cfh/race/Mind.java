package cfh.race;

public interface Mind {

    public interface Output {
        public float accel();
        public float turn();
    }

    public Output control(long delta, float vel, double[] distances);
}
