package PcbFormat;

import Util.Direction;

/**
 * Created by pca006132 on 2016/5/3.
 */
public class PcbSettings {
    static int[] coor = {0,2,0};
    private static CBChain parseBoxCmd(String cmd) throws IllegalArgumentException {
        String[] elements = cmd.split(" ");
        BoxCbChain chain = new BoxCbChain(coor);
        if (elements.length % 2 == 1)
            throw new IllegalArgumentException();
        for (int i =0; i < elements.length - 1; i += 2) {
            String[] subElements = null;
            switch (elements[i]) {
                case "-xSize":
                    chain.setxLimit(Integer.parseInt(elements[i+1]));
                    break;
                case "-zSize":
                    chain.setzLimit(Integer.parseInt(elements[i+1]));
                    break;
                case "-outerCase":
                    subElements = elements[i+1].split(":");
                    chain.setOuterCase(subElements[0],
                            Byte.parseByte(subElements[1]));
                    break;
                case "-baseCase":
                    subElements = elements[i+1].split(":");
                    chain.setBaseCase(subElements[0],
                            Byte.parseByte(subElements[1]));
                    break;
            }
        }
        return chain;
    }
    private static CBChain parseStraightChainCmd(String cmd) throws IllegalArgumentException{
        String[] elements = cmd.split(" ");
        StraightCbChain chain = new StraightCbChain(coor);
        if (cmd.length() > 0 && elements.length % 2 == 1)
            throw new IllegalArgumentException();
        for (int i =0; i < elements.length - 1; i += 2) {
            String[] subElements = null;
            switch (elements[i]) {
                case "-count":
                    chain.setRowCbLimit(Integer.parseInt(elements[i+1]));
                    break;
                case "-direction":
                    Direction dir;
                    switch (elements[i+1]) {
                        case "+y":
                            dir = Direction.positiveY;
                            break;
                        case "-y":
                            dir = Direction.negativeY;
                            break;
                        case "+z":
                            dir = Direction.positiveZ;
                            break;
                        case "-z":
                            dir = Direction.negativeZ;
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    chain.setDirection(dir);
            }
        }
        return chain;
    }
    public static CBChain getChain(String cmd) {
        if (cmd.startsWith("box ")) {
            return parseBoxCmd(cmd.substring(4));
        } else {
            return parseStraightChainCmd(cmd);
        }
    }
}
