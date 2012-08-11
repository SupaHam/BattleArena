package mc.alk.arena.executors;

import java.util.ArrayList;
import java.util.List;

import mc.alk.arena.BattleArena;
import mc.alk.arena.controllers.ArenaEditor;
import mc.alk.arena.controllers.MessageController;
import mc.alk.arena.controllers.ProtectionController;
import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.spawns.SpawnInstance;
import mc.alk.arena.objects.spawns.TimedSpawn;
import mc.alk.arena.serializers.SpawnSerializer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class ArenaEditorExecutor extends CustomCommandExecutor {
	public static String idPrefix = "ar_";
	ProtectionController pc;
	WorldEditPlugin wep;
	ArenaEditor aac;
	public ArenaEditorExecutor(){
		super();
		this.ac = BattleArena.getBAC();
		this.aac = BattleArena.getArenaEditor();
	}

	@MCCommand(cmds={"select","sel"}, inGame=true, min=2, arenas={1}, op=true, usage="sel <arena>")
	public boolean arenaSelect(CommandSender sender, Command cmd, String commandLabel, Object[] args) {
		Arena arena = (Arena) args[1];
		aac.setCurrentArena((Player) sender, arena);
		return MessageController.sendMessage(sender,"You have selected " + arena.getName());
	}

	@MCCommand(cmds={"as","addspawn"}, selection=true, op=true, min=2,
			usage="/aa addspawn <mob/item/block/spawnGroup> [buffs or effects] [number] [fs=first spawn time] [rt=respawn time] [trigger=<trigger type>]")
	public boolean arenaAddMob(CommandSender sender, Command cmd, String commandLabel, Object[] args) {
		Player p = (Player) sender;
		Arena a = aac.getArena(p);
		SpawnInstance spawn = parseSpawn(args);
		if (spawn == null){
			return MessageController.sendMessage(sender,"Couldnt recognize spawn " + args[1]);			
		}
		Location l = p.getLocation();
		spawn.setLocation(l);
		TimedSpawn ts = new TimedSpawn(0,30,0,spawn);
		
		a.addTimedSpawn(ts);
		ac.updateArena(a);
		return MessageController.sendMessage(sender, "&6"+a.getName()+ "&e now has spawn " + spawn);
	}

	private SpawnInstance parseSpawn(Object[] args) {
		List<String> spawnArgs = new ArrayList<String>();
//		List<EditOption> optionArgs = new ArrayList<EditOption>();
		for (int i=1;i< args.length;i++){
			String arg = (String) args[i];
			if (arg.contains("=")){
				
			} else {
				spawnArgs.add(arg);
			}
		}
		int number = -1;
		if (spawnArgs.size() > 1){
			try {number = Integer.parseInt(spawnArgs.get(spawnArgs.size()-1));} catch(Exception e){}
		}
		if (number == -1){
			spawnArgs.add("1");}
		List<SpawnInstance> spawn = SpawnSerializer.parseSpawnable(spawnArgs);
		return spawn.get(0);
	}

	@MCCommand(cmds={"region","addregion","addr"}, selection=true, op=true)
	public boolean arenaAddWorldGuardRegion(CommandSender sender, Player p, Object[] args) {
		Arena a = aac.getArena(p);
		Selection sel = wep.getSelection(p);
		if (sel == null)
			return MessageController.sendMessage(sender, ChatColor.RED + "Please select the protection area first.");
		ProtectedRegion region = pc.addRegion(p, sel, idPrefix+a.getName());
		if (region == null)
			return MessageController.sendMessage(sender, ChatColor.RED + "Selected region could not be made");
		a.addRegion(region);
		ac.updateArena(a);
		return MessageController.sendMessage(sender, ChatColor.GREEN + "Region added to " + a.getName());
	}

	public void showHelp(CommandSender sender, Command command){
		help(sender,command,null,null);
	}

	@MCCommand( cmds = {"help","?"})
	public void help(CommandSender sender, Command command, String label, Object[] args){
		super.help(sender, command, args);
	}

}