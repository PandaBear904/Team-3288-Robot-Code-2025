package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.TestSubsystem;

public class TestCommand extends InstantCommand {
    private final TestSubsystem testSubsystem;
    private final double speed;
    private double desiredPos = 0;
    private double rotatePos = 0;
    private double onPos = 0;

    public TestCommand(TestSubsystem testSubsystem, double speed) {
        this.testSubsystem = testSubsystem;
        this.speed = speed;
        addRequirements(testSubsystem);
    }

    public TestCommand(TestSubsystem testSubsystem, double speed, double desiredPos){
        this.testSubsystem = testSubsystem;
        this.speed = speed;
        this.desiredPos = desiredPos;
        addRequirements(testSubsystem); 
    }

    public TestCommand(TestSubsystem testSubsystem, double speed, double desiredPos, double rotatePos, double onPos){
        this.testSubsystem = testSubsystem;
        this.speed = speed;
        this.desiredPos = desiredPos;
        this.rotatePos = rotatePos;
        this.onPos = onPos;
        addRequirements(testSubsystem); 
    }

    @Override
    public void initialize(){
        testSubsystem.setTestSpeed(speed);
    }

    @Override
    public void execute(){
        testSubsystem.goToPosition(speed, desiredPos, rotatePos, onPos);
    }

    @Override
    public void end(boolean interrupted){
        testSubsystem.stopTest();
    }

    @Override
    public boolean isFinished(){
        return false; // Keep running until the button is released???
    }
    
}
