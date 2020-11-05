package com.buildmanage.listen;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import com.buildmanage.config.BMPermissions;
import static com.buildmanage.config.BMConfigUtil.getWhiteList;

import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class BMBlockListener implements Listener {

    public BMBlockListener(){
        Plugin resPlug = getServer().getPluginManager().getPlugin("Residence");
        if (resPlug != null) {
            System.out.println("[BuildManage] 成功载入依赖 Residence");
        }else {
            System.out.println("[BuildManage] 未能载入依赖 Residence");
        }
    }

    @EventHandler
    public void blockStopBreak(BlockBreakEvent event) {

        if (event.getPlayer().hasPermission(BMPermissions.BREAK.toString())){return;}

        List<String>blockBreakWhiteList = getWhiteList(event.getPlayer().getWorld().getName(),"blockBreakWhiteList");
        if (blockBreakWhiteList != null && inTheWhiteList(blockBreakWhiteList,event.getBlock())){return;}

        Residence residence = Residence.getInstance();

        if (!event.isCancelled()&&residence != null) {
            Location loc = event.getBlock().getLocation();
            ClaimedResidence res = residence.getResidenceManager().getByLoc(loc);

            if (res == null) {              //如果方块不在领地中
                event.setCancelled(true);   //取消此事件
            }
        }
    }

    @EventHandler
    public void blockStopplace(BlockPlaceEvent event) {

        if (event.getPlayer().hasPermission(BMPermissions.PLACE.toString())){return;}

        List<String> blockPlaceWhiteList = getWhiteList(event.getPlayer().getWorld().getName(),"blockPlaceWhiteList");
        if (blockPlaceWhiteList != null && inTheWhiteList(blockPlaceWhiteList,event.getBlock())){ return;}

        Residence residence = Residence.getInstance();

        if (!event.isCancelled()&&residence != null) {
            Location loc = event.getBlock().getLocation();
            ClaimedResidence res = residence.getResidenceManager().getByLoc(loc);

            if (res == null ) {             //如果方块不在领地中
                event.setCancelled(true);
            }
        }
    }

    private Boolean inTheWhiteList(List<String> whiteList, Block block){

        String type = block.getType().toString();
        Material material = Material.valueOf(type);
        String blockId = String.valueOf(material.getId());
        if (whiteList.contains(blockId)){
            return true;
        }
        return false;

    }

}
