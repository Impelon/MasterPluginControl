package de.impelon.masterplugincontrol;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MasterControlExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {

		if (args.length == 0)
			return false;

		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
			if (!p.hasPermission("mpc.*")) {
				MasterControlMain.sendMessage(sender, MasterControlMain.main.getPLName() + MasterControlMain.main.getConfig().getString("mpc.messages.general.noPerm"));
				return true;
			}
		}

		if (args[0].equalsIgnoreCase("info")) {
			if (p != null) {
				String version = ChatColor.WHITE + MasterControlMain.main.getDescription().getVersion();
				String author = ChatColor.WHITE + MasterControlMain.main.getDescription().getAuthors().get(0);
				String website = ChatColor.WHITE + MasterControlMain.main.getDescription().getWebsite();
				MasterControlMain.sendMessage(sender, MasterControlMain.main.getPLName());
				MasterControlMain.sendMessage(sender, ChatColor.ITALIC + " version: " + version);
				MasterControlMain.sendMessage(sender, ChatColor.ITALIC + " by: " + author);
				MasterControlMain.sendMessage(sender, ChatColor.ITALIC + " website: " + website);
			} else
				MasterControlMain.sendMessage(sender, "ver: " + MasterControlMain.main.getDescription().getVersion() + " by: " + MasterControlMain.main.getDescription().getAuthors().get(0));
			return true;
		}

		else if (args[0].equalsIgnoreCase("help")) {
			int page = 0;
			try {
				page = args.length > 1 ? Integer.valueOf(args[1]) % (MasterControlMain.main.getHelp(-1).length / 8) : 0;
			} catch (NumberFormatException e) {
				MasterControlMain.sendMessageWithPrefix(sender, "'" + args[1] + "'" + MasterControlMain.main.getConfig().getString("mpc.messages.commands.restart.noNumber"));
			}
			if (p != null)
				MasterControlMain.sendMessageWithPrefix(sender, ChatColor.BOLD + "MasterPluginControl | Help | Page " + page);
			else
				MasterControlMain.sendMessage(sender, "MasterPluginControl | Help | Page " + page);
			for (String line : MasterControlMain.main.getHelp(page))
				if (line != null)
					MasterControlMain.sendMessage(sender, line);
			return true;
		}

		else if (args[0].equalsIgnoreCase("reload")) {
			if (p != null) {
				MasterControlMain.main.loadConfig();
				MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.general.reloadSuccessful"));
			} else
				MasterControlMain.main.loadConfig();
			return true;
		}

		else if (args[0].equalsIgnoreCase("list")) {
			MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.general.plugins"));
			MasterControlMain.sendMessage(sender, MasterControlMain.main.getPlugins().toString());
			return true;
		}

		else if (args[0].equalsIgnoreCase("stop")) {
			if (args.length == 1) {
				MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.general.plugins"));
				MasterControlMain.sendMessage(sender, MasterControlMain.main.getPlugins().toString());
			} else {
				if (MasterControlMain.main.getServer().getPluginManager().getPlugin(args[1]) == null)
					MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.commands.stop.noPlugin") + "'" + args[1] + "'");
				else {
					final Plugin pl = MasterControlMain.main.getServer().getPluginManager().getPlugin(args[1]);
					MasterControlMain.main.getServer().getPluginManager().disablePlugin(pl);
					MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.commands.stop.sucessful") + "'" + args[1] + "'");
				}
			}
			return true;
		}

		else if (args[0].equalsIgnoreCase("start")) {
			if (args.length == 1) {
				MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.general.plugins"));
				MasterControlMain.sendMessage(sender, MasterControlMain.main.getPlugins().toString());
			} else {
				if (MasterControlMain.main.getServer().getPluginManager().getPlugin(args[1]) == null)
					MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.general.noPlugin") + "'" + args[1] + "'");
				else {
					final Plugin pl = MasterControlMain.main.getServer().getPluginManager().getPlugin(args[1]);
					MasterControlMain.main.getServer().getPluginManager().enablePlugin(pl);
					MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.commands.start.sucessful") + "'" + args[1] + "'");
				}
			}
			return true;
		}

		else if (args[0].equalsIgnoreCase("load")) {
			if (args.length == 1)
				MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.commands.load.noArgs"));
			else {
				File file = new File("plugins/" + args[1] + ".jar");
				if (MasterControlMain.main.getServer().getPluginManager().getPlugin(args[1]) == null)
					try {
						Plugin pl = MasterControlMain.main.getServer().getPluginManager().loadPlugin(file);
						MasterControlMain.main.getServer().getPluginManager().enablePlugin(pl);
						MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.commands.start.sucessful") + "'" + args[1] + "'");
					} catch (Exception e) {
						MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.general.noPlugin") + "'" + args[1] + ".jar'");
						MasterControlMain.main.getLogger().warning(e.toString());
					}
				else
					MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.commands.load.loaded"));
			}
			return true;
		}

		else if (args[0].equalsIgnoreCase("restart")) {
			int delay = 20;
			if (args.length > 2)
				try {
					delay = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					MasterControlMain.sendMessageWithPrefix(sender, "'" + args[2] + "'" + MasterControlMain.main.getConfig().getString("mpc.messages.commands.restart.noNumber"));
				}
			if (args.length == 1) {
				MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.general.plugins"));
				MasterControlMain.sendMessage(sender, MasterControlMain.main.getPlugins().toString());
			} else {
				if (MasterControlMain.main.getServer().getPluginManager().getPlugin(args[1]) == null)
					MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.general.noPlugin") + "'" + args[1] + "'");
				else {
					final Plugin pl = MasterControlMain.main.getServer().getPluginManager().getPlugin(args[1]);
					MasterControlMain.main.getServer().getPluginManager().disablePlugin(pl);
					new BukkitRunnable() {
						@Override
						public void run() {
							MasterControlMain.main.getServer().getPluginManager().enablePlugin(pl);
							MasterControlMain.sendMessageWithPrefix(sender, MasterControlMain.main.getConfig().getString("mpc.messages.commands.start.sucessful") + "'" + args[1] + "'");
						}
					}.runTaskLater(MasterControlMain.main, delay);
				}
			}
			return true;
		}

		return false;
	}
}
