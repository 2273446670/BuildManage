package com.buildmanage.config;

public enum BMPermissions {

    /**
     * 所有方块的放置权限
     */
    PLACE("bm.place",0),
    /**
     * 所有方块的破坏权限
     */
    BREAK("bm.break",1),
    /**
     * 使用命令的权限
     */
    COMMAND("bm.command",2);

    private int code;
    private String desc;

    BMPermissions(String desc, int code) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }

    public int getCode(){
        return code;
    }

}
