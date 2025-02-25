package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.TestSubsystem;


/*
 * I don't thing I will have to mess with this
 */

public class TestCommand extends InstantCommand {
    private final TestSubsystem testSubsystem;
    private final double speed;
    private double desiredPos = 0;

    public TestCommand(TestSubsystem testSubsystem, double speed, double desiredPos) {
        this.testSubsystem = testSubsystem;
        this.speed = speed;
        this.desiredPos = desiredPos;
        addRequirements(testSubsystem);
    }

    @Override
    public void execute() {
        testSubsystem.goToPosition(speed, desiredPos);
    }

    @Override
    public void initialize() {
        testSubsystem.setTestSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        testSubsystem.setTestSpeed(0);
    }

    @Override
    public boolean isFinished() {
        return true; // or return false; depending on your logic
    }
}