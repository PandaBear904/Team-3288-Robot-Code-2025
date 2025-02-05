package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkFlex;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ElevatorSubsystem extends SubsystemBase {
    //Set up the motors need in the subsystem
    private SparkFlex elevatorMotor = new SparkFlex(60, SparkFlex.MotorType.kBrushless); // Might need to change later

    //Get the encoder form the motor
    private final RelativeEncoder elevatorEncoder = elevatorMotor.getEncoder();

    //Sets up the tartget position for the elevator
    private double targetPosition = 0;

    // This is for the distance on that is exspected to be off
    private final double offset = 0.05; 

    //Speed of the motor
    private final double forwardSpeed = 0.5;
    private final double reverseSpeed = -0.5;

    public ElevatorSubsystem(){
        //Makes it so no movement happens on startup
        targetPosition = elevatorEncoder.getPosition();
    }

    /**
     * Sets a new target position for the elevator
     * 
     * @param position The desired position (in encoder units) Need to find out what they can't think of it right now
    */
    public void setTargetPosition(double position){
        targetPosition = position;
    }

    @Override
    public void periodic(){
        // Read the current position of the elevator
        double currentPos = elevatorEncoder.getPosition();
        double error = targetPosition - currentPos;

        //if the error is within the offset, stop the motor
        if(Math.abs(error) < offset){
            elevatorMotor.set(0);
        }
        //If the current position is less that the target, got up
        else if (error > 0){
            elevatorMotor.set(forwardSpeed);
        }
        //If not go down
        else{
            elevatorMotor.set(reverseSpeed);
        }
    }

    /*Stops the Motor from moving
    hope we don't have to use this but it is here just in case
    */
    public void stopElevator(){
        elevatorMotor.set(0);
    }

}
