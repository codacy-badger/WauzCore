package eu.wauz.wauzcore.commands.administrative;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.wauz.wauzcore.commands.execution.WauzCommand;
import eu.wauz.wauzcore.commands.execution.WauzCommandExecutor;
import eu.wauz.wauzcore.system.WauzDebugger;

/**
 * A command, that can be executed by a player with fitting permissions.</br>
 * - Description: <b>Get Weapon with Skillgem</b></br>
 * - Usage: <b>/wzSkill.weapon [skillname]</b></br>
 * - Permission: <b>wauz.system</b>
 * 
 * @author Wauzmons
 * 
 * @see WauzCommand
 * @see WauzCommandExecutor
 */
public class CmdWzSkillWeapon implements WauzCommand {

	/**
	 * @return The id of the command.
	 */
	@Override
	public String getCommandId() {
		return "wzSkill.weapon";
	}

	/**
	 * Executes the command for given sender with arguments.
	 * 
	 * @param sender The sender of the command.
	 * @param args The arguments of the command.
	 * 
	 * @return If the command had correct syntax.
	 */
	@Override
	public boolean executeCommand(CommandSender sender, String[] args) {
		return WauzDebugger.getSkillgemWeapon((Player) sender, args[0].replace("_", " "));
	}

}
