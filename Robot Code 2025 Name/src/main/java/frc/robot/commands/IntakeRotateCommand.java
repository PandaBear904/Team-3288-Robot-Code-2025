package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.IntakeRotateSubsystem;

public class IntakeRotateCommand extends InstantCommand {
    private final IntakeRotateSubsystem intakeRotateSubsystem;
    private final double speed;
    private double desiredPos = 0;

    public IntakeRotateCommand(IntakeRotateSubsystem intakeSubsystem, double speed) {
        this.intakeRotateSubsystem = intakeSubsystem;
        this.speed = speed;
        addRequirements(intakeSubsystem);
    }

    public IntakeRotateCommand(IntakeRotateSubsystem intakeSubsystem, double speed, double desiredPos) {
        this.intakeRotateSubsystem = intakeSubsystem;
        this.speed = speed;
        this.desiredPos = desiredPos;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void initialize(){
        intakeRotateSubsystem.setRotateSpeed(speed);
    }

    @Override
    public void execute(){
        intakeRotateSubsystem.intakeToPos(desiredPos, speed);
    }

    @Override
    public void end(boolean interrupted){
        intakeRotateSubsystem.stopRotate();
    }

    @Override
    public boolean isFinished(){
        return false;
    }
    
}
