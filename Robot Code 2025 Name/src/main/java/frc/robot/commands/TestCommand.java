
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.TestSubsystem;

public class TestCommand extends InstantCommand {
    private final TestSubsystem testSubsystem;
    private final double speed;

    public TestCommand(TestSubsystem testSubsystem, double speed) {
        this.testSubsystem = testSubsystem;
        this.speed = speed;
        addRequirements(testSubsystem);
    }

    @Override
    public void initialize(){
        testSubsystem.setTestSpeed(speed);
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
