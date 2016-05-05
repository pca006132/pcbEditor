package PcbFormat;

import java.util.*;
import static PcbFormat.CommandUtil.escape;

/**
 * Created by pca006132 on 2016/4/26.
 *
 */
public class parsePcb {
    public static boolean marker_type = false; //false = AEC, true = AS
    private int current_line_num = 0;
    private Stack<CBChain> chains = new Stack<CBChain>();

    public String[] getOOC(String pcb, CBChain chain) throws PcbParseException {
        return Command2OOC(PCB2Command(pcb, chain));
    }

    public String checkForCondDir() {
        StringBuilder sb = new StringBuilder();
        for (CBChain chain : chains) {
            sb.append(chain.condError());
        }
        return sb.toString();
    }

    private String marker(String cmd, int[] coor) {
        String[] parts = cmd.split(" ");
        int x = coor[0];
        int y = coor[1];
        int z = coor[2];
        String name = parts[0].substring(5);
        if (marker_type) {
            StringBuilder result = new StringBuilder(String.format(
                    "summon ArmorStand ~%d ~%d ~%d {CustomName:%s,NoGravity:1b,Invisible:1b",
                    x, y, z, name));
            if (parts.length > 1) {
                result.append(",Tags:[");
                for (int i = 1; i < parts.length; i++) {
                    result.append('"');
                    result.append(parts[i]);
                    result.append("\",");
                }
                result.replace(result.length() - 1, result.length(), "");
                result.append(']');
            }
            result.append("}");
            return result.toString();
        } else {
            StringBuilder result = new StringBuilder(String.format(
                    "summon AreaEffectCloud ~%d ~%d ~%d {CustomName:%s,Duration:2147483647",
                    x, y, z, name));
            if (parts.length > 1) {
                result.append(",Tags:[");
                for (int i = 1; i < parts.length; i++) {
                    result.append('"');
                    result.append(parts[i]);
                    result.append("\",");
                }
                result.replace(result.length() - 1, result.length(), "");
                result.append(']');
            }
            result.append("}");
            return result.toString();
        }
    }

    private String[] stats(String cmd, int x, int y, int z) throws PcbParseException {
        String[] components = cmd.split(" ");
        if (components.length < 3)
            throw new PcbParseException("stats命令格式错误", current_line_num);
        String stat = components[0].substring(6);
        switch (stat) {
            case "ab":
                stat = "AffectedBlocks";
                break;
            case "ae":
                stat = "AffectedEntities";
                break;
            case "ai":
                stat = "AffectedItems";
                break;
            case "qr":
                stat = "QueryResult";
                break;
            case "sc":
                stat = "SuccessCount";
                break;
        }
        String[] result = {String.format("scoreboard players set %s %s 0",components[1], components[2]),
                String.format("stats block ~%d ~%d ~%d set %s %s %s",
                        x, y, z, stat, components[1], components[2])};
        return result;
    }

    private String[] PCB2Command(String pcb, CBChain chain) throws PcbParseException {
        current_line_num = 0;
        String[] lines = pcb.split("\n");
        List<String> init_cmd = new ArrayList<String>();
        List<String> cb_cmd = new ArrayList<String>();
        List<String> middle_cmd = new ArrayList<String >();
        List<String> last_cmd = new ArrayList<String>();

        //variable used for parsing
        boolean isComment = false;
        chains.push(chain);
        //loop for parsing
        for (String rawline : lines) {
            current_line_num++;
            String line = rawline.trim();
            //multiline comment
            if (isComment) {
                if (line.endsWith("*/"))
                    isComment = false;
                continue;
            }
            if (line.startsWith("/*")) {
                isComment = true;
                continue;
            }
            //single line comment
            if (line.startsWith("//"))
                continue;
            if (line.length() == 0)
                continue;
            //new line
            if (line.startsWith("new")) {
                String[] components = line.split(" ");
                if (components.length < 4)
                    throw new PcbParseException("new命令格式错误", current_line_num);
                try {
                    int[] newChainCoor = new int[3];
                    newChainCoor[0] = Integer.parseInt(components[1]);
                    newChainCoor[1] = Integer.parseInt(components[2]) + 2;
                    newChainCoor[2] = Integer.parseInt(components[3]);
                    if (components.length == 5 && components[4].equals("py")) {
                        chains.push(new StraightCbChain(newChainCoor));
                        ((StraightCbChain)chains.peek()).disableAutoChangeDirection();
                    }
                    else
                        chains.push(chain.newChain(newChainCoor));
                } catch (NumberFormatException e) {
                    throw new PcbParseException("new命令数值错误", current_line_num);
                }
                continue;
            }

            if (line.startsWith("init:"))
                init_cmd.add(line.substring(5));
            else if (line.startsWith("after:"))
                last_cmd.add(line.substring(6));
            else if (line.startsWith("mark:"))
                middle_cmd.add(marker(line, chains.peek().getNextCbCoor()));
            else if (line.startsWith("sign:")) {
                String cmd = chains.peek().addSign(line);
                if (cmd.length() != 0)
                    middle_cmd.add(line);
            } else if (line.startsWith("stats:")) {
                int[] coor = chains.peek().getNextCbCoor();
                last_cmd.addAll(Arrays.asList(stats(line, coor[0], coor[1], coor[2])));
            }
            else {
                    chains.peek().addCb(line, current_line_num);
            }
        }
        for (CBChain c : chains) {
            cb_cmd.addAll(c.getCommands());
        }

        //return result
        List<String> result = new ArrayList<String>();
        result.addAll(init_cmd);
        result.addAll(cb_cmd);
        result.addAll(middle_cmd);
        result.addAll(last_cmd);
        return result.toArray(new String[result.size()]);
    }

    private String[] Command2OOC(String[] commands) throws PcbParseException {
        OOC oocs = new OOC(commands);
        return oocs.getOOCs();
    }
}
