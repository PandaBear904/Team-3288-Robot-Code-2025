package frc.robot.subsystems;

//Use this import a Spark Max Motor Controller
//import com.revrobotics.spark.SparkMax;
//Use this import for a Spark Flex Motor Controller
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TestSubsystem extends SubsystemBase {
    //Sets up motor
    private final SparkFlex test = new SparkFlex(10, MotorType.kBrushless);

    private final RelativeEncoder testEncoder = test.getEncoder();
    private double offset = 0.5;
    private double testPosition;
    private double cPos;
    private double dPos;
    private int pos;

    //For Spark Max NEO 
    //private final SparkMax NAME_HERE = new SparkMax(1, MotorType.kBrushless);
    public TestSubsystem(){
        testPosition = testEncoder.getPosition();
        test.setInverted(true);
    }
    @Override
    public void periodic(){
        testPosition = testEncoder.getPosition();
        SmartDashboard.putNumber("Test Encoder", testPosition);
        SmartDashboard.putNumber("Position", pos);
    }

    public void setTestSpeed(double speed){
        test.set(speed);
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

    public void stopTest(){
        test.set(0);
    }
}
