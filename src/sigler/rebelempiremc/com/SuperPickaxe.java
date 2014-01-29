//Updated 1/22/14 7:52PM
package sigler.rebelempiremc.com;

import java.util.ArrayList;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SuperPickaxe extends JavaPlugin
{
	//Vault Start
	public static Permission permission = null;
	public static Economy economy = null;
	public static Chat chat = null;

	private boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	private boolean setupChat()
	{
		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}

		return (chat != null);
	}

	private boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}
	//Vault End

	public void onEnable()
	{
		if (!setupEconomy() ) {
			getServer().getLogger().info("SuperPickAxe enabled!");
			return;
		}
		setupPermissions();
		setupChat();
		saveDefaultConfig();
		spa.removeAll(spa);
	}

	public void onDisable()
	{
		getServer().getLogger().info("SuperPickAxe disabled!");
	}

	ArrayList<Player> spa = new ArrayList<Player>();
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			if (cmd.getName().equalsIgnoreCase("spa") || cmd.getName().equalsIgnoreCase("superpickaxe"))
			{
				final Player player = (Player) sender;
				if (sender.hasPermission("superpickaxe.use"))
				{
					final int money = this.getConfig().getInt("CostToUse");
					int NegativeTimeEffectsInSeconds = this.getConfig().getInt("NegativeTimeEffectsInSeconds");
					final int negative = NegativeTimeEffectsInSeconds * 20;
					int NegativeTimeEffectsInSeconds2 = this.getConfig().getInt("NegativeTimeEffectsInSeconds");
					int negativecool = NegativeTimeEffectsInSeconds2 * 20;
					int PositiveTimeEffectsInSeconds = this.getConfig().getInt("PositiveTimeEffectsInSeconds");
					int positive = PositiveTimeEffectsInSeconds * 20;
					int CooldownInSeconds = this.getConfig().getInt("CooldownInSeconds");
					int cooldownsec = CooldownInSeconds * 20;
					int negativerun = positive + negativecool;
					int cooldown = cooldownsec + negativerun;
					float cooldowndisplay = cooldown/20;

					if (!(spa.contains(player)))
					{
						spa.add(player);
						player.sendMessage(ChatColor.GREEN + "I feel energetic, lets mine!");
						player.playSound(player.getLocation(),Sound.FIREWORK_BLAST,1, 0);
						player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, positive, 127));
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, positive, 1));
						this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {// the scheduler starts ticking, this is a delayed scheduler, which means : after a certain amount of time, he activates your code inside it
							public void run()
							{
								player.sendMessage(ChatColor.YELLOW + "I feel" + ChatColor.MAGIC + " 090 " +ChatColor.MAGIC + ChatColor.YELLOW + "tired from" + ChatColor.MAGIC + " 090 " + ChatColor.YELLOW + "that mining...");
								player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, negative, 1));
								player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, negative, 1));
								player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, negative, 1));
								player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, negative, 1));
								player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, negative, 1));
								player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, negative, 1));
								player.playSound(player.getLocation(),Sound.EXPLODE,1, 0);

							}
						}, positive);
						if(!(sender.hasPermission("superpickaxe.bypasscooldown")))
						{
							this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {// the scheduler starts ticking, this is a delayed scheduler, which means : after a certain amount of time, he activates your code inside it
								public void run()
								{
									spa.remove(player);
									EconomyResponse r = economy.withdrawPlayer(player.getName(), money);
									if(r.transactionSuccess()) {
										player.sendMessage(ChatColor.DARK_AQUA + "The amount of " + ChatColor.RED + money + " has been deducted and your cooldown expired!");
									} else {
										player.sendMessage(String.format("An error occured: %s", r.errorMessage));
									}
								}
							}, cooldown);
						}else
						{
							if (!(sender.hasPermission("superpickaxe.bypassmoney")))
							{
								this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
								{// the scheduler starts ticking, this is a delayed scheduler, which means : after a certain amount of time, he activates your code inside it

									public void run()
									{
										spa.remove(player);
										EconomyResponse r = economy.withdrawPlayer(player.getName(), money);
										if(r.transactionSuccess()) {
											player.sendMessage(ChatColor.DARK_AQUA + "The amount of " + ChatColor.RED + money + " has been deducted and your cooldown expired!");
										} else {
											player.sendMessage(String.format("An error occured: %s", r.errorMessage));
										}
									}
								}, negativerun);
							}else
							{
								this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable()
								{// the scheduler starts ticking, this is a delayed scheduler, which means : after a certain amount of time, he activates your code inside it

									public void run()
									{
										spa.remove(player);
										player.sendMessage(ChatColor.DARK_AQUA + "Your cooldown has expired!");
									}
								}, negativerun);
							}
						}

					}
					else
					{
						player.sendMessage(ChatColor.RED + "There is a " + cooldowndisplay/60 + " minute cooldown enabled!");
					}

				}
				else
				{
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission!");
				}

			}

		}else
		{
			sender.sendMessage("You must be a player!");
		}
		return false;
	}
}


