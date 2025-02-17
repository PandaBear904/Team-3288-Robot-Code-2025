package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkFlex;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ElevatorSubsystem extends SubsystemBase {
    //Set up the motors need in the subsystem
    private SparkFlex elevatorMotor = new SparkFlex(10, SparkFlex.MotorType.kBrushless); // Might need to change later

    //Get the encoder form the motor
    private final RelativeEncoder elevatorEncoder = elevatorMotor.getEncoder();

    //Sets up the tartget position for the elevator
    private double targetPosition = 0;

    //Sets up the current position for the elevator
    private double currentPosition;
    private double cPos;

    //Sets up the position for the elevator
    private int pos;

    // This is for the distance on that is exspected to be off
    private final double offset = 0.05; 


    public ElevatorSubsystem(){
        //Makes it so no movement happens on startup
        targetPosition = elevatorEncoder.getPosition();
        elevatorMotor.setInverted(true);
    }

    @Override
    public void periodic(){
        //Put the encoder values on the smartdashboard
        currentPosition = elevatorEncoder.getPosition();
        SmartDashboard.putNumber("Elevator Encoder", currentPosition);
        SmartDashboard.putNumber("Elevator Target Position", targetPosition);
        SmartDashboard.putNumber("Elevator Position", pos);
    }

    public void setElevatorSpeed(double speed){
        //Sets the speed of the elevator
        elevatorMotor.set(speed);
    }

    public void goToPosition(double speed, double desiredPos){
        //Sets the speed of the elevator
        double currentPos = elevatorEncoder.getPosition();
        cPos = currentPos;
        if (Math.abs(currentPos - desiredPos) > offset) { // Add a tolerance
            if (desiredPos > currentPos) {
                elevatorMotor.set(speed);
            } else {
                elevatorMotor.set(-speed);
            }
        } else {
            elevatorMotor.set(0); // Stop the motor once position is reached
            pos();
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

    /*Stops the Motor from moving
    hope we don't have to use this but it is here just in case
    */
    public void stopElevator(){
        elevatorMotor.set(0);
    }

}
