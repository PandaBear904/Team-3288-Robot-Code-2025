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
    private final SparkFlex test = new SparkFlex(1, MotorType.kBrushless);
    private final SparkFlex test2 = new SparkFlex(10, MotorType.kBrushless);

    private final RelativeEncoder testEncoder = test.getEncoder();
    private final double testPosition = testEncoder.getPosition();
    //For Spark Max Motor Controller
    //private final SparkMax NAME_HERE = new SparkMax(1, MotorType.kBrushless);
    public TestSubsystem(){
        SmartDashboard.putNumber("Test Encoder", testPosition);
    }

    public void setTestSpeed(double speed){
        test.set(speed);
        if(testPosition == 10){
            test2.set(speed);
        }
    }

    public void stopTest(){
        test.set(0);
        test2.set(0);
    }
}
