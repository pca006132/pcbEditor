package PcbFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pca006132 on 2016/4/30.
 */
public class BoxCbChain extends CBChain {
    public static String outerBlock = "stained_glass";
    public static byte outerDamage = (byte)0;
    public static String baseBlock = "quartz_block";
    public static byte baseDamage = (byte)0;

    public static int xLimit = 5;
    public static int zLimit = 5;

    private List<String> signs = new ArrayList<String>();
    private int xCount = 0;
    private int yCount = 0;
    private int zCount = 0;
    private int signCount = 0;
    private Direction xDir = Direction.positiveX;
    private Direction zDir = Direction.positiveZ;

    public void setxLimit(int limit) {
        if (limit < 2)
            throw new IllegalArgumentException();
        xLimit = limit;
    }
    public void setzLimit(int limit) {
        if (limit < 2)
            throw new IllegalArgumentException();
        zLimit = limit;
    }
    public void setOuterCase(String block, byte damage) {
        outerBlock = block;
        outerDamage = damage;
    }
    public void setBaseCase(String block, byte damage) {
        baseBlock = block;
        baseDamage = damage;
    }

    public BoxCbChain(int[] coor) {
        super(coor);
    }
    @Override
    public void addCb(String cmd, int lineNum) throws PcbParseException {
        xCount++;
        Direction tempDir = xDir;
        if (xCount == xLimit) {
            xCount = 0;
            xDir = CoorUtil.inverseDir(xDir);
            zCount++;
            tempDir = zDir;
            if (zCount == zLimit) {
                tempDir = Direction.positiveY;
                zCount = 0;
                zDir = CoorUtil.inverseDir(zDir);
                yCount++;
            }
        }
        if (isNewChain) {
            isNewChain = false;
            cbStack.push(new CommandBlock(cmd, newCoor[0], newCoor[1], newCoor[2],
                    CoorUtil.directionToCBDamage(tempDir), lineNum));
        } else {
            int[] coor = getNextCbCoor();
            cbStack.push(new CommandBlock(cmd, coor[0], coor[1], coor[2],
                    CoorUtil.directionToCBDamage(tempDir), lineNum));
        }

    }
    @Override
    public CBChain newChain(int[] coor) {
        return new BoxCbChain(coor);
    }
    @Override
    public String addSign(String line) {
        signs.add(line);
        return "";
    }
    @Override
    public List<String> getCommands() {
        List<String> cb_cmd = new ArrayList<String>();
        List<String> rcb_cmd = new ArrayList<String>();

        //outer block
        cb_cmd.add(String.format(
                "fill ~%d ~%d ~%d ~%d ~%d ~%d %s %d hollow", newCoor[0]-1, newCoor[1], newCoor[2]-1,
                newCoor[0] + xLimit, newCoor[1] + yCount, newCoor[2] + zLimit,
                outerBlock, outerDamage));
        //top block
        cb_cmd.add(String.format(
                "fill ~%d ~%d ~%d ~%d ~%d ~%d %s %d hollow", newCoor[0]-1, newCoor[1] - 1, newCoor[2]-1,
                newCoor[0] + xLimit, newCoor[1] - 1, newCoor[2] + zLimit,
                baseBlock, baseDamage));
        //base block
        cb_cmd.add(String.format(
                "fill ~%d ~%d ~%d ~%d ~%d ~%d %s %d hollow", newCoor[0]-1, newCoor[1] + yCount + 1,
                newCoor[2]-1, newCoor[0] + xLimit, newCoor[1] + yCount + 1, newCoor[2] + zLimit,
                baseBlock, baseDamage));
        for (CommandBlock cb : cbStack) {
            if (cb.getCb_type() != CommandBlock.type.rcb) {
                cb_cmd.add(cb.toString());
            }
            else {
                rcb_cmd.add(cb.toString());
            }
        }
        cb_cmd.addAll(rcb_cmd);
        for (String nbt : signs) {
            int[] coor = getSignCoor();
            int[] coor1 = {newCoor[0]-2 - coor[0], newCoor[1] - 1 - coor[1], newCoor[2] - 1 - coor[2]};
            int[] coor2 = {newCoor[0] + xLimit - coor[0], newCoor[1] + yCount + 1 - coor[1],
                    newCoor[2] + zLimit - coor[2]};

            nbt = nbt.replace("{delete me}", String.format(
                    "fill ~%d ~%d ~%d ~%d ~%d ~%d air 0",coor1[0],coor1[1],coor1[2],
                    coor2[0],coor2[1],coor2[2]));
            signCount++;
            cb_cmd.add(sign(nbt, coor[0], coor[1], coor[2],(byte) 4, false));
        }
        return cb_cmd;
    }

    private int[] getSignCoor() {
        int[] coor = newCoor.clone();
        coor[0]-=2;
        coor[1] = coor[1] + signCount;
        coor[2] = coor[2] + (zLimit / 2);
        return coor;
    }
}
