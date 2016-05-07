package com.pcapcb.pcb.test;

import com.pcapcb.pcb.format.CBChain;
import com.pcapcb.pcb.format.PcbParseException;
import com.pcapcb.pcb.format.PcbSettings;
import com.pcapcb.pcb.format.PcbToOOC;
import org.junit.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.Assert.*;
/**
 * Created by pca006132 on 2016/5/7.
 */
public class PcbToOOCTest {
    StringBuilder sb = new StringBuilder();
    int count = 2000;
    int matchCount = 0;
    CBChain setting = PcbSettings.getChain("");
    Pattern p = Pattern.compile("say \\\\\\\\\\\\\\\"testing");
    @Test
    public void testGeneratingOOC() throws PcbParseException {
        for (int i = 0; i < 2000; i++) {
            sb.append("say \"testing\n");
        }
        PcbToOOC pcbToOOC = new PcbToOOC();
        String[] results = pcbToOOC.getOOC(sb.toString(),setting);
        for (String result : results) {
            assertFalse(result.length() > 30000);
            Matcher m = p.matcher(result);
            while (m.find()) {
                matchCount++;
            }
        }
        assertEquals(count, matchCount);
    }
}
