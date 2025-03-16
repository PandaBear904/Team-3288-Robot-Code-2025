package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkFlex;

public class ClimbSusbsystem extends SubsystemBase {
    private SparkFlex climb = new SparkFlex(10, SparkFlex.MotorType.kBrushless);

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
