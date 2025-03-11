package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.ClimbSusbsystem;

public class ClimbCommand extends InstantCommand {
    private final ClimbSusbsystem climbSusbsystem;
    private final double speed;

    public ClimbCommand(ClimbSusbsystem climbSusbsystem, double speed){
        this.climbSusbsystem = climbSusbsystem;
        this.speed = speed;
        addRequirements(climbSusbsystem);
    }
    
    @Override
    public void execute(){
        climbSusbsystem.climbOn(speed);
    }

    @Override
    public void end(boolean interrupted){
        climbSusbsystem.stopClimb();
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
