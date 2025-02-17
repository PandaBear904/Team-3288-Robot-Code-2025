package frc.robot.commands;
import frc.robot.subsystems.IntakeSpin;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class IntakeSpinCommand extends InstantCommand {
private final IntakeSpin intakeSpin;
private final double speed;

    public IntakeSpinCommand(IntakeSpin intakeSpin, double speed) {
        this.intakeSpin = intakeSpin;
        this.speed = speed;
        addRequirements(intakeSpin);
    }

    @Override
    public void initialize() {
        intakeSpin.setIntakeSpeed(speed);
    }

    @Override
    public void execute() {
        intakeSpin.setIntakeSpeed(speed);
    }
    
    @Override
    public void end(boolean interrupted) {
        intakeSpin.stopIntake();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
