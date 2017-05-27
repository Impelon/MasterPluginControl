package de.Impelon.masterplugincontrol;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MasterControlMain extends JavaPlugin {
	
	public static MasterControlMain main;
	private MasterControlExecutor mpcexe;

	@Override
	public void onEnable() {
		main = this;
		loadConfig();
		mpcexe = new MasterControlExecutor(this);
		this.getCommand("mpc").setExecutor(mpcexe);
	}

	public final String getPLName() {
		String name = ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + ChatColor.MAGIC + "|" + ChatColor.BLACK + ChatColor.MAGIC + "|" + ChatColor.GRAY
				+ ChatColor.MAGIC + "|" + ChatColor.WHITE + ChatColor.BOLD + this.getDescription().getName() + ChatColor.GRAY + ChatColor.MAGIC + "|"
				+ ChatColor.BLACK + ChatColor.MAGIC + "|" + ChatColor.GRAY + ChatColor.MAGIC + "|" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;
		return name;
	}

	public StringBuffer getPlugins() {
		StringBuffer sb = new StringBuffer();
		for (Plugin plugin : this.getServer().getPluginManager().getPlugins()) {
			sb.append(" | ");
			if (plugin.isEnabled()) {
				sb.append(ChatColor.DARK_GREEN + "\u269D" + ChatColor.GREEN + ChatColor.BOLD + plugin.getName() + ChatColor.WHITE);
			} else {
				sb.append(ChatColor.DARK_RED + "\u269D" + ChatColor.DARK_GRAY + ChatColor.ITALIC + plugin.getName() + ChatColor.WHITE);
			}
			sb.append(" | ");
		}
		return sb;
	}

	public void loadConfig() {
		reloadConfig();
		this.getConfig().options().header("MasterPluginControl | Plugin configuration");
		this.getConfig().options().copyDefaults(true);

		this.getConfig().addDefault("mpc.messages.commands.help.help", "displays this message");
		this.getConfig().addDefault("mpc.messages.commands.help.info", "displays information on MasterPluginControl itself");
		this.getConfig().addDefault("mpc.messages.commands.help.reload", "reloads the config.yml");
		this.getConfig().addDefault("mpc.messages.commands.help.list", "lists all avalible Plugins");
		this.getConfig().addDefault("mpc.messages.commands.help.stop", "stops the given Plugin");
		this.getConfig().addDefault("mpc.messages.commands.help.start", "enables the given Plugin");
		this.getConfig().addDefault("mpc.messages.commands.help.load", "loads a new Plugin from its File");
		this.getConfig().addDefault("mpc.messages.commands.help.restart", "stops and restarts a given Plugin after a certain delay");
		this.getConfig().addDefault("mpc.messages.commands.stop.sucessful", "Sucessfully disabeled: ");
		this.getConfig().addDefault("mpc.messages.commands.start.sucessful", "Sucessfully enabeled: ");
		this.getConfig().addDefault("mpc.messages.commands.restart.noNumber", "is not a valid number");
		this.getConfig().addDefault("mpc.messages.commands.load.noArgs", "You need to specify a Plugin-File to load");
		this.getConfig().addDefault("mpc.messages.commands.load.loaded", "This Plugin is loaded allready");
		this.getConfig().addDefault("mpc.messages.general.noPlugin", "Plugin could not be found: ");
		this.getConfig().addDefault("mpc.messages.general.plugins", "Avalible Plugins:");
		this.getConfig().addDefault("mpc.messages.general.noPerm", "You do not have the permission to use this command");
		this.getConfig().addDefault("mpc.messages.general.notAvalConsole", "This command is not available for the Console");
		this.getConfig().addDefault("mpc.messages.general.reloadSuccessful", "Successfully reloaded config.yml");

		this.saveConfig();
		System.out.println(this.getConfig().getString("mpc.messages.general.reloadSuccessful"));
	}
}
