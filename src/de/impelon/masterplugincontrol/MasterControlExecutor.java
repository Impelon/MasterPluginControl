package de.impelon.masterplugincontrol;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MasterControlExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {

		if (args.length == 0)
			return false;

		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
			if (!p.hasPermission("mpc.*")) {
				MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.general.noPerm"));
				return true;
			}
		}
		
		if (args[0].equalsIgnoreCase("info")) {
			String version = MasterControlMain.getInstance().getDescription().getVersion();
			String website = MasterControlMain.getInstance().getDescription().getWebsite();
			StringBuilder buffer = new StringBuilder();
			for (String author : MasterControlMain.getInstance().getDescription().getAuthors())
				buffer.append(author + ", ");
			String authors = buffer.substring(0, buffer.length() - 2);
			
			MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.commands.info")
					.replace("@version", version).replace("@authors", authors).replace("@website", website));
			return true;
		}

		else if (args[0].equalsIgnoreCase("help")) {
			int page = 1;
			int maxpage = (int) Math.ceil(MasterControlMain.getInstance().getHelp(-1).size() / (double) MasterControlMain.getInstance().getConfig().getInt("mpc.commands.help.linesperpage", 8));
			try {
				page = args.length > 1 ? (Integer.parseInt(args[1]) - 1) % maxpage + 1 : page;
			} catch (NumberFormatException ex) {
				MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.general.noNumber")
						.replace("@number", args[1]));
			}
			MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.commands.help.header")
					.replace("@plugin", MasterControlMain.getInstance().getName()).replace("@page", String.valueOf(page)).replace("@maxpage", String.valueOf(maxpage)));
			for (String line : MasterControlMain.getInstance().getHelp(page))
				if (line != null)
					MasterControlMain.getInstance().sendMessage(sender, line);
			return true;
		}

		else if (args[0].equalsIgnoreCase("reload")) {
			MasterControlMain.getInstance().loadConfig();
			if (sender instanceof Player)
				MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.general.reloadSuccessful"));
			return true;
		}

		else if (args[0].equalsIgnoreCase("list")) {
			MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.general.plugins"));
			MasterControlMain.getInstance().sendMessage(sender, MasterControlMain.getInstance().getPlugins().toString());
			return true;
		}

		else if (args[0].equalsIgnoreCase("stop")) {
			if (args.length == 1) {
				MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.general.plugins"));
				MasterControlMain.getInstance().sendMessage(sender, MasterControlMain.getInstance().getPlugins().toString());
			} else {
				final Plugin plugin = MasterControlMain.getInstance().getServer().getPluginManager().getPlugin(args[1]);
				if (plugin == null)
					MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.general.noPlugin")
							.replace("@plugin", "'" + args[1] + "'"));
				else {
					MasterControlMain.getInstance().getServer().getPluginManager().disablePlugin(plugin);
					MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.commands.stop.sucessful")
							.replace("@plugin", "'" + args[1] + "'"));
				}
			}
			return true;
		}

		else if (args[0].equalsIgnoreCase("start")) {
			if (args.length == 1) {
				MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.general.plugins"));
				MasterControlMain.getInstance().sendMessage(sender, MasterControlMain.getInstance().getPlugins().toString());
			} else {
				final Plugin plugin = MasterControlMain.getInstance().getServer().getPluginManager().getPlugin(args[1]);
				if (plugin == null)
					MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.general.noPlugin")
							.replace("@plugin", "'" + args[1] + "'"));
				else {
					MasterControlMain.getInstance().getServer().getPluginManager().enablePlugin(plugin);
					MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.commands.start.sucessful")
							.replace("@plugin", "'" + args[1] + "'"));
				}
			}
			return true;
		}

		else if (args[0].equalsIgnoreCase("load")) {
			if (args.length == 1)
				MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.commands.load.noArgs"));
			else {
				File file = new File((args.length > 2 ? (args[2].equalsIgnoreCase("-i") ? args[1] : "plugins/" + args[1] + ".jar") : "plugins/" + args[1] + ".jar"));
				if (MasterControlMain.getInstance().getServer().getPluginManager().getPlugin(args[1]) == null)
					try {
						final Plugin plugin = MasterControlMain.getInstance().getServer().getPluginManager().loadPlugin(file);
						MasterControlMain.getInstance().getServer().getPluginManager().enablePlugin(plugin);
						MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.commands.start.sucessful")
								.replace("@plugin", "'" + args[1] + "'"));
					} catch (Exception e) {
						MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.general.noPlugin")
								.replace("@plugin", "'" + file.getPath() + "'"));
						MasterControlMain.getInstance().getLogger().warning(e.toString());
					}
				else
					MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.commands.load.loaded"));
			}
			return true;
		}

		else if (args[0].equalsIgnoreCase("restart")) {
			long delay = 20;
			try {
				delay = args.length > 2 ? Long.parseLong(args[2]) : delay;
			} catch (NumberFormatException ex) {
				MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.general.noNumber")
						.replace("@number", "'" + args[2] + "'"));
			}
			if (args.length == 1) {
				MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.general.plugins"));
				MasterControlMain.getInstance().sendMessage(sender, MasterControlMain.getInstance().getPlugins().toString());
			} else {
				final Plugin plugin = MasterControlMain.getInstance().getServer().getPluginManager().getPlugin(args[1]);
				if (plugin == null)
					MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.general.noPlugin") + "'" + args[1] + "'");
				else
					MasterControlMain.getInstance().restartPlugin(sender, plugin, delay);
			}
			return true;
		}

		return false;
	}
}
