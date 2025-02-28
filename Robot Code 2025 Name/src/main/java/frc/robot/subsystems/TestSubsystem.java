package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


/*
 * Might switch this to the code in the 
 * test subsystem but need to try it first
 */

public class TestSubsystem extends SubsystemBase {
    //Set up the motors need in the subsystem
    private SparkFlex test = new SparkFlex(10, SparkFlex.MotorType.kBrushless); // Might need to change later
    private SparkMax test2 = new SparkMax(11, SparkMax.MotorType.kBrushless);
    private SparkFlex test3 = new SparkFlex(12, SparkFlex.MotorType.kBrushless);

    //Get the encoder form the motor
    private final RelativeEncoder testEncoder = test.getEncoder();
    private final RelativeEncoder test2Encoder = test2.getEncoder();
    private final RelativeEncoder test3Encoder = test3.getEncoder();

    private double targetPosition = 0;

    private double currentPosition;
    private double cPos;
    private int pos;

    private final double offset = 0.5; 


    public TestSubsystem(){
        //Makes it so no movement happens on startup
        targetPosition = testEncoder.getPosition();
        test.setInverted(true);
        test2.setInverted(false);

    }

    @Override
    public void periodic(){
        //Put the encoder values on the smartdashboard
        currentPosition = testEncoder.getPosition();
        SmartDashboard.putNumber("Test Encoder", currentPosition);
        SmartDashboard.putNumber("Test Target Position", targetPosition);
        SmartDashboard.putNumber("Test Position", pos);
        SmartDashboard.putNumber("Test2 Encoder", test2Encoder.getPosition());
        SmartDashboard.putNumber("Test3 Encoder", test3Encoder.getPosition());
    }

    public void setTestSpeed(double speed){
        //test.set(speed);
        //test2.set(speed);
        //test3.set(speed);
    }

    public void goToPosition(double speed, double desiredPos, double rotatePos, double onPos){
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
            intakeRotate(speed, rotatePos, onPos);
        }
    }

    public void pos(){
        //Finds what position the elevator is in
        if (Math.abs(cPos - 250) <= offset) {
            pos = 1;
        } else if (Math.abs(cPos - 150) <= offset){
            pos = 2;
        } else {
            pos = 3;
        }
    }

    public void intakeRotate(double speed, double rotatePos, double onPos){
        double currentPos = test3Encoder.getPosition();
        cPos = currentPos;
        if (Math.abs(currentPos - rotatePos) > offset) { // Add a tolerance
            if (rotatePos > currentPos) {
                test3.set(speed);
            } else {
                test3.set(-speed);
            }
        } else {
            test3.set(0);
            intakeOn(speed, onPos);
        }
    }

    public void intakeOn(double speed, double onPos){
        double currentPos = test2Encoder.getPosition();
        cPos = currentPos;
        if (Math.abs(currentPos - onPos) > offset) { // Add a tolerance
            if (onPos > currentPos) {
                test2.set(speed);
            } else {
                test2.set(-speed);
            }
        } else {
            test2.set(0);
        }
    }



    /*Stops the Motor from moving
    hope we don't have to use this but it is here just in case
    */
    public void stopTest(){
        test.set(0);
    }

}
