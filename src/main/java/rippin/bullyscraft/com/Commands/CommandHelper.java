package rippin.bullyscraft.com.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHelper {
	
	private CommandSender sender;
	private Command cmd;
	
	public CommandHelper(CommandSender sender, Command cmd){
		this.sender = sender;
		this.cmd = cmd;
	}

	public void noPermission(){
		sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
	}

	public void noConsole(){
		sender.sendMessage(ChatColor.RED + "This command can only be run in-game!");
	}

	public void wrongArguments(){
		sender.sendMessage(ChatColor.RED + "Unknown subcommand! Usage: " + cmd.getUsage());	
	}

	public void unknownCommand(){
		sender.sendMessage(ChatColor.RED + "Unknown command! Use /help for the commands!");
	}
    public void helpMessage(){
        sender.sendMessage(cmd.getUsage());
    }
}
