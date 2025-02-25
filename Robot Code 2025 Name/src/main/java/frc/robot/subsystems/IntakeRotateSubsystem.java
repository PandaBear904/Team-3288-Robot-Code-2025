package frc.robot.subsystems;


/*
 * I what to get rid of this subsystem by
 * adding it in to the elevator subsystem
 */

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class IntakeRotateSubsystem extends SubsystemBase {
    private final SparkMax intakeRotate = new SparkMax(11, MotorType.kBrushless);
    private final RelativeEncoder intakeRotatePos = intakeRotate.getEncoder();
    private double rotatePos;

    public IntakeRotateSubsystem() {
        rotatePos = intakeRotatePos.getPosition();
        intakeRotate.setInverted(true);
    }

    public void intakeToPos(double desiredPos, double speed){
        double currentPos = intakeRotatePos.getPosition();

        if (Math.abs(currentPos - desiredPos) > 0.9){
            if(desiredPos > currentPos){
                intakeRotate.set(speed);
            } else {
                intakeRotate.set(-speed);
            }
        } else {
            intakeRotate.set(0);
        }
    }

    @Override
    public void periodic(){
        rotatePos = intakeRotatePos.getPosition();
        SmartDashboard.putNumber("Intake Pos", rotatePos);
    }
    
    public void setRotateSpeed(double speed){
        intakeRotate.set(speed);
    }

    public void stopRotate(){
        intakeRotate.set(0);
    }
}
