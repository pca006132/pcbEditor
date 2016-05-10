package com.pcapcb.ui.editor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.jtattoo.plaf.fast.FastLookAndFeel;


import com.pcapcb.pcb.format.CBChain;
import com.pcapcb.pcb.format.PcbParseException;
import com.pcapcb.pcb.format.PcbSettings;
import com.pcapcb.pcb.format.PcbToOOC;
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Properties;

/**
 * Created by pca006132 on 2016/5/8.
 */
public class TextEditor extends RSyntaxTextArea {
    public TextEditor() {
        super(20,60);
        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/pcb", "com.pcapcb.ui.editor.PcbTokenMaker");
        FoldParserManager.get().addFoldParserMapping("text/pcb", new PcbFolding());
        setSyntaxEditingStyle("text/pcb");
        setLineWrap(true);
        setBracketMatchingEnabled(true);
        setCodeFoldingEnabled(true);
    }
}
