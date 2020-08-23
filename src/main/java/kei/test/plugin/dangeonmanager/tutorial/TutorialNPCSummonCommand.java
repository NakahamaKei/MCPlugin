package kei.test.plugin.dangeonmanager.tutorial;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.plugin.Plugin;

public class TutorialNPCSummonCommand implements CommandExecutor {

	public TutorialNPCSummonCommand(Plugin plugin) {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			System.out.println("プレイヤーでコマンドを実行してください");
			return false;
		}

		Player player = (Player) sender;
		Location loc = player.getLocation();

		String cmd = command.getName();

		if (cmd.equalsIgnoreCase("tutorial")) {
			if (args.length != 1) {
				sender.sendMessage("チュートリアルNPCの番号を指定してください");
				return false;
			}

			char num = args[0].charAt(0);
			switch (num) {
			case '1':
				spawnTutorialNPC(player, loc, NPCName.TUTORIAL_NPC_1, Profession.CLERIC);
				break;
			case '2':
				spawnTutorialNPC(player, loc, NPCName.TUTORIAL_NPC_2, Profession.WEAPONSMITH);
				break;
			case '3':
				break;
			default:
				sender.sendMessage("有効なチュートリアルNPCの番号を指定してください");
				break;
			}
		}

		return false;
	}

	private void spawnTutorialNPC(Player player, Location location, String name, Profession profession) {
		Villager v = (Villager) player.getWorld().spawnEntity(location, EntityType.VILLAGER);
		v.setProfession(profession);
		v.setAdult();
		v.setAI(false);
		v.setInvulnerable(true);
		v.setCustomName(name);
		v.setAware(true);
	}
}
