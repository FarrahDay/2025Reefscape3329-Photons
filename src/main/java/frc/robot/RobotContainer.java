package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.Algae;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.SwerveSubsystem;
import swervelib.SwerveInputStream;

import com.pathplanner.lib.auto.AutoBuilder;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class RobotContainer {
  private final SendableChooser<Command> autoChooser;
  public final SwerveSubsystem drivebase = new SwerveSubsystem();
  private Elevator m_Elevator = new Elevator();
  private Algae m_Algae = new Algae();
  public final CommandXboxController m_driverController = new CommandXboxController(OperatorConstants.kDriverControllerPort);
  public final CommandXboxController m_operatorController = new CommandXboxController(OperatorConstants.kOperatorControllerPort);
  public double forward = 0;
  public double strafe = 0;
  public double turn = 0;

  public RobotContainer() {
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Autonomous Chooser", autoChooser);
    setMotorBrake(true);
    drivebase.setDefaultCommand(driveFieldOrientedAngularVelocity);
    configureBindings();
  }

  SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(), () -> forward, () -> strafe)
      .withControllerRotationAxis(() -> -turn).deadband(OperatorConstants.DEADBAND).scaleTranslation(0.8)
      .allianceRelativeControl(true);
      
  Command driveFieldOrientedAngularVelocity = drivebase.driveFieldOriented(driveAngularVelocity);

  private void configureBindings() {
    // algae controls
    // to change positions, while disabled adjust the algae the angle you want and observe the current position
    m_operatorController.a().onTrue(m_Elevator.moveElevatorCommand(5));
    m_operatorController.b().onTrue(m_Elevator.moveElevatorCommand(10));
    m_operatorController.x().onTrue(m_Elevator.moveElevatorCommand(15));
    m_operatorController.y().onTrue(m_Elevator.moveElevatorCommand(20));
    // intake and eject controls
    // don't mess with
    m_operatorController.leftBumper().onTrue(m_Algae.intakeAlgaeCommand());
    m_operatorController.rightBumper().onTrue(m_Algae.ejectAlgaeCommand());
    // elevator controls
    // to change positions, while disabled adjust the elevator to the height you want and observe the current position
    m_operatorController.povUp().onTrue(m_Algae.moveAlgaeCommand(0.1));
    m_operatorController.povRight().onTrue(m_Algae.moveAlgaeCommand(0.2));
    m_operatorController.povLeft().onTrue(m_Algae.moveAlgaeCommand(0.25));
    m_operatorController.povDown().onTrue(m_Algae.moveAlgaeCommand(0));
  }

  public void setMotorBrake(boolean brake) {
    drivebase.setMotorBrake(brake);
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
