package com.pcapcb.ui.main;

import com.jtattoo.plaf.fast.FastLookAndFeel;
import com.pcapcb.pcb.format.CBChain;
import com.pcapcb.pcb.format.PcbParseException;
import com.pcapcb.pcb.format.PcbSettings;
import com.pcapcb.pcb.format.PcbToOOC;
import com.pcapcb.ui.editor.TextEditor;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * Created by pca006132 on 2016/5/10.
 */
public class EditorForm extends JFrame {
    TextEditor editor;
    JMenuBar menuBar;

    public EditorForm() {
        URL iconURL = getClass().getResource("Command_Block.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        JPanel cp = new JPanel(new BorderLayout());
        editor = new TextEditor();
        menuBar = new JMenuBar();
        initialize_menu();
        cp.add(new RTextScrollPane(editor));
        setJMenuBar(menuBar);
        setContentPane(cp);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("懒癌卫士");
        pack();
        setLocationRelativeTo(null);
    }

    private void initialize_menu() {
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        JMenu menuFiles = new JMenu("档案");
        //read file
        JMenuItem menuReadFile = new JMenuItem("开启档案");
        menuReadFile.addActionListener((l) -> readTXTFile());
        menuFiles.add(menuReadFile);

        //save file
        JMenuItem menuSaveFile = new JMenuItem("储存档案");
        KeyStroke ctrlS = KeyStroke.getKeyStroke("control S");
        menuSaveFile.setAccelerator(ctrlS);
        menuSaveFile.addActionListener((l) -> saveFile());
        menuFiles.add(menuSaveFile);

        //generate ooc
        JMenuItem menuGenerate = new JMenuItem("生成");
        //ctrl+g for window, command+g for osx
        KeyStroke ctrlG = KeyStroke.getKeyStroke("control G");
        menuGenerate.setAccelerator(ctrlG);
        menuGenerate.addActionListener((l) -> generate_click());

        menuBar.add(menuFiles);
        menuBar.add(menuGenerate);
    }

    private void generate_click() {
        String settingStr = null;
        String[] commands;
        settingStr = JOptionPane.showInputDialog(this, "setting cmd:");
        if (settingStr == null)
            return;
        String cmd = editor.getSelectedText();
        if (cmd.length() == 0)
            cmd = editor.getText();
        try {
            commands = generateOOC(cmd, settingStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,"输入数值错误");
            return;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,"输入参数错误");
            ex.printStackTrace();
            return;
        } catch (PcbParseException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        }
        OutputForm outputForm = new OutputForm(commands);
        SwingUtilities.invokeLater(() -> {outputForm.setVisible(true);});
    }

    private void readTXTFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile()));
        fileChooser.showOpenDialog(this);
        if (fileChooser.getSelectedFile() == null)
            return;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new FileReader(fileChooser.getSelectedFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,"开启档案时遇上错误");
            return;
        }
        editor.setText(sb.toString());
    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile()));
        fileChooser.showSaveDialog(this);
        if (fileChooser.getSelectedFile() == null)
            return;
        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()))) {
            bw.write(editor.getText());
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,"储存档案时遇上错误");
            return;
        }
    }

    private String[] generateOOC(String pcb, String settingStr) throws
            IllegalArgumentException, PcbParseException{
        CBChain setting = null;
        setting = PcbSettings.getChain(settingStr);

        PcbToOOC PcbToOOC = new PcbToOOC();
        String[] commands = null;
        commands = PcbToOOC.getOOC(pcb, setting);
        String warning = PcbToOOC.checkForCondDir();
        //print warning(change dir when cond)
        if (warning.length() > 0)
            JOptionPane.showMessageDialog(this, warning);
        return commands;
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
        SwingUtilities.invokeLater(() -> {new EditorForm().setVisible(true);});
    }
}
