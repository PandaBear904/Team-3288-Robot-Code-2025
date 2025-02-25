package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/*
 * I what to get rid of this subsystem by
 * adding it in to the elevator subsystem
 */

public class IntakeSpin extends SubsystemBase {
    SparkFlex intakeMotor = new SparkFlex(1, MotorType.kBrushless);

    public IntakeSpin() {}

    public void setIntakeSpeed(double speed) {
        intakeMotor.set(speed);
    }

    public void stopIntake() {
        intakeMotor.set(0);
    }
    
}
