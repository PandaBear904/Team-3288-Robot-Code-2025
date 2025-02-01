package frc.robot.subsystems;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CameraSubsystem extends SubsystemBase{
    public CameraSubsystem(){
        UsbCamera camera = CameraServer.startAutomaticCapture();
        camera.setResolution(320, 240);
        camera.setFPS(30);
    } 
}
