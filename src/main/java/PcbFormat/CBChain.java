package PcbFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
/**
 * Created by pca006132 on 2016/4/29.
 */
public abstract class CBChain implements Iterable<CommandBlock> {
    Stack<CommandBlock> cbStack = new Stack<CommandBlock>();
    boolean isNewChain = true;
    int[] newCoor = new int[3];
    public CBChain(int[] coor) {
        newCoor = coor;
    }
    public Iterator<CommandBlock> iterator() {
        return cbStack.iterator();
    }
    public int[] getNextCbCoor() {
        if (cbStack.size() == 0)
            return newCoor;
        else
            return cbStack.peek().getNextCbCoor();
    }
    public abstract void addCb(String cmd, int lineNum) throws PcbParseException;
    public abstract String addSign(String line);
    public abstract CBChain newChain(int[] coor);
    public abstract List<String> getCommands();
    public String condError() {
        StringBuilder result = new StringBuilder();
        if (cbStack.size() < 1)
            return "";
        int lastDamage = cbStack.get(0).getDamage();
        for (CommandBlock cb : cbStack) {
            if (cb.isCond() && cb.getDamage() != lastDamage) {
                result.append(String.format("第%d行的cond CB刚好位于转向的位置\n", cb.line_num));
            }
            lastDamage = cb.getDamage();
        }
        return result.toString();
    }

    //for internal use only
    String sign(String cmd, int x, int y, int z, byte direction, boolean standing) {
        String nbt = cmd.substring(5);
        return String.format("setblock ~%d ~%d ~%d %s %d replace %s", x, y, z,(standing?"standing_sign":
                "wall_sign"), direction, nbt);
    }
    String sign(String cmd, int x, int y, int z, byte direction) {

        return sign(cmd, x, y, z, direction, true);
    }
}
