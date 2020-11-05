package com.buildmanage.commandsexecute;

import com.buildmanage.config.BMConfigUtil;
import com.buildmanage.config.BMPermissions;
import com.buildmanage.main.BuildManage;
import jdk.nashorn.internal.ir.Block;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Method;
import java.util.List;

import static com.buildmanage.config.BMConfigUtil.getWhiteList;

public class BMCommandExecutor {

    private String cmd = "/"+BuildManage.buildManage.getCMD()+" ";
    private ChatColor helpColor = ChatColor.DARK_GREEN;
    private ChatColor errorColor = ChatColor.RED;

    @BMCommandIndex(value = {},op = false,help = "")
    public boolean toHelp(CommandSender sender, String currentAlias, String[] args, Player player){

        return help(sender, currentAlias, args, player);
    }

    @BMCommandIndex(value = "help",op = false,help = "")
    public boolean help(CommandSender sender, String currentAlias, String[] args, Player player){
        String[] newArgs = new String[2];
        newArgs[0] = "help";
        newArgs[1] = "1";
        return helpn(sender, currentAlias, newArgs, player);
    }

    @BMCommandIndex(value = {"help","?"},op = false,help = "")
    public boolean helpn(CommandSender sender, String currentAlias, String[] args, Player player){

        int size = 0;
        int start = 0;

        try {
            start = Integer.valueOf(args[1]) * 5 - 5;
        }catch (Exception e){
            sender.sendMessage(ChatColor.RED+"请输入"+cmd+"help [数字]");
        }

        sender.sendMessage(":"+start);

        List<Method> methods = BMCommandEntrance.getCommandMethodList();

        for (int i = start; i < methods.size(); i++) {
            Method method = methods.get(i);
            if (method.getName().equals("help") || method.getName().equals("helpn") || method.getName().equals("toHelp")){continue;}
            BMCommandIndex commandIndex = method.getAnnotation(BMCommandIndex.class);
            if (player!=null && ! player.isOp() && commandIndex.op()){
                continue;
            }else {
                sender.sendMessage(helpColor+cmd+commandIndex.help());
                size ++;
                if (size == 5){
                    return true;
                }
            }
        }

        return true;

    }

    @BMCommandIndex(value = "reload",op = true,help = "reload                   重新加载配置文件")
    public boolean reload(CommandSender sender, String currentAlias, String[] args, Player player){
        BMConfigUtil.reloadConfig();
        sender.sendMessage(ChatColor.GREEN+"成功重新加载配置文件!");
        return true;
    }

    @BMCommandIndex(value = {"res","?"},op = true,help = "res [true/false]                       是否允许玩家在自己的领地内自由建筑")
    public boolean res(CommandSender sender, String currentAlias, String[] args, Player player){

        sender.sendMessage("1");
        return true;
    }

    @BMCommandIndex(value = {"world","?"},op = true,help = "world [add/delete]                是否将当前世界加入建筑管理，加入后默认权限：" +
            "\t玩家可以在当前世界自己的领地中自由建筑" +
            "\t玩家不能在这个世界破坏自己领地外的一切方块（白名单除外）" +
            "\t玩家不能在这个世界自己的领地外放置一切方块（白名单除外）")
    public boolean worldManage(CommandSender sender, String currentAlias, String[] args, Player player){

        sender.sendMessage("2");
        return true;
    }

    @BMCommandIndex(value = {"block","break","?"},op = true,help = "block break [add/delete]            是否允许在当前世界破坏手中的方块")
    public boolean blockBreakeManage(CommandSender sender, String currentAlias, String[] args, Player player) {

        if (player == null){
            sender.sendMessage(helpColor+cmd+"block [break/place] [add/delete] [id]");
            return true;
        }

        String blockId = getPlayerBlockId(player);
        if (blockId == null){
            sender.sendMessage(ChatColor.RED+"未检测到手中的方块信息");
        }else {
            sender.sendMessage("blockId:"+blockId);
        }

        sender.sendMessage("3");
        return true;

    }

    @BMCommandIndex(value = {"block","place","?"},op = true,help = "block place [add/delete]            是否允许在当前世界放置手中的方块")
    public boolean blockPlaceManage(CommandSender sender, String currentAlias, String[] args,Player player){
        sender.sendMessage("4");
        return true;
    }

    @BMCommandIndex(value = {"block","break","?","?"},op = true,help = "block place [add/delete] [id]       是否允许在当前世界破坏id为？的方块")
    public boolean blockBreakIdManage(CommandSender sender, String currentAlias, String[] args,Player player){
        sender.sendMessage("5");
        return true;
    }

    @BMCommandIndex(value = {"block","place","?","?"},op = true,help = "block place [add/delete] [id]       是否允许在当前世界放置id为？的方块")
    public boolean blockPlaceIdManage(CommandSender sender, String currentAlias, String[] args,Player player){
        sender.sendMessage("6");
        return true;
    }

    private String getPlayerBlockId(Player player){

        PlayerInventory playerInventory = player.getInventory();
        ItemStack itemStack = playerInventory.getItemInMainHand();

        if (itemStack == null){
            return null;
        }

        Material material = Material.valueOf(itemStack.getType().toString());
        String id = String.valueOf(material.getId());

        Short s = itemStack.getDurability();
        String blockId = id + (s < 1 ? "":":"+s);

        return blockId;
    }


}
