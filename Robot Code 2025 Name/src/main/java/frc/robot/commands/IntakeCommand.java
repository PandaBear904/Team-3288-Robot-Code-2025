package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.ElevatorSubsystem;

public class IntakeCommand extends InstantCommand {
    private final ElevatorSubsystem elevatorSubsystem;
    private final double speed;
    private double desiredPos = 0;

    public IntakeCommand(ElevatorSubsystem elevatorSubsystem, double speed, double desiredPos){
        this.elevatorSubsystem = elevatorSubsystem;
        this.speed = speed;
        this.desiredPos = desiredPos;
        addRequirements(elevatorSubsystem); 
    }

    @Override
    public void execute(){
        //elevatorSubsystem.intakeOn(speed, desiredPos);
    }
}
