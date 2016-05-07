package com.pcapcb.pcb.utils; /**
 * Created by pca006132 on 2016/3/15.
 */
import java.util.Random;
import java.math.BigInteger;

public class CommandUtil {


    public static String escape(String text) {
        return text.replace("\\","\\\\").replace("\"","\\\"");
    }
    public static String deescape(String text) {
        return text.replace("\\\"","\"").replace("\\\\","\\");
    }

    //used for colorBlackTech
    public static  String colorSignText = "点我点我";
    /**
     * change command to sign clickEvent.
     * will change '§' to \u00a7
     * but will not pack in a command block(a wrapper is needed instead)
     * @param text \command block str, setblock...
     * @return setblock of sign
     */
    public static String colorBlackTech(String text) {
        String setBlock = escape(escape(text));
        setBlock = setBlock.replace("§","\\u00a7");
        return "setblock ~ ~ ~ standing_sign 0 replace {Text1:\"{\\\"text\\\":\\\"" + colorSignText +
                "\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"run_command\\\",\\\"value\\\":\\\""
                + setBlock + "\\\"}}\",Text2:\"{\\\"text\\\":\\\"\\\"}\",Text3:\"{\\\"text\\\":\\\"\\\"}\",Text4:\"{\\\"text\\\":\\\"\\\"}\"}";
    }

    public static Long[] randomUUIDPair() {
        Random rnd = new Random();
        Long[] pair = {rnd.nextLong(), rnd.nextLong()};
        return pair;
    }
    public static String uuidPairToUUID(Long max, Long min) {
        StringBuilder builder = new StringBuilder();
        String most = Long.toHexString(max);
        String least = Long.toHexString(min);
        for (int i = 0; i < 16 - most.length(); i++) {
            builder.append('0');
        }
        builder.append(most);
        for (int i = 0; i < 16 - least.length(); i++) {
            builder.append('0');
        }
        builder.append(least);
        builder.insert(8,'-');
        builder.insert(13,'-');
        builder.insert(18,'-');
        builder.insert(23,'-');
        return builder.toString();
    }
    public static Long[] uuidToPair(String uuid) {
        uuid = uuid.replace("-","");
        if (uuid.length() != 32) {
            throw new IllegalArgumentException("invalid uuid format");
        }
        Long max,min;
        try {
            max = new BigInteger(uuid.substring(0,16),16).longValue();
            min = new BigInteger(uuid.substring(16),16).longValue();
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("invalid uuid format");
        }
        Long[] result = {max, min};
        return result;
    }
}
