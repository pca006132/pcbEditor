package com.pcapcb.pcb.format;

import com.pcapcb.pcb.utils.CoorUtil;
import com.pcapcb.pcb.utils.CommandUtil;

/**
 * Created by pca006132 on 2016/4/26.
 */
public class CommandBlock {
    public enum type {
        rcb, icb, ccb
    }
    private enum prefixes {
        i, r, c, d, nothing
    }
    private int relative_x;
    private int relative_y;
    private int relative_z;
    public int line_num;
    private boolean isCond = false;
    private boolean isAuto = true;
    private byte damage;
    public type cb_type = type.ccb;
    private String command;

    public CommandBlock(String cmd, int x, int y, int z, byte direction, int lineNum)
    throws PcbParseException{
        relative_x = x;
        relative_y = y;
        relative_z = z;
        line_num = lineNum;
        damage = direction;
        boolean have_prefix = true;
        do {
            if (cmd.startsWith("icb:")) {
                cb_type = type.icb;
                isAuto = false;
                cmd = cmd.substring(4);
            }
            else if (cmd.startsWith("rcb:")) {
                cb_type = type.rcb;
                cmd = cmd.substring(4);
            }
            else if (cmd.startsWith("cond:")) {
                isCond = true;
                cmd = cmd.substring(5);
            }
            else if (cmd.startsWith("data:")) {
                damage = Byte.parseByte(cmd.substring(5,6));
                if (damage > 7)
                    throw new PcbParseException("data数值错误: 不可大于7", lineNum);
                if (cmd.charAt(6) != ' ')
                    throw new PcbParseException("data前缀错误", lineNum);
                cmd = cmd.substring(7);
            }
            else {
                have_prefix = false;
                command = cmd;
            }
        } while (have_prefix);
    }
    public void setDirection(byte direction) {
        damage = direction;
    }
    public type getCb_type() {
        return cb_type;
    }
    public byte getDamage() {return damage;}
    public boolean isCond() {return isCond;}
    public int[] getNextCbCoor() {
        int[] coor = {relative_x, relative_y, relative_z};
        return CoorUtil.move( coor, CoorUtil.CBDamageToDirection(damage));
    }
    @Override
    public String toString() {
        String block = "";
        switch (cb_type) {
            case rcb:
                block = "repeating_command_block";
                break;
            case ccb:
                block = "chain_command_block";
                break;
            case icb:
                block = "command_block";
                break;
        }
        return String.format("setblock ~%d ~%d ~%d %s %d replace {Command:\"%s\"%s}", relative_x, relative_y, relative_z, block, damage + (isCond?8:0), CommandUtil.escape(command) , (isAuto?",auto:1b":""));
    }
}
