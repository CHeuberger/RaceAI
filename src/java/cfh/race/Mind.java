package cfh.race;

public interface Mind {

    public static class Output {
        final float accel;
        final float turn;
        public Output(float accel, float turn) {
            this.accel = accel;
            this.turn = turn;
        }
    }

    public Output control(long delta, float vel, float accel, float turn, double[] distances);
}
