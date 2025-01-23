package frc.robot.Command;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.DriveSubsystem;

public class ResetEncodersCommand extends InstantCommand {
    public ResetEncodersCommand(DriveSubsystem driveSubsystem) {
        super(driveSubsystem::resetEncoders, driveSubsystem);
    }
}