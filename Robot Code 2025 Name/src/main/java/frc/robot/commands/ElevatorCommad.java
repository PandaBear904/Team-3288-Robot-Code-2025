
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.ElevatorSubsystem;

public class ElevatorCommad extends InstantCommand {
    public ElevatorCommad(ElevatorSubsystem elevatorSubsystem, double position) {
        super(() -> elevatorSubsystem.setTargetPosition(position), elevatorSubsystem);
    }
    
}
