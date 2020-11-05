package com.buildmanage.commandsexecute;

import com.buildmanage.main.BuildManage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class BMCommandEntrance extends Command {

    private BMCommandExecutor bmCommandExecutor = new BMCommandExecutor();
    private static List<Method> executeMethodList = new ArrayList<>();
    private Logger logger = BuildManage.buildManage.getLogger();
    public static List<Method> getCommandMethodList(){
        return executeMethodList;
    }

    public BMCommandEntrance(String name) {

        super(name);
        initExecuteMethodList();

    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {

        Player player = null;
        if (sender instanceof Player){

            player = (Player) sender;

            PlayerInventory playerInventory = player.getInventory();
            ItemStack itemStack = playerInventory.getItemInMainHand();
            Material material = Material.valueOf(itemStack.getType().toString());

            String id = String.valueOf(material.getId());
            Short s = itemStack.getDurability();
            //最终的方块ID
            String blockId = id + (s < 1 ? "":":"+s);
        }





        boolean isMatch = false;

        for (int p = 0; p < executeMethodList.size(); p++) {

            Method method = executeMethodList.get(p);
            BMCommandIndex cIndex = method.getAnnotation(BMCommandIndex.class);
            if (player!=null && ! player.isOp() && cIndex.op()){
                continue;
            }

            String[] params = cIndex.value();

            if (cIndex.value().length == args.length) {
                int matchSize = 0;
                for (int i = 0; i < args.length; i++) {
                    if (params[i].equals("?")) {
                        matchSize++;
                        continue;
                    } else if (params[i].equals(args[i])) {
                        matchSize++;
                        continue;
                    }
                }
                if (matchSize == args.length) {
                    try {
                        isMatch = (boolean)method.invoke(bmCommandExecutor,sender, currentAlias, args,player);
                    } catch (IllegalAccessException e) {
                        System.out.print(ChatColor.RED+e.getMessage());
                        e.printStackTrace();

                    } catch (InvocationTargetException e) {
                        System.out.print(ChatColor.RED+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }

        if (!isMatch){
            sender.sendMessage(ChatColor.GREEN+"输入：/bm help 查看帮助");
        }
        return isMatch;

    }

    /**
     * 加载指令方法
     */
    private void initExecuteMethodList(){

        Class<BMCommandExecutor> commandExecutorClass = (Class<BMCommandExecutor>) bmCommandExecutor.getClass();
        Method[] methods = commandExecutorClass.getDeclaredMethods();
        Arrays.stream(methods).forEach((method) -> {

            BMCommandIndex cIndex = method.getAnnotation(BMCommandIndex.class);
            if (cIndex != null){
                executeMethodList.add(method);
            }
        });
    }

//
//    /**
//     * 删除领地外可以破坏的方块白名单
//     * @param blockId
//     * @return
//     */
//    private String deleteBreakWhiteList(String blockId){
//
//        List<String> blockBreakWhiteList = BMConfigUtil.getWhiteList(worldName,"blockBreakWhiteList");
//        if (!blockBreakWhiteList.contains(blockId)){
//            return failColor+"删除失败,未在白名单!";
//        }
//
//        Iterator iterator = blockBreakWhiteList.iterator();
//        while (iterator.hasNext()){
//            String id = iterator.next().toString();
//            if (id.equals(blockId)){
//                iterator.remove();
//            }
//        }
//
//        config.set(worldName+".blockBreakWhiteList",blockBreakWhiteList);
//        buildManage.saveConfig();
//        return successColor+"删除成功!";
//    }
//
//    /**
//     * 添加领地外可以放置的方块白名单
//     * @param blockId
//     * @return
//     */
//    private String addPlaceWhiteList(String blockId){
//
//        List<String> blockPlaceWhiteList = BMConfigUtil.getWhiteList(worldName,"blockPlaceWhiteList");
//        if (blockPlaceWhiteList.contains(blockId)){
//            return failColor+"添加失败,已存在!";
//        }
//
//        blockPlaceWhiteList.add(String.valueOf(blockId));
//        config.set(worldName+".blockPlaceWhiteList",blockPlaceWhiteList);
//        buildManage.saveConfig();
//        return successColor+"添加成功!";
//    }
//
//    /**
//     * 删除领地外可以放置的方块白名单
//     * @param blockId
//     * @return
//     */
//    private String deletePlaceWhiteList(String blockId){
//
//        List<String> blockPlaceWhiteList = BMConfigUtil.getWhiteList(worldName,"blockPlaceWhiteList");
//        if (!blockPlaceWhiteList.contains(blockId)){
//            return failColor+"删除失败,未在白名单!";
//        }
//
//        Iterator iterator = blockPlaceWhiteList.iterator();
//        while (iterator.hasNext()){
//            String id = iterator.next().toString();
//            if (id.equals(blockId)){
//                iterator.remove();
//            }
//        }
//
//        config.set(worldName+".blockPlaceWhiteList",blockPlaceWhiteList);
//        buildManage.saveConfig();
//        return successColor+"删除成功!";
//    }



}
