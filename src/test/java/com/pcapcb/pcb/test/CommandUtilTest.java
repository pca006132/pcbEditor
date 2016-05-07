package com.pcapcb.pcb.test;

import org.junit.*;
import static org.junit.Assert.*;
import static com.pcapcb.pcb.utils.CommandUtil.*;

/**
 * Created by pca006132 on 2016/5/4.
 */
public class CommandUtilTest {
    @Test
    public void testEscape() {
        assertEquals("\\\"\\\\ \\\\\\\\ \\\\\\\\\\\\ ",escape("\"\\ \\\\ \\\\\\ "));
    }
    @Test
    public void testDeescape() {
        assertEquals("\"\\ \\\\ \\\\\\ ", deescape("\\\"\\\\ \\\\\\\\ \\\\\\\\\\\\ "));
    }
    @Test
    public void testColorBT() {
        assertEquals("setblock ~ ~ ~ standing_sign 0 replace {Text1:\"{\\\"text\\\":\\\"点我点我\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"run_command\\\",\\\"value\\\":\\\"say \\u00a7a 1\\\\\\\\\\\"}}\",Text2:\"{\\\"text\\\":\\\"\\\"}\",Text3:\"{\\\"text\\\":\\\"\\\"}\",Text4:\"{\\\"text\\\":\\\"\\\"}\"}",
                colorBlackTech("say §a 1\\"));
    }
}
