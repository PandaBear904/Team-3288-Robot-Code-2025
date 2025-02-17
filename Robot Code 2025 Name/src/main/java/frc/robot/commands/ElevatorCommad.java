package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.ElevatorSubsystem;

public class ElevatorCommad extends InstantCommand {
    private final ElevatorSubsystem elevatorSubsystem;
    private final double speed;
    private double desiredPos = 0;

    public ElevatorCommad(ElevatorSubsystem elevatorSubsystem, double speed) {
        this.elevatorSubsystem = elevatorSubsystem;
        this.speed = speed;
        addRequirements(elevatorSubsystem);
    }

    public ElevatorCommad(ElevatorSubsystem elevatorSubsystem, double speed, double desiredPos){
        this.elevatorSubsystem = elevatorSubsystem;
        this.speed = speed;
        this.desiredPos = desiredPos;
        addRequirements(elevatorSubsystem); 
    }

    @Override
    public void initialize(){
        elevatorSubsystem.setElevatorSpeed(speed);
    }

    @Override
    public void execute(){
        elevatorSubsystem.goToPosition(speed, desiredPos);
    }

    @Override
    public void end(boolean interrupted){
        elevatorSubsystem.stopElevator();
    }

    @Override
    public boolean isFinished(){
        return false; // Keep running until the button is released???
    }
    
}
