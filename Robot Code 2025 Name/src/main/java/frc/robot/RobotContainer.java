// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.ClimbCommand;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.ClimbSusbsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class RobotContainer {
        //For the auto chooser
        private SendableChooser<Command> autoChooser = new SendableChooser<>();

    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandJoystick joystick = new CommandJoystick(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
    private final ClimbSusbsystem climbSusbsystem = new ClimbSusbsystem();
    private double climbSpeed = 1;
    

    public RobotContainer() {
        
    // Configure the AutoBuilder
    RobotConfig config = null;
    try {
        config = RobotConfig.fromGUISettings();
    } catch (Exception e) {
        // Handle exception as needed
        e.printStackTrace();
    }

    // Configure AutoBuilder last  
    AutoBuilder.configure(
        drivetrain::getPose, 
        drivetrain::resetOdometry, 
        drivetrain::getChassisSpeeds, 
        drivetrain::drive, 
        new PPHolonomicDriveController(
                new PIDConstants(2.0, 0.0, 0.1), 
                new PIDConstants(2.0, 0.0, 0.1)
        ),
        config,
        () -> {
            var alliance = DriverStation.getAlliance();
            if (alliance.isPresent()) {
              return alliance.get() == DriverStation.Alliance.Red;
            }
            return false;
          },
        drivetrain, climbSusbsystem
        );

        configureBindings();
        configureAutoChooser();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystick.getY() * MaxSpeed)
                     .withVelocityY(-joystick.getX() * MaxSpeed)
                     .withRotationalRate(-joystick.getTwist() * MaxAngularRate)
            )
        );
        
        joystick.button(1).whileTrue(drivetrain.applyRequest(() -> brake)); // Trigger for brake

        joystick.button(2).whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getY(), -joystick.getX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        joystick.button(7).and(joystick.button(4)).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        joystick.button(7).and(joystick.button(3)).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        joystick.button(8).and(joystick.button(4)).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        joystick.button(8).and(joystick.button(3)).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // reset the field-centric heading on left bumper press
        joystick.button(5).onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        drivetrain.registerTelemetry(logger::telemeterize);
        //Climb
        new Trigger(joystick.button(5)).whileTrue(new ClimbCommand(climbSusbsystem, climbSpeed));
        new Trigger(joystick.button(6)).whileTrue(new ClimbCommand(climbSusbsystem, -climbSpeed));
    }
    

        private void configureAutoChooser() {
            autoChooser = new SendableChooser<>();
    
            // Add a default auto selection
            Command path = new PathPlannerAuto("test Auto");
            Command climb = new ClimbCommand(climbSusbsystem, -1.0).withTimeout(4);
            Command negativeClimb = new ClimbCommand(climbSusbsystem, 1.0).withTimeout(3);

            autoChooser.setDefaultOption("Test Auto", new SequentialCommandGroup(
                drivetrain.runOnce(() -> drivetrain.seedFieldCentric()), // Optional: reset heading
                //path,   // follow the path
                climb,     // Then activate the climb
                negativeClimb
                ));
            
            autoChooser.addOption("No Auto", new PathPlannerAuto("No Auto"));
            
            SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    public Command getAutonomousCommand() {
        drivetrain.seedFieldCentric(); // Zero the heading
        return autoChooser.getSelected();
    }
}