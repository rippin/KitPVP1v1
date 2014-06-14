package rippin.bullyscraft.com.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import rippin.bullyscraft.com.KitPVP1v1;

public abstract class CommandAbstract {

	protected KitPVP1v1 plugin;

	public CommandAbstract(KitPVP1v1 plugin) {
		this.plugin = plugin;

	}

	public abstract void executeCommand(CommandSender sender, Command cmd,
			String[] args);



}
