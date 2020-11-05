package com.buildmanage.config;

import org.bukkit.configuration.file.FileConfiguration;
import com.buildmanage.main.BuildManage;
import java.util.ArrayList;
import java.util.List;

public class BMConfigUtil {

    private static FileConfiguration config = BuildManage.buildManage.getConfig();
    public static void reloadConfig(){
        config = BuildManage.buildManage.getConfig();
    }

    public static List<String> getWhiteList(String worldName, String key) {

        try {
            FileConfiguration config = BuildManage.buildManage.getConfig();
            List<String> whiteList = config.getStringList(worldName+"."+key);

            if (whiteList == null) {
                return new ArrayList<String>();
            } else {
                return whiteList;
            }

        } catch (Exception e) {
            return new ArrayList<String>();
        }

    }

    /**
     * 世界是否被插件管理
     * @param worldName
     * @return
     */
    public static boolean worldManage(String worldName){

        return config.get(worldName) == null ? false : true;
    }

    /**
     * 是否可以在领地建筑
     * @param worldName
     * @return
     */
    public static boolean worldRes(String worldName){

        if (!worldManage(worldName)){return false;}
        Boolean wordRes = config.getBoolean(worldName+".res");
        return (wordRes == null || wordRes == false) ? false : true;
    }

    /**
     * 世界是否允许破坏
     * @param worldName
     * @return
     */
    public static boolean worldBreak(String worldName){

        Boolean worldManage = worldManage(worldName);
        if (!worldManage){
            return true;
        }else {

            Boolean worldBreak = config.getBoolean(worldName+".break");
            return (worldBreak == null || worldBreak == false)? false : true;

        }

    }

    /**
     * 世界是否允许放置
     * @param worldName
     * @return
     */
    public static boolean worldPlace(String worldName){

        Boolean worldManage = worldManage(worldName);
        if (!worldManage){
            return true;
        }else {

            Boolean worldBreak = config.getBoolean(worldName+".place");
            return (worldBreak == null || worldBreak == false)? false : true;

        }
    }






}
