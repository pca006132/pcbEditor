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
import java.util.ResourceBundle;

/**
 * Created by pca006132 on 2016/5/10.
 */
public class EditorForm extends JFrame {
    ResourceBundle myResources;
    TextEditor editor;
    JMenuBar menuBar;

    public EditorForm(ResourceBundle resourceBundle) {
        myResources = resourceBundle;
        URL iconURL = getClass().getResource("Command_Block.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());

        JPanel cp = new JPanel(new BorderLayout());
        editor = new TextEditor();
        initialize_menu();
        cp.add(new RTextScrollPane(editor));
        setContentPane(cp);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(myResources.getString("ui.name"));
        pack();
        setLocationRelativeTo(null);
    }

    private void initialize_menu() {
        menuBar = new JMenuBar();
        menuBar.setMinimumSize(new Dimension(50,0));
        setJMenuBar(menuBar);

        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        menuBar.setLayout(flowLayout);

        JMenu menuFiles = new JMenu(myResources.getString("ui.menu.file"));
        //read file
        JMenuItem menuReadFile = new JMenuItem(myResources.getString("ui.menu.file.openfile"));
        menuReadFile.addActionListener((l) -> readTXTFile());
        menuFiles.add(menuReadFile);

        //save file
        JMenuItem menuSaveFile = new JMenuItem(myResources.getString("ui.menu.file.savefile"));
        KeyStroke ctrlS = KeyStroke.getKeyStroke("control S");
        menuSaveFile.setAccelerator(ctrlS);
        menuSaveFile.addActionListener((l) -> saveFile());
        menuFiles.add(menuSaveFile);

        //generate ooc
        JMenuItem menuGenerate = new JMenuItem(myResources.getString("ui.generate"));
        //ctrl+g for window, command+g for osx
        KeyStroke ctrlG = KeyStroke.getKeyStroke("control G");
        menuGenerate.setAccelerator(ctrlG);
        menuGenerate.addActionListener((l) -> {
            SwingUtilities.invokeLater(() ->generate_click());
        });

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
        if (editor.getSelectionStart() == editor.getSelectionEnd())
            cmd = editor.getText();
        try {
            commands = generateOOC(cmd, settingStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,myResources.getString("error.number"));
            return;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,myResources.getString("error.para"));
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
            JOptionPane.showMessageDialog(this,myResources.getString("error.io.in"));
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
            JOptionPane.showMessageDialog(this,myResources.getString("error.io.out"));
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
        ResourceBundle resourceBundle  = ResourceBundle.getBundle("com.pcapcb.ui.main.pcb");
        
        String font_15 = String.format("%s PLAIN 15", resourceBundle.getString("ui.font"));
        String font_13 = String.format("%s PLAIN 13", resourceBundle.getString("ui.font"));
        
        //set properties of look and feel
        Properties props = new Properties();
        props.put("logoString", "");
        props.put("controlTextFont",font_15);
        props.put("systemTextFont",font_15);
        props.put("userTextFont",font_15);
        props.put("menuTextFont",font_15);
        props.put("windowTitleFont",font_13);
        props.put("subTextFont",font_15);
        props.put("windowTitleBackgroundColor","80 170 230");
        props.put("windowBorderColor","80 170 230");
        props.put("windowInactiveTitleBackgroundColor","170 170 170");
        props.put("windowInactiveBorderColor","170 170 170");
        props.put("menuSelectionBackgroundColor","200 225 242");
        props.put("buttonBackgroundColor","80 170 230");
        props.put("textAntiAliasing","on");

        FastLookAndFeel.setCurrentTheme(props);
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        SwingUtilities.invokeLater(() -> {new EditorForm(resourceBundle).setVisible(true);});
    }
}
