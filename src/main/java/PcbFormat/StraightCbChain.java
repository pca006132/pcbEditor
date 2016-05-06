package PcbFormat;

import Util.CoorUtil;
import Util.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pca006132 on 2016/4/30.
 */
public class StraightCbChain extends CBChain {
    private static Direction initialDir = Direction.positiveY;
    private Direction dir = initialDir;
    private static int limit = 0;
    private boolean autoChangeDirection = true;
    private int rowCbCount = 0;
    public StraightCbChain(int[] coor) {
        super(coor);
    }
    @Override
    public void addCb(String cmd, int lineNum) throws PcbParseException{
        if (cmd.equals("changeD")) {
            cbStack.peek().setDirection(CoorUtil.directionToCBDamage(Direction.positiveX));
            dir = CoorUtil.inverseDir(dir);
            return;
        }

        Direction tempDir = dir;
        if (limit != 0 && autoChangeDirection) {
            rowCbCount++;
            if (rowCbCount == limit) {
                tempDir = Direction.positiveX;
                rowCbCount = 0;
                dir = CoorUtil.inverseDir(dir);
            }
        }
        int[] coor = getNextCbCoor();
        if (isNewChain) {
            isNewChain = false;
            coor = newCoor;
        }
        cbStack.push(new CommandBlock(cmd, coor[0], coor[1], coor[2],
                CoorUtil.directionToCBDamage(tempDir), lineNum));
    }
    @Override
    public String addSign(String line) {
        int[] coor = cbStack.peek().getNextCbCoor();
        if (dir == Direction.negativeY || dir == Direction.positiveY)
            return (sign(line, coor[0] + 1, coor[1], coor[2],(byte) 5, false));
        else
            return (sign(line, coor[0], coor[1] + 1, coor[2],(byte) 12));
    }
    @Override
    public CBChain newChain(int[] coor) {
        return new StraightCbChain(coor);
    }
    @Override
    public List<String> getCommands() {
        List<String> cb_cmd = new ArrayList<String>();
        List<String> rcb_cmd = new ArrayList<String>();
        for (CommandBlock cb : cbStack) {
            if (cb.getCb_type() != CommandBlock.type.rcb) {
                cb_cmd.add(cb.toString());
            }
            else {
                rcb_cmd.add(cb.toString());
            }
        }
        cb_cmd.addAll(rcb_cmd);
        return cb_cmd;
    }
    public static void setDirection(Direction direction) {
        initialDir = direction;
    }
    public static void setRowCbLimit(int count) {
        if (count < 2)
            throw new IllegalArgumentException();
        limit = count;
    }
    public void disableAutoChangeDirection() {
        autoChangeDirection = false;
    }


}
