package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkFlex;

public class ClimbSusbsystem extends SubsystemBase {
    private SparkFlex climb = new SparkFlex(10, SparkFlex.MotorType.kBrushless);
    private DigitalInput limitSwitch = new DigitalInput(9   );

    public ClimbSusbsystem(){
        climb.setInverted(false);
        SmartDashboard.putData(limitSwitch);
    }

    public void climbOn(double speed){
        if (speed > 0){
            if (limitSwitch.get()){
                climb.set(0);
            } else{
                climb.set(speed);
            }
        }else if(speed < 0){
            climb.set(speed);
        }
    }
    
    public void stopClimb(){
        climb.set(0);
    }
}
