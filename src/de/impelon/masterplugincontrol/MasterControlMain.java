package de.impelon.masterplugincontrol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class MasterControlMain extends JavaPlugin {
	
	private static MasterControlMain main;
	private MasterControlExecutor mpcexe;
	
	private String[] helppages;

	@Override
	public void onEnable() {
		main = this;
		loadConfig();
		mpcexe = new MasterControlExecutor();
		this.getCommand("mpc").setExecutor(mpcexe);
	}
	
	@Override
	public void onDisable() {
		main = null;
	}
	
	public static MasterControlMain getInstance() {
		return main;
	}

	public final String getPLName() {
		String name = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + ChatColor.MAGIC + "|" + ChatColor.BLACK + ChatColor.MAGIC + "|" + ChatColor.GRAY
				+ ChatColor.MAGIC + "|" + ChatColor.WHITE + ChatColor.BOLD + this.getDescription().getName() + ChatColor.GRAY + ChatColor.MAGIC + "|"
				+ ChatColor.BLACK + ChatColor.MAGIC + "|" + ChatColor.GRAY + ChatColor.MAGIC + "|" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;
		return name;
	}

	public void loadConfig() {
		reloadConfig();
		this.getConfig().options().header("MasterPluginControl | Plugin configuration");
		this.getConfig().options().copyDefaults(true);
		
		this.getConfig().addDefault("mpc.commands.help.linesperpage", 8);
		
		this.getConfig().addDefault("mpc.messages.commands.help.header", "&l@plugin | Help | Page @page");
		this.getConfig().addDefault("mpc.messages.commands.help.help", "&8/mpc help <page>&r - &odisplays this message");
		this.getConfig().addDefault("mpc.messages.commands.help.info", "&8/mpc info <page>&r - &odisplays information on MasterPluginControl itself");
		this.getConfig().addDefault("mpc.messages.commands.help.reload", "&8/mpc reload&r - &oreloads the config.yml");
		this.getConfig().addDefault("mpc.messages.commands.help.list", "&8/mpc list&r - &olists all avalible Plugins");
		this.getConfig().addDefault("mpc.messages.commands.help.stop", "&8/mpc stop <plugin>&r - &ostops the given Plugin");
		this.getConfig().addDefault("mpc.messages.commands.help.start", "&8/mpc start <plugin>&r - &oenables the given Plugin");
		this.getConfig().addDefault("mpc.messages.commands.help.load", "&8/mpc load <plugin> [-i]&r - &oloads a new Plugin from its File (-i to specify full relative path)");
		this.getConfig().addDefault("mpc.messages.commands.help.restart", "&8/mpc restart <plugin> [delay]&r - &ostops and restarts a given Plugin after a certain delay");
		this.getConfig().addDefault("mpc.messages.commands.stop.sucessful", "Sucessfully disabeled: @plugin");
		this.getConfig().addDefault("mpc.messages.commands.start.sucessful", "Sucessfully enabeled: @plugin");
		this.getConfig().addDefault("mpc.messages.commands.start.unsucessful", "Could not enable: @plugin");
		this.getConfig().addDefault("mpc.messages.commands.load.noArgs", "You need to specify a Plugin-File to load");
		this.getConfig().addDefault("mpc.messages.commands.load.loaded", "This Plugin is loaded allready");
		this.getConfig().addDefault("mpc.messages.commands.list.enabeled", "&2\u2022 &a&l@plugin&r");
		this.getConfig().addDefault("mpc.messages.commands.list.disabeled", "&4\u2022 &8&o@plugin&r");
		this.getConfig().addDefault("mpc.messages.commands.info", "\n&oversion:&r @version\n&oby:&r @authors\n&owebsite:&r @website");
		
		this.getConfig().addDefault("mpc.messages.general.noNumber", "@number ist keine valide Zahl!");
		this.getConfig().addDefault("mpc.messages.general.noPlugin", "Plugin could not be found: @plugin");
		this.getConfig().addDefault("mpc.messages.general.plugins", "Avalible Plugins:");
		this.getConfig().addDefault("mpc.messages.general.noPerm", "You do not have the permission to use this command");
		this.getConfig().addDefault("mpc.messages.general.notAvalibleForConsole", "This command is not available for the Console");
		this.getConfig().addDefault("mpc.messages.general.reloadSuccessful", "Successfully reloaded config.yml");

		this.saveConfig();
		this.getLogger().info(this.getConfig().getString("mpc.messages.general.reloadSuccessful"));
	}
	
	public List<String> getHelp(int page) {
		int linesperpage = this.getConfig().getInt("mpc.commands.help.linesperpage");
		List<String> helppages = new ArrayList<String>();
		ConfigurationSection helpsection = this.getConfig().getConfigurationSection("mpc.messages.commands.help");
		for (String key : helpsection.getKeys(false))
			if (key != "header")
				helppages.add(helpsection.getString(key));
		if (page < 1)
			return helppages;
		page %= helppages.size() / linesperpage;
		return helppages.subList((page - 1) * linesperpage, page * linesperpage);
	}
	
	public void sendMessage(CommandSender sender, String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (sender instanceof Player)
			sender.sendMessage(message);
		else
			this.getLogger().info(ChatColor.stripColor(message));
	}
	
	public void sendMessageWithPrefix(CommandSender sender, String message) {
		if (sender instanceof Player)
			sendMessage(sender, this.getPLName() + message);
		else
			sendMessage(sender, message);
	}
	
	public StringBuffer getPlugins() {
		StringBuffer sb = new StringBuffer();
		int n = 0;
		while (n < this.getServer().getPluginManager().getPlugins().length) {
			Plugin plugin = this.getServer().getPluginManager().getPlugins()[n];
			if (plugin.isEnabled())
				sb.append(this.getConfig().getString("mpc.messages.commands.list.enabeled").replace("@plugin", plugin.getName()));
			else
				sb.append(this.getConfig().getString("mpc.messages.commands.list.disabeled").replace("@plugin", plugin.getName()));
			if (n <= this.getServer().getPluginManager().getPlugins().length - 1)
				sb.append(", ");
			n++;
		}
		return sb;
	}
	
	public void restartPlugin(final CommandSender sender, final Plugin plugin, long delay) {
		MasterControlMain.getInstance().getServer().getPluginManager().disablePlugin(plugin);
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					MasterControlMain.getInstance().getServer().getPluginManager().enablePlugin(plugin);
					MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.commands.start.sucessful")
							.replace("@plugin", "'" + plugin.getName() + "'"));
				} catch (Exception ex) {
					MasterControlMain.getInstance().sendMessageWithPrefix(sender, MasterControlMain.getInstance().getConfig().getString("mpc.messages.commands.start.unsucessful")
							.replace("@plugin", "'" + plugin.getName() + "'"));
				}
			}
		}.runTaskLater(MasterControlMain.getInstance(), delay);
	}
	
}
