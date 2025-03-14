package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkFlex;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Encoder;


/*
 * Might switch this to the code in the 
 * elevator subsystem but need to try it first
 */

public class ElevatorSubsystem extends SubsystemBase {
    //Set up the motors need in the subsystem
    private SparkFlex elevator = new SparkFlex(10, SparkFlex.MotorType.kBrushless);
    private SparkFlex intakeOn = new SparkFlex(11, SparkFlex.MotorType.kBrushless);
    private SparkFlex intakeRotate = new SparkFlex(12, SparkFlex.MotorType.kBrushless);

    //Get the encoder form the motor
    private final RelativeEncoder elevatorEncoder = elevator.getEncoder();
    private final RelativeEncoder intakeOnEncoder = intakeOn.getEncoder();
    private final RelativeEncoder intakeRotateEncoder = intakeRotate.getEncoder();
    private final Encoder throughBore = new Encoder(0, 1);

    private double targetPosition = 0;

    private double currentPosition;
    private double elevatorCurrentPosition;

    private final double offset = 10;


    public ElevatorSubsystem(){
        //Makes it so no movement happens on startup
        targetPosition = throughBore.get();
        elevator.setInverted(true);
        intakeOn.setInverted(false);
        intakeRotate.setInverted(false);
    }

    @Override
    public void periodic(){
        //Put the encoder values on the smartdashboard
        currentPosition = throughBore.get();
        elevatorCurrentPosition = elevatorEncoder.getPosition();
        SmartDashboard.putNumber("Elevator Encoder", elevatorCurrentPosition);
        SmartDashboard.putNumber("Through Bore Encoder", currentPosition);
        SmartDashboard.putNumber("Elevator Target Position", targetPosition);
        SmartDashboard.putNumber("Through Bore", throughBore.get());
        SmartDashboard.putNumber("intakeOn Encoder", intakeOnEncoder.getPosition());
        SmartDashboard.putNumber("intakeRotate Encoder", intakeRotateEncoder.getPosition());
    }

    public void setElevatorSpeed(double speed){
        //elevator.set(speed);
        //intakeOn.set(speed);
        //intakeRotate.set(speed);
    }

    public void goToPosition(double speed, double desiredPos, double rotatePos, double onPos){
        double currentPos = throughBore.get();
        if (Math.abs(currentPos - desiredPos) > offset) { // Add a tolerance
            if (desiredPos > currentPos) {
                elevator.set(speed);
            } else {
                elevator.set(-speed);
            }
        } else {
            elevator.set(0); // Stop the motor once position is reached
            intakeRotate(speed, rotatePos, onPos);
        }
    }

    
    public void intakeRotate(double speed, double rotatePos, double onPos){
        speed = 0.4;
        double currentPos = intakeRotateEncoder.getPosition();
        if (Math.abs(currentPos - rotatePos) > 1) { 
            if (rotatePos > currentPos) {
                intakeRotate.set(speed);
            } else {
                intakeRotate.set(-speed);
            }
        } else {
            intakeRotate.set(0);
            intakeOn(speed, onPos);
        }
    }

    public void intakeOn(double speed, double onPos){
        speed = 0.5;
        double currentPos = intakeOnEncoder.getPosition();
        if (Math.abs(currentPos - onPos) > 10) { 
            if (onPos > currentPos) {
                intakeOn.set(speed);
            } else {
                intakeOn.set(-speed);
            }
        } else {
            intakeOn.set(0);
        }
    }
    

    /*
    *Stops the Motor from moving
    *hope we don't have to use this but it is here just in case
    */
    public void stopElevator(){
        elevator.set(0);
        intakeOn.set(0);
        intakeRotate.set(0);
    }

}
