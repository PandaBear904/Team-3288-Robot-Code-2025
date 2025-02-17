package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.IntakeRotateSubsystem;

public class TestCommand extends InstantCommand {
    private final ElevatorSubsystem elevatorSubsystem;
    private final IntakeRotateSubsystem intakeRotateSubsystem;
    private final double speed;
    private double desiredPos = 0;

    public TestCommand(ElevatorSubsystem elevatorSubsystem, IntakeRotateSubsystem intakeRotateSubsystem, double speed) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.intakeRotateSubsystem = intakeRotateSubsystem;
        this.speed = speed;
        addRequirements(elevatorSubsystem, intakeRotateSubsystem);
    }

    public TestCommand(ElevatorSubsystem elevatorSubsystem, IntakeRotateSubsystem intakeRotateSubsystem, double speed, double desiredPos){
        this.elevatorSubsystem = elevatorSubsystem;
        this.intakeRotateSubsystem = intakeRotateSubsystem;
        this.speed = speed;
        this.desiredPos = desiredPos;
        addRequirements(elevatorSubsystem, intakeRotateSubsystem); 
    }

    @Override
    public void initialize(){
        elevatorSubsystem.setElevatorSpeed(speed);
        intakeRotateSubsystem.setRotateSpeed(speed);
    }

    @Override
    public void execute(){
        elevatorSubsystem.goToPosition(speed, desiredPos);
        intakeRotateSubsystem.intakeToPos(desiredPos, speed);
    }

    @Override
    public void end(boolean interrupted){
        elevatorSubsystem.stopElevator();
        intakeRotateSubsystem.stopRotate();
    }
    
    @Override
    public boolean isFinished(){
        return false; // Keep running until the button is released???
    }
}
