package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.CameraSubsystem;

public class SwitchCameraCommand extends InstantCommand {

    public SwitchCameraCommand(CameraSubsystem cameraSubsystem){
        super(cameraSubsystem::switchCamera, cameraSubsystem);
    }
}
