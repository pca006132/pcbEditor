package com.pcapcb.ui.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.folding.Fold;
import org.fife.ui.rsyntaxtextarea.folding.FoldParser;
import org.fife.ui.rsyntaxtextarea.folding.FoldType;

/**
 * Created by pca006132 on 2016/5/8.
 */
public class PcbFolding implements FoldParser{
    @Override
    public List<Fold> getFolds(RSyntaxTextArea textArea) {
        List<Fold> folds = new ArrayList<>();

        Fold currentFold = null;
        int lineCount = textArea.getLineCount();

        boolean inMultilineComment = false;
        int commentStartOffset = -1;
        try {
            for (int i = 0; i < lineCount; i++) {
                String line = textArea.getText(textArea.getLineStartOffset(i),                          textArea.getLineEndOffset(i) - textArea.getLineStartOffset(i)).
                        trim();

                //handle multiline comment
                if (line.startsWith("/*")) {
                    commentStartOffset = textArea.getLineStartOffset(i);
                    inMultilineComment = true;
                }
                if (line.endsWith("*/") && inMultilineComment) {
                    if (commentStartOffset != -1) {
                        if (currentFold == null) {
                            currentFold = new Fold(FoldType.COMMENT, textArea,
                                    commentStartOffset);
                            currentFold.setEndOffset(textArea.getLineEndOffset(i) - 1);
                            folds.add(currentFold);
                            currentFold = null;
                        } else {
                            currentFold = currentFold.createChild(FoldType.COMMENT,
                                    commentStartOffset);
                            currentFold.setEndOffset(textArea.getLineEndOffset(i) - 1);
                            currentFold = currentFold.getParent();
                        }
                    }
                    commentStartOffset = -1;
                    inMultilineComment = false;
                }

                //handle //{ and //}
                if (!inMultilineComment && line.startsWith("//") &&
                        line.endsWith("{")) {
                    if (currentFold == null) {
                        currentFold = new Fold(FoldType.CODE, textArea,
                                textArea.getLineStartOffset(i));
                        folds.add(currentFold);
                    } else {
                        currentFold = currentFold.createChild(FoldType.CODE,
                                textArea.getLineStartOffset(i));
                    }
                }
                if (!inMultilineComment && line.startsWith("//}")) {
                    if (currentFold != null) {
                        currentFold.setEndOffset(textArea.getLineEndOffset(i) - 1);
                        currentFold = currentFold.getParent();
                    }
                }

            }
            if (currentFold != null &&
                    currentFold.getEndOffset() == Integer.MAX_VALUE) {
                for (int i = 0; i < currentFold.getChildCount(); i++) {
                    folds.add(currentFold.getChild(i));
                }
                folds.remove(currentFold);
            }
        } catch (BadLocationException ex) {
            //should not happen
            ex.printStackTrace();
        }
        return folds;
    }
}
