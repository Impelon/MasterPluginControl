package de.Impelon.masterplugincontrol;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MasterControlExecutor implements CommandExecutor {

	private MasterControlMain main;

	public MasterControlExecutor(MasterControlMain main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {

		if (args.length == 0)
			return false;

		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
			if (!p.hasPermission("mpc.*")) {
				p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.general.noPerm"));
				return true;
			}
		}

		if (args[0].equalsIgnoreCase("info")) {
			if (p != null) {
				String version = ChatColor.WHITE + main.getDescription().getVersion();
				String author = ChatColor.WHITE + main.getDescription().getAuthors().get(0);
				String website = ChatColor.WHITE + main.getDescription().getWebsite();
				p.sendMessage(main.getPLName());
				p.sendMessage(ChatColor.ITALIC + " version: " + version);
				p.sendMessage(ChatColor.ITALIC + " by: " + author);
				p.sendMessage(ChatColor.ITALIC + " website: " + website);
			} else
				main.getLogger().info("ver: " + main.getDescription().getVersion() + " by: " + main.getDescription().getAuthors().get(0));
			return true;
		}

		else if (args[0].equalsIgnoreCase("help")) {
			if (p != null) {
				p.sendMessage(main.getPLName() + ChatColor.BOLD + "MasterPluginControl | Help");
				p.sendMessage(ChatColor.DARK_GRAY + "/mpc help" + ChatColor.WHITE + " - " + ChatColor.ITALIC
						+ main.getConfig().getString("mpc.messages.commands.help.help"));
				p.sendMessage(ChatColor.DARK_GRAY + "/mpc info" + ChatColor.WHITE + " - " + ChatColor.ITALIC
						+ main.getConfig().getString("mpc.messages.commands.help.info"));
				p.sendMessage(ChatColor.DARK_GRAY + "/mpc reload" + ChatColor.WHITE + " - " + ChatColor.ITALIC
						+ main.getConfig().getString("mpc.messages.commands.help.reload"));
				p.sendMessage(ChatColor.DARK_GRAY + "/mpc list" + ChatColor.WHITE + " - " + ChatColor.ITALIC
						+ main.getConfig().getString("mpc.messages.commands.help.list"));
				p.sendMessage(ChatColor.DARK_GRAY + "/mpc stop <plugin>" + ChatColor.WHITE + " - " + ChatColor.ITALIC
						+ main.getConfig().getString("mpc.messages.commands.help.stop"));
				p.sendMessage(ChatColor.DARK_GRAY + "/mpc start <plugin>" + ChatColor.WHITE + " - " + ChatColor.ITALIC
						+ main.getConfig().getString("mpc.messages.commands.help.start"));
				p.sendMessage(ChatColor.DARK_GRAY + "/mpc load <plugin>" + ChatColor.WHITE + " - " + ChatColor.ITALIC
						+ main.getConfig().getString("mpc.messages.commands.help.load"));
				p.sendMessage(ChatColor.DARK_GRAY + "/mpc restart <plugin> [delay]" + ChatColor.WHITE + " - " + ChatColor.ITALIC
						+ main.getConfig().getString("mpc.messages.commands.help.restart"));
			} else {
				main.getLogger().info("MasterPluginControl | Help");
				main.getLogger().info("/mpc help" + " - " + main.getConfig().getString("mpc.messages.commands.help.help"));
				main.getLogger().info("/mpc info" + " - " + main.getConfig().getString("mpc.messages.commands.help.info"));
				main.getLogger().info("/mpc reload" + " - " + main.getConfig().getString("mpc.messages.commands.help.reload"));
				main.getLogger().info("/mpc list" + " - " + main.getConfig().getString("mpc.messages.commands.help.list"));
				main.getLogger().info("/mpc stop <plugin>" + " - " + main.getConfig().getString("mpc.messages.commands.help.stop"));
				main.getLogger().info("/mpc start <plugin>" + " - " + main.getConfig().getString("mpc.messages.commands.help.start"));
				main.getLogger().info("/mpc load <plugin>" + " - " + main.getConfig().getString("mpc.messages.commands.help.load"));
				main.getLogger().info("/mpc restart <plugin> [delay]" + " - " + main.getConfig().getString("mpc.messages.commands.help.restart"));
			}
			return true;
		}

		else if (args[0].equalsIgnoreCase("reload")) {
			if (p != null) {
				main.loadConfig();
				p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.general.reloadSuccessful"));
			} else
				main.loadConfig();
			return true;
		}

		else if (args[0].equalsIgnoreCase("list")) {
			if (p != null) {
				p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.general.plugins"));
				p.sendMessage(main.getPlugins().toString());
			} else {
				main.getLogger().info(main.getConfig().getString("mpc.messages.general.plugins"));
				main.getLogger().info(main.getPlugins().toString());
			}
			return true;
		}

		else if (args[0].equalsIgnoreCase("stop")) {
			if (p != null) {
				if (args.length == 1) {
					p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.general.plugins"));
					p.sendMessage(main.getPlugins().toString());
				} else {
					if (main.getServer().getPluginManager().getPlugin(args[1]) == null)
						p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.commands.stop.noPlugin") + "'" + args[1] + "'");
					else {
						final Plugin pl = main.getServer().getPluginManager().getPlugin(args[1]);
						main.getServer().getPluginManager().disablePlugin(pl);
						p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.commands.stop.sucessful") + "'" + args[1] + "'");
					}
				}
			} else {
				if (args.length == 1) {
					main.getLogger().info(main.getConfig().getString("mpc.messages.general.plugins"));
					main.getLogger().info(main.getPlugins().toString());
				} else {
					if (main.getServer().getPluginManager().getPlugin(args[1]) == null)
						main.getLogger().info(main.getConfig().getString("mpc.messages.general.noPlugin") + "'" + args[1] + "'");
					else {
						final Plugin pl = main.getServer().getPluginManager().getPlugin(args[1]);
						main.getServer().getPluginManager().disablePlugin(pl);
						main.getLogger().info(main.getConfig().getString("mpc.messages.commands.stop.sucessful") + "'" + args[1] + "'");
					}
				}
			}
			return true;
		}

		else if (args[0].equalsIgnoreCase("start")) {
			if (p != null) {
				if (args.length == 1) {
					p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.general.plugins"));
					p.sendMessage(main.getPlugins().toString());
				} else {
					if (main.getServer().getPluginManager().getPlugin(args[1]) == null)
						p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.general.noPlugin") + "'" + args[1] + "'");
					else {
						final Plugin pl = main.getServer().getPluginManager().getPlugin(args[1]);
						main.getServer().getPluginManager().enablePlugin(pl);
						p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.commands.start.sucessful") + "'" + args[1] + "'");
					}
				}
			} else {
				if (args.length == 1) {
					main.getLogger().info(main.getConfig().getString("mpc.messages.general.plugins"));
					main.getLogger().info(main.getPlugins().toString());
				} else {
					if (main.getServer().getPluginManager().getPlugin(args[1]) == null)
						main.getLogger().info(main.getConfig().getString("mpc.messages.general.noPlugin") + "'" + args[1] + "'");
					else {
						final Plugin pl = main.getServer().getPluginManager().getPlugin(args[1]);
						main.getServer().getPluginManager().enablePlugin(pl);
						main.getLogger().info(main.getConfig().getString("mpc.messages.commands.start.sucessful") + "'" + args[1] + "'");
					}
				}
			}
			return true;
		}

		else if (args[0].equalsIgnoreCase("load")) {
			if (p != null) {
				if (args.length == 1)
					p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.commands.load.noArgs"));
				else {
					File pl = new File("plugins/" + args[1] + ".jar");
					if (main.getServer().getPluginManager().getPlugin(args[1]) == null)
						try {
							main.getServer().getPluginManager().loadPlugin(pl);
							main.getServer().getPluginManager().enablePlugin(main.getServer().getPluginManager().getPlugin(args[1]));
							p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.commands.start.sucessful") + "'" + args[1] + "'");
						} catch (Exception e) {
							p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.general.noPlugin") + "'" + args[1] + ".jar'");
							main.getLogger().warning(e.toString());
						}
					else
						p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.commands.load.loaded"));
				}
			} else {
				if (args.length == 1)
					main.getLogger().info(main.getConfig().getString("mpc.messages.commands.load.noArgs"));
				else {
					File pl = new File("plugins/" + args[1] + ".jar");
					if (main.getServer().getPluginManager().getPlugin(args[1]) == null)
						try {
							main.getServer().getPluginManager().loadPlugin(pl);
							main.getServer().getPluginManager().enablePlugin(main.getServer().getPluginManager().getPlugin(args[1]));
						} catch (Exception e) {
							main.getLogger().warning(e.toString());
						}
					else
						main.getLogger().info(main.getPLName() + main.getConfig().getString("mpc.messages.commands.load.loaded"));
				}
			}
			return true;
		}

		else if (args[0].equalsIgnoreCase("restart")) {
			int delay = 20;
			if (args.length > 2) {
				try {
					delay = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					sender.sendMessage("'" + args[2] + "'" + main.getConfig().getString("mpc.messages.commands.restart.noNumber"));
				}
			}
			if (p != null) {
				if (args.length == 1) {
					p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.general.plugins"));
					p.sendMessage(main.getPlugins().toString());
				} else {
					if (main.getServer().getPluginManager().getPlugin(args[1]) == null) {
						p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.general.noPlugin") + "'" + args[1] + "'");
					} else {
						final Plugin pl = main.getServer().getPluginManager().getPlugin(args[1]);
						main.getServer().getPluginManager().disablePlugin(pl);
						new BukkitRunnable() {
							@Override
							public void run() {
								final Player p = (Player) sender;
								main.getServer().getPluginManager().enablePlugin(pl);
								p.sendMessage(main.getPLName() + main.getConfig().getString("mpc.messages.commands.start.sucessful") + "'" + args[1] + "'");
							}
						}.runTaskLater(main, delay);
					}
				}
			} else {
				if (args.length == 1) {
					main.getLogger().info(main.getConfig().getString("mpc.messages.general.plugins"));
					main.getLogger().info(main.getPlugins().toString());
				} else {
					if (main.getServer().getPluginManager().getPlugin(args[1]) == null)
						main.getLogger().info(main.getConfig().getString("mpc.messages.general.noPlugin") + "'" + args[1] + "'");
					else {
						final Plugin pl = main.getServer().getPluginManager().getPlugin(args[1]);
						main.getServer().getPluginManager().disablePlugin(pl);
						new BukkitRunnable() {
							@Override
							public void run() {
								main.getServer().getPluginManager().enablePlugin(pl);
								main.getLogger().info(main.getConfig().getString("mpc.messages.commands.start.sucessful") + "'" + args[1] + "'");
							}
						}.runTaskLater(main, delay);
					}
				}
			}
			return true;
		}

		return false;
	}
}
