package PcbFormat;

/**
 * Created by pca006132 on 2016/4/27.
 */
public class PcbParseException extends Exception {
    public PcbParseException(String problem, int lineNum) {
        super("PCB指令错误:\n第" + lineNum + "行, " + problem);
    }
    public PcbParseException(String problem) {super("pcb错误:" + problem);}
}
