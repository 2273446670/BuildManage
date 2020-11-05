package com.buildmanage.main;

import com.buildmanage.config.BMConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import com.buildmanage.commandsexecute.BMCommandEntrance;
import com.buildmanage.config.BMPermissions;
import com.buildmanage.listen.BMBlockListener;
import java.lang.reflect.Field;
import java.util.logging.Logger;

public class BuildManage extends JavaPlugin{

    public static BuildManage buildManage;
    private Logger logger = this.getLogger();

    @Override
    public void onDisable() {

        logger.info(ChatColor.RED+"[BuildManage] 插件被关闭");

    }

    private String CMD = "bm";
    public String getCMD() {
        return CMD;
    }

    @Override
    public void onEnable() {

        buildManage = this;
        this.saveConfig();

        //注册事件
        this.getServer().getPluginManager().registerEvents(new BMBlockListener(), this);
        //注册权限
        getServer().getPluginManager().addPermission(new Permission(BMPermissions.BREAK.toString(), PermissionDefault.OP));
        getServer().getPluginManager().addPermission(new Permission(BMPermissions.PLACE.toString(), PermissionDefault.OP));
        //生成配置文件
        saveDefaultConfig();

        //注册指令
        Field commandMap = null;
        try {
            commandMap = getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            CommandMap map = (CommandMap) commandMap.get(getServer());
            boolean reg = map.register("bukkit",new BMCommandEntrance("bm"));
            if (!reg){
                map.register("bukkit",new BMCommandEntrance("buildmanage"));
                this.CMD = "buildmanage";
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println(ChatColor.GREEN+"[我的自定义插件] 初始化完成！");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        BMConfigUtil.reloadConfig();
    }
}
