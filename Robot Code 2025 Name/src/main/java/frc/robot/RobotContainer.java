// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.*;

//PathPlanner Stuff
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.commands.PathPlannerAuto;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//Stuff
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
//Imports for subsystem/commands
//import frc.robot.commands.ElevatorCommad;
//import frc.robot.commands.SwitchCameraCommand;
//import frc.robot.subsystems.CameraSubsystem;
//import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.ClimbSusbsystem;
//import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.commands.ClimbCommand;
import frc.robot.generated.TunerConstants;
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

    private final CommandXboxController joystick = new CommandXboxController(0);

    //private final CommandXboxController opJoystick = new CommandXboxController(1);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    //Subsystems
    //private final CameraSubsystem cameraSubsystem = new CameraSubsystem();
    //private final ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();
    private final ClimbSusbsystem climbSusbsystem = new ClimbSusbsystem();
    /*

    //Speeds
    private double elevatorSpeed = 1;

    //Elevator Positions
    private int elevatorPos1 = 0;    // This are the # tested on 3/8/2035 These were with out the intake working
    private int elevatorPos2 = 4450; // 4450 
    private int elevatorPos3 = 6600; // 6600
    private int elevatorPos4 = 7700; // 7700
    private int elevatorPos5 = 1000;


    //Intake Positions
    private double intakeOn = 120;
    private double intakeOff = 0;

    //Intake Rotate Positions
    private double intakeRotatePos1 = 3;
    private double intakeRotatePos2 = 55;
    private int intakePos = 50;
    */
    //Climb
    private double climbSpeed = 1;

    boolean isCompetition = true;

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
                new PIDConstants(5.0, 0.0, 0.0), 
                new PIDConstants(5.0, 0.0, 0.0)
        ),
        config,
        () -> {
            var alliance =  DriverStation.Alliance.Red;//DriverStation.getAlliance();
            return true;//alliance.isPresent() && alliance.get() == DriverStation.Alliance.Red;
        },
        drivetrain, climbSusbsystem//, cameraSubsystem//, elevatorSubsystem
        );

        configureBindings();
        configureAutoChooser();
    }

    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(joystick.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(joystick.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-joystick.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        joystick.x().whileTrue(drivetrain.applyRequest(() -> brake));
        joystick.y().whileTrue(drivetrain.applyRequest(() ->
            point.withModuleDirection(new Rotation2d(-joystick.getLeftY(), -joystick.getLeftX()))
        ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        joystick.back().and(joystick.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        joystick.back().and(joystick.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        joystick.start().and(joystick.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        joystick.start().and(joystick.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // reset the field-centric heading on left bumper press
        joystick.a().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        drivetrain.registerTelemetry(logger::telemeterize);

        //BUTTONS!!!!!!!!!!!!!
        //Camera
        //new Trigger(joystick.b().onTrue(new SwitchCameraCommand(cameraSubsystem)));
        
        //Climb
        new Trigger(joystick.button(5)).whileTrue(new ClimbCommand(climbSusbsystem, climbSpeed));
        new Trigger(joystick.button(6)).whileTrue(new ClimbCommand(climbSusbsystem, -climbSpeed));

        //Elevator
        /*
        //With OP controll
        //Starting Config
        new Trigger(opJoystick.y().onTrue(new ElevatorCommad(elevatorSubsystem, elevatorSpeed, elevatorPos1, intakeRotatePos1, intakeOff)));
        //L1/L2??
        new Trigger(opJoystick.x().onTrue(new ElevatorCommad(elevatorSubsystem, elevatorSpeed, elevatorPos2, intakeRotatePos2, intakeOn)));
        //L2/L3??
        new Trigger(opJoystick.button(6).onTrue(new ElevatorCommad(elevatorSubsystem, elevatorSpeed, elevatorPos3, intakeRotatePos2, intakeOn)));
        //L3/L4??
        new Trigger(opJoystick.button(5).onTrue(new ElevatorCommad(elevatorSubsystem, elevatorSpeed, elevatorPos4, intakeRotatePos2, intakeOn)));
        //Intake game peice
        new Trigger(opJoystick.a().onTrue(new ElevatorCommad(elevatorSubsystem, elevatorSpeed, elevatorPos5, intakePos, -intakeOn)));

        //new Trigger(opJoystick.b()).onTrue(new IntakeCommand(elevatorSubsystem, elevatorSpeed, intakeOn));
        */

        //With Driver controll
        /*
        new Trigger(joystick.y().onTrue(new ElevatorCommad(elevatorSubsystem, elevatorSpeed, elevatorPos1, intakeRotatePos1, intakeOff)));
        new Trigger(joystick.x().onTrue(new ElevatorCommad(elevatorSubsystem, elevatorSpeed, elevatorPos2, intakeRotatePos2, intakeOn)));
        new Trigger(joystick.button(6).onTrue(new ElevatorCommad(elevatorSubsystem, elevatorSpeed, elevatorPos3, intakeRotatePos3, intakeOn)));
        new Trigger(joystick.button(5).onTrue(new ElevatorCommad(elevatorSubsystem, elevatorSpeed, elevatorPos3, intakeRotatePos4, intakeOff)));
        new Trigger(joystick.a().onTrue(new IntakeCommand(elevatorSubsystem, intakeRotatespeed, intakeOn)));
        */
    }


    private void configureAutoChooser() {
        autoChooser = new SendableChooser<>();

        // Add a default auto selection
        autoChooser.setDefaultOption("Test Auto", new PathPlannerAuto("test Auto"));
        
        autoChooser.addOption("No Auto", new PathPlannerAuto("No Auto"));
        
        SmartDashboard.putData("Auto Chooser", autoChooser);
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}