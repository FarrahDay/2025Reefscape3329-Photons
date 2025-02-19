// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;
  private PhotonCamera camera;
  
    private double forward;
    private double strafe;
    private double turn;
    private boolean targetVisible;

    //private static ProfiledPIDController turnPID;
    private static PIDController turnPID;
    private static PIDController forwardPID;

    public Robot() {
      m_robotContainer = new RobotContainer();
    }
      
    @Override
    public void robotInit(){
      camera = new PhotonCamera("Yellow Cam");    
    }

    @Override
    public void robotPeriodic() {
      CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {}
      
    @Override
    public void disabledPeriodic() {}
      
    @Override
    public void autonomousInit()
    {
      m_robotContainer.setMotorBrake(true);
      m_autonomousCommand = m_robotContainer.getAutonomousCommand();
      if (m_autonomousCommand != null)
      {
        m_autonomousCommand.schedule();
      }
    }

    @Override
    public void autonomousPeriodic() {}
      
    @Override
    public void teleopInit() {
      double turnTarget = 0;
      double forwardTarget = 0;
      turnPID = new PIDController(Constants.VisionConstants.turnP,
                                  Constants.VisionConstants.turnI,
                                  Constants.VisionConstants.turnD);
      forwardPID = new PIDController(Constants.VisionConstants.forwardP,
                                      Constants.VisionConstants.forwardI, 
                                      Constants.VisionConstants.forwardD);
      setTargetTurn(turnTarget);
      setTargetForward(forwardTarget);
      if (m_autonomousCommand != null) {
        m_autonomousCommand.cancel();
      }
    }

    public void setTargetTurn(double target){
      turnPID.setSetpoint(target);
    }

    public void setTargetForward(double target){
      forwardPID.setSetpoint(target);
    }
  
    @Override
    public void teleopPeriodic() {
      forward = -m_robotContainer.m_driverController.getLeftY();
      strafe = -m_robotContainer.m_driverController.getLeftX();
      turn = -m_robotContainer.m_driverController.getRightX();
      double targetYaw = 0.0;
      double targetRange = 0.0;
      var results = camera.getAllUnreadResults();
      if(!results.isEmpty()){
        var result = results.get(results.size() - 1);
        if(result.hasTargets()){
          for(var target : result.getTargets()){
            if(target.getFiducialId() == 7){
              targetYaw = target.getYaw();
              targetRange = PhotonUtils.calculateDistanceToTargetMeters(0.171, 0.3048, Units.degreesToRadians(20), Units.degreesToRadians(target.getPitch()));
              targetVisible = true;
            }
          }
        }
      }
      else{
        targetVisible = false;
      }
      m_robotContainer.strafe = -this.strafe;
      SmartDashboard.putNumber("Drive Forward", m_robotContainer.forward);
      SmartDashboard.putNumber("Drive Strafe", m_robotContainer.strafe);

    if(m_robotContainer.m_driverController.a().getAsBoolean() && targetVisible){
      //turn = turnPID.calculate(targetYaw);
      forward = forwardPID.calculate(0.5 - targetRange);
    }

    m_robotContainer.turn = -this.turn;
    m_robotContainer.forward = -this.forward;
    SmartDashboard.putNumber("Drive Turn", m_robotContainer.turn);
    SmartDashboard.putBoolean("Visible", targetVisible);
    SmartDashboard.putNumber("Turn", turn);
    SmartDashboard.putNumber("Forward", forward);
    SmartDashboard.putNumber("Strafe", strafe);
    SmartDashboard.putNumber("Target Yaw", targetYaw);
    SmartDashboard.putNumber("Target Range", targetRange);
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
