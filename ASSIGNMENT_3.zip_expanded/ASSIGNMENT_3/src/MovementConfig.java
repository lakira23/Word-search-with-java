public class MovementConfig {
    private final double cmPerSecond;
    private final long turn90Ms;
    private final int drawSpeed;
    private final int turnSpeed;

    public MovementConfig(double cmPerSecond, long turn90Ms, int drawSpeed, int turnSpeed) {
        this.cmPerSecond = cmPerSecond;
        this.turn90Ms = turn90Ms;
        this.drawSpeed = drawSpeed;
        this.turnSpeed = turnSpeed;
    }

    public long distanceToMs(double distanceCm) {
        return Math.round((distanceCm / cmPerSecond) * 1000.0);
    }

    public long angleToTurnMs(double angleDegrees) {
        return Math.round((angleDegrees / 90.0) * turn90Ms);
    }

    public int getDrawSpeed() {
        return drawSpeed;
    }

    public int getTurnSpeed() {
        return turnSpeed;
    }
}
