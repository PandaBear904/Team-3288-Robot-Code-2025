package frc.robot.subsystems;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSink;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CameraSubsystem extends SubsystemBase{
    private final UsbCamera camera1;
    private final UsbCamera camera2;
    private final VideoSink cameraServer;

    private boolean isCamera1Active = true;


    public CameraSubsystem() {
        camera1 = CameraServer.startAutomaticCapture(0);
        camera2 = CameraServer.startAutomaticCapture(1);
        camera1.setResolution(32, 32);
        camera2.setResolution(64, 64);
        camera1.setFPS(10);
        camera2.setFPS(15);
        cameraServer = CameraServer.getServer();
        cameraServer.setSource(camera1);
    } 

    public void switchCamera(){
        if (isCamera1Active) {
            cameraServer.setSource(camera2);
        } else {
            cameraServer.setSource(camera1);
        }
        isCamera1Active = !isCamera1Active;
    }
}
