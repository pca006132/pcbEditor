package com.pcapcb.ui.editor;

import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager;
import org.fife.ui.rsyntaxtextarea.*;

import java.awt.*;

/**
 * Created by pca006132 on 2016/5/8.
 */
public class TextEditor extends RSyntaxTextArea {
    public TextEditor() {
        super(20,40);
        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/pcb", "com.pcapcb.ui.editor.PcbTokenMaker");
        FoldParserManager.get().addFoldParserMapping("text/pcb", new PcbFolding());
        setSyntaxEditingStyle("text/pcb");
        setLineWrap(true);
        setBracketMatchingEnabled(true);
        setCodeFoldingEnabled(true);
        setAnimateBracketMatching(false);
        setFont(new Font("Microsoft Yahei", Font.PLAIN, 16));
        setCurrentLineHighlightColor(new Color(221,225,242));

        getPopupMenu().remove(1);
    }
}
