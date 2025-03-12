package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkMax;

public class ClimbSusbsystem extends SubsystemBase {
    private SparkMax climb = new SparkMax(13, SparkMax.MotorType.kBrushed);

    private double sec = 0;

    public ClimbSusbsystem(){
        climb.setInverted(false);
    }

    public void climbOn(double speed){
        climb.set(speed);
    }
    
    public void stopClimb(){
        climb.set(0);
    }
}
