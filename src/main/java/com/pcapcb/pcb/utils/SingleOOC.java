package com.pcapcb.pcb.utils;

import com.pcapcb.pcb.format.PcbParseException;

import static com.pcapcb.pcb.utils.CommandUtil.colorBlackTech;
import static com.pcapcb.pcb.utils.CommandUtil.escape;

/**
 * Created by pca006132 on 2016/5/5.
 */
public class SingleOOC {
    //constants
    final String prefix = "/summon FallingSand ~ ~1 ~2 " +
            "{Time:1,Block:\"minecraft:redstone_block\",Passengers:" +
            "[{id:\"FallingSand\",Time:1,Block:\"minecraft:activator_rail\"" +
            ",Passengers:[";
    final String suffix = "{id:\"MinecartCommandBlock\",Command:\"" +
            "/setblock ~ ~-1 ~ minecraft:lava 15\"},{id:\"MinecartCommandBlock\",Command:\"setblock ~ ~ ~ air 0\"}]}]}";
    final String cmdPrefix = "{id:\"MinecartCommandBlock\",Command:\"";
    final int prefixLength = prefix.length();
    final int colorPrefixLength = getColorModeLength(prefix);
    final int cmdPrefixLength = cmdPrefix.length();
    final int colorCmdPrefixLength = getColorModeLength(cmdPrefix);
    final int suffixLength = suffix.length();
    final int colorSuffixLength = getColorModeLength(suffix);
    //end constants

    private boolean useColorBlackTech;
    private StringBuilder cmd = new StringBuilder();
    private int normalLength = prefixLength;
    private int colorModeLength = colorPrefixLength;
    private boolean oocGenerated = false;

    public SingleOOC() {
        cmd.append(prefix);
    }

    public void addCommand(String command) throws IllegalAccessError {
        if (oocGenerated)
            throw new IllegalAccessError();
        if (command.contains("§"))
            useColorBlackTech = true;
        cmd.append(cmdPrefix);
        cmd.append(escape(command));
        cmd.append("\"},");
        colorModeLength += getColorModeLength(command) + colorCmdPrefixLength + 4;
        normalLength += getEscapedLength(command) + cmdPrefixLength + 3;
    }
    public String getOOC() {
        if (useColorBlackTech)
            return getColorOOC();
        else
            return getNormalOOC();
    }
    public boolean canAddCommand(String command) throws PcbParseException {
        if (escape(command).length() > 30000)
            throw new PcbParseException("单一命令过长");
        if (useColorBlackTech || command.contains("§")) {
            if (colorModeLength + getColorModeLength(command) + colorCmdPrefixLength + 4 + colorSuffixLength > 28000)
                return false;
            else
                return true;
        } else {
            if (normalLength + getEscapedLength(command) + cmdPrefixLength + 3 + suffixLength> 30000)
                return false;
            else
                return true;
        }
    }

    private int getEscapedLength(String str) {
        int specialCharCount = 0;
        int strLength = str.length();
        for (int i = 0; i < strLength; i++) {
            switch (str.charAt(i)) {
                case '"':
                case '\\':
                    specialCharCount++;
            }
        }
        return specialCharCount + strLength;
        // (specialCharCount * 2 + non special char count)
    }
    private int getColorModeLength(String str) {
        int specialCharCount = 0;
        int colorCharCount = 0;
        int strLength = str.length();
        for (int i = 0; i < strLength; i++) {
            switch (str.charAt(i)) {
                case '"':
                case '\\':
                    specialCharCount++;
                    break;
                case '§':
                    colorCharCount+=5; //+ 6(-> 6 char) - 1(from str length)
            }
        }
        return specialCharCount*3 + strLength + colorCharCount;
    }

    String getNormalOOC() {
        if (oocGenerated)
            return cmd.toString();
        else {
            cmd.append(suffix);
            oocGenerated = true;
            return cmd.toString();
        }
    }
    String getColorOOC() {
        if (oocGenerated)
            return colorBlackTech(cmd.toString());
        else {
            cmd.append(suffix);
            oocGenerated = true;
            return colorBlackTech(cmd.toString());
        }
    }
}