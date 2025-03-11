package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.revrobotics.spark.SparkMax;

public class ClimbSusbsystem extends SubsystemBase {
    private SparkMax climb = new SparkMax(46, SparkMax.MotorType.kBrushed);

    private double sec = 0;

    public ClimbSusbsystem(){
        climb.setInverted(false);
        SmartDashboard.putNumber("AAAAAAAAAAAAAAA", sec);
    }

    public void climbOn(double speed){
        climb.set(speed);
        if (speed > 0){
            sec = sec +0.001;
        }
    }
    
    public void stopClimb(){
        climb.set(0);
        sec = 0;
    }
}
