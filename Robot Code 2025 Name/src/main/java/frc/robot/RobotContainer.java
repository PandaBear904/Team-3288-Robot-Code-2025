// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.ElevatorCommad;
import frc.robot.commands.IntakeRotateCommand;
import frc.robot.commands.SwitchCameraCommand;
import frc.robot.commands.TestCommand;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.IntakeRotateSubsystem;
import frc.robot.subsystems.TestSubsystem;

public class RobotContainer {


    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed);

    private final CommandXboxController joystick = new CommandXboxController(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    //Subsystems
    private final CameraSubsystem cameraSubsystem = new CameraSubsystem();
    //private final ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();
    private final TestSubsystem testSubsystem = new TestSubsystem();
    //private final IntakeRotateSubsystem intakeRotateSubsystem = new IntakeRotateSubsystem();

    //Speeds
    private double elevatorSpeed = 0.5;
    private double intakeRotatespeed = 0.1;

    //Elevator Positions
    private int ePos1 = 0;
    private int ePos2 = 150;
    private int ePos3 = 250;

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-joystick.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-joystick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        joystick.a().whileTrue(drivetrain.applyRequest(() -> brake));
        joystick.b().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // reset the field-centric heading on left bumper press
        joystick.leftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        drivetrain.registerTelemetry(logger::telemeterize);

        //BUTTONS!!!!!!!!!!!!!
        //Camera
        new Trigger(joystick.button(6).onTrue(new SwitchCameraCommand(cameraSubsystem)));

        //Elevator
        //new Trigger(joystick.y().onTrue(new ElevatorCommad(elevatorSubsystem, elevatorSpeed, elevatorPos)));
        //new Trigger(joystick.x().onTrue(new ElevatorCommad(elevatorSubsystem, elevatorSpeed, elevatorPos2)));
        //new Trigger(joystick.button(6).onTrue(new ElevatorCommad(elevatorSubsystem, elevatorSpeed, elevatorPos3)));

        // Should be able to get rid of soon
        //IntakeRotate
        /*new Trigger (joystick.y().onTrue(new IntakeRotateCommand(intakeRotateSubsystem, intakeRotatespeed, 0)));
        new Trigger (joystick.x().onTrue(new IntakeRotateCommand(intakeRotateSubsystem, intakeRotatespeed, 1.5)));
        new Trigger (joystick.button(6).onTrue(new IntakeRotateCommand(intakeRotateSubsystem, intakeRotatespeed, 3)));
        */

        // Should be able to get rid of soon
        //Test
        //I have no idea what buttons these are need to test (example button(3), button(4))
        //Will need to make a sheet or somthing after testing.
        new Trigger(joystick.button(3).onTrue(new TestCommand(testSubsystem, elevatorSpeed, ePos1)));
        new Trigger(joystick.button(4).onTrue(new TestCommand(testSubsystem, elevatorSpeed, ePos2)));
        new Trigger(joystick.button(4).onTrue(new TestCommand(testSubsystem, elevatorSpeed, ePos3)));

    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
