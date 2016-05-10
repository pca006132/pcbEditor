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
import java.util.Properties;

/**
 * Created by pca006132 on 2016/5/8.
 */
public class TextEditor extends JFrame {
    public TextEditor() {
        JPanel cp = new JPanel(new BorderLayout());

        AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
        atmf.putMapping("text/pcb", "com.pcapcb.ui.editor.PcbTokenMaker");
        FoldParserManager.get().addFoldParserMapping("text/pcb", new PcbFolding());
        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle("text/pcb");

        textArea.setLineWrap(true);
        textArea.setBracketMatchingEnabled(true);
        textArea.setCodeFoldingEnabled(true);
        RTextScrollPane sp = new RTextScrollPane(textArea);
        cp.add(sp);
        setContentPane(cp);

        JMenuBar menuBar = new JMenuBar();
        JMenuItem menuGenerate = new JMenuItem("生成");

        //ctrl+g for window, command+g for osx
        KeyStroke ctrlG = KeyStroke.getKeyStroke("control G");
        menuGenerate.setAccelerator(ctrlG);

        menuGenerate.addActionListener((l) -> {
            String settingStr = null;
            settingStr = JOptionPane.showInputDialog(this, "setting cmd:");
            if (settingStr == null)
                return;

            CBChain setting = null;
            try {
                setting = PcbSettings.getChain(settingStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,"输入参数数值错误");
                return;
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,"输入参数错误");
                ex.printStackTrace();
                return;
            }
            PcbToOOC PcbToOOC = new PcbToOOC();
            String[] commands = null;
            try {
                commands = PcbToOOC.getOOC(textArea.getText(), setting);
            } catch (PcbParseException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                return;
            }
            String warning = PcbToOOC.checkForCondDir();
            //print warning(change dir when cond)
            if (warning.length() > 0)
                JOptionPane.showMessageDialog(this, warning);
            JFrame frame = new JFrame();
            JTextArea textarea = new JTextArea(20,50);
            JScrollPane pane = new JScrollPane(textarea);
            pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            pane.setBorder(new EmptyBorder(10,10,10,10));
            frame.setContentPane(pane);
            frame.setTitle("输出");
            for (String text : commands) {
                textarea.append(text);
                textarea.append("\r\n\r\n");
            }
            frame.setSize(500,300);
            SwingUtilities.invokeLater(() -> {frame.setVisible(true);});
        });
        menuBar.add(menuGenerate);
        setJMenuBar(menuBar);

        setTitle("Fuck this shit i'm out");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }
    public static void main(String[] args) {
        // Start all Swing applications on the EDT.
        Properties props = new Properties();
        props.put("logoString", "pcb");
        FastLookAndFeel.setCurrentTheme(props);
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        SwingUtilities.invokeLater(() -> {new TextEditor().setVisible(true);});
    }
}
