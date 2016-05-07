package com.pcapcb.pcb.test; /**
 * Created by pca006132 on 2016/5/4.
 */
import java.util.Arrays;
import java.util.Collection;

import com.pcapcb.pcb.format.CommandBlock;
import com.pcapcb.pcb.format.PcbParseException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;
import static com.pcapcb.pcb.format.CommandBlock.type.*;


@RunWith(Parameterized.class)
public class CommandBlockTest {
    private CommandBlock cb;
    private String expectedToString;
    private boolean expectedIsCond;
    private byte expectedDamage;
    private CommandBlock.type expectedCBType;

    public CommandBlockTest(String cmd, String result, boolean isCond, byte damage, CommandBlock.type typeOfCB) throws PcbParseException {
        cb = new CommandBlock(cmd, 0, 2, 0, (byte)0, 0);
        expectedToString = result;
        expectedIsCond = isCond;
        expectedDamage = damage;
        expectedCBType = typeOfCB;
    }
    @Test
    public void testParseCommand() {
        assertEquals(expectedIsCond, cb.isCond());
        assertEquals(expectedDamage, cb.getDamage());
        assertEquals(expectedCBType, cb.getCb_type());
        assertEquals(expectedToString,cb.toString());
    }
    @Parameterized.Parameters
    public static Collection<Object[]> cmds() {
        return Arrays.asList(new Object[][] {
                {"say \"fa q\" \\","setblock ~0 ~2 ~0 chain_command_block 0 replace {Command:\"say \\\"fa q\\\" \\\\\",auto:1b}",false,(byte)0,ccb},
                {"cond:say hi","setblock ~0 ~2 ~0 chain_command_block 8 replace {Command:\"say hi\",auto:1b}", true,(byte)0,ccb},
                {"data:5 icb:say test","setblock ~0 ~2 ~0 command_block 5 replace {Command:\"say test\"}", false, (byte)5, icb},
                {"data:5 cond:rcb:say hi","setblock ~0 ~2 ~0 repeating_command_block 13 replace {Command:\"say hi\",auto:1b}",true, (byte)5, rcb}
        });
    }
}
