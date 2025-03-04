package frc.robot;

import edu.wpi.first.math.util.Units;

public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
    public static final int kOperatorControllerPort = 1;
    public static final double DEADBAND = 0.05;
  }

  public static class ElevatorConstants {
    public static final int rightID = 13;
    public static final int leftID = 14;
    public static final double maxVelocity = 0.3;
    public static final double maxAcceleration = 0.3;
  }

  public static class AlgaeConstants {
    public static final int pivotID = 17;
    public static final int intakeID = 18;
    public static final int encoderID = 3;
    public static final double intakeSpeed = 1;
    public static final double ejectSpeed = 0.8;
    public static final double kP = 6;
    public static final double kI = 0;
    public static final double kD = 0;
    public static final double maxVelocity = 0.3;
    public static final double maxAcceleration = 0.15;
  }


  
  public static final double maxSpeed = Units.feetToMeters(4.5);
}
