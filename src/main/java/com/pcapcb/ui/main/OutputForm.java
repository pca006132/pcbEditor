package com.pcapcb.ui.main;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Created by pca006132 on 2016/5/10.
 */
public class OutputForm extends JFrame {
    JTextArea textArea;
    String[] output;
    int i = 0;
    public OutputForm(String[] outputs) {
        output = outputs;
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        JScrollPane pane = new JScrollPane(textArea);
        pane.setPreferredSize(new Dimension(450,200));
        //pane.setBorder(new EmptyBorder(10,10,10,10));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        JButton buttonCopy = new JButton("复制");
        buttonCopy.addActionListener((l) -> copy());
        JButton buttonCopyAll = new JButton("复制所有");
        buttonCopyAll.addActionListener((l) -> copyAll());
        panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        panel.add(Box.createHorizontalGlue());
        panel.add(buttonCopy);


        Container container = getContentPane();
        container.add(pane, BorderLayout.CENTER);
        container.add(panel, BorderLayout.PAGE_END);

        if (output.length > 1) {
            setTitle("输出(多于一条OOC)");
            panel.add(Box.createRigidArea(new Dimension(10, 0)));
            panel.add(buttonCopyAll);
        } else
            setTitle("输出");

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        URL iconURL = getClass().getResource("Command_Block.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        setSize(500,300);
        pack();
        copy();
        setLocationRelativeTo(null);
    }
    private void copy() {
        StringSelection selection = new StringSelection(textArea.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection , selection);
        if (i < output.length) {
            textArea.setText(output[i]);
            i++;
        } else {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }
    private void copyAll() {
        StringBuilder sb = new StringBuilder();
        for (String ooc : output) {
            sb.append(ooc);
            sb.append(System.lineSeparator());
            sb.append(System.lineSeparator());
        }
        StringSelection selection = new StringSelection(sb.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection , selection);
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
