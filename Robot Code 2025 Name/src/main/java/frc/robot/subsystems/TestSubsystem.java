package frc.robot.subsystems;
/*
 * THIS IS THE TEST SUBSYSTEM
 * 
 * I have been using this to test new part of the bot
 * Right now it is set up to hopefully run the elevator
 * and the intake rotation
 * 
 * Might need to the intake drop off
 */


//All of the imports
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TestSubsystem extends SubsystemBase {
    //Sets up motors
    private final SparkFlex test = new SparkFlex(10, MotorType.kBrushless);
    private final SparkMax test2 = new SparkMax(11, MotorType.kBrushless);

    //Encoders set up
    private final RelativeEncoder testEncoder = test.getEncoder();
    private final RelativeEncoder test2Encoder = test2.getEncoder();
    private double testPosition;
    private double test2Position;

    //Values
    private double offset = 0.5;
    private double cPos;
    private int pos;

    //For Spark Max NEO 
    //private final SparkMax NAME_HERE = new SparkMax(1, MotorType.kBrushless);
    public TestSubsystem(){
        testPosition = testEncoder.getPosition();
        test2Position = test2Encoder.getPosition();
        test.setInverted(false);
        test2.setInverted(false);
    }
    @Override
    public void periodic(){
        testPosition = testEncoder.getPosition();
        test2Position = test2Encoder.getPosition();
        SmartDashboard.putNumber("Test Encoder", testPosition);
        SmartDashboard.putNumber("Test2 Encoder", test2Position);
        SmartDashboard.putNumber("Position", pos);
    }

    public void setTestSpeed(double speed){
        test.set(speed);
        test2.set(speed);
    }

    public void goToPosition(double speed, double desiredPos){
        double currentPos = testEncoder.getPosition();
        cPos = currentPos;
        if (Math.abs(currentPos - desiredPos) > offset) { // Add a tolerance
            if (desiredPos > currentPos) {
                test.set(speed);
            } else {
                test.set(-speed);
            }
        } else {
            test.set(0); // Stop the motor once position is reached
            pos();
            IntakeOn(speed);
        }

    }

    public void pos(){
        if (Math.abs(cPos - 250) <= offset) {
            pos = 1;
        } else if (Math.abs(cPos - 150) <= offset){
            pos = 2;
        } else {
            pos = 3;
        }
    }

    private void IntakeOn(double speed){
        double currentPos = test2Encoder.getPosition();
        double desiredPos = 1.5;
        cPos = currentPos;
        if (Math.abs(currentPos - desiredPos) > offset) { 
            if (desiredPos > currentPos) {
                test2.set(speed);
            } else {
                test2.set(-speed);
            }
        } else {
            test2.set(0); // Stop the motor once position is reached
            reset2Encoder();
        }
    }

    public void reset2Encoder(){
        double currentPos = test2Position;

        if (Math.abs(currentPos - 0) > offset){
            if (0 > currentPos){
                test2.set(0.3);
            } else {
                test2.set(-0.3);
            }
        } else {
            test.set(0);
        }
    }

    public void stopTest(){
        test.set(0);
        test2.set(0);
    }
}
