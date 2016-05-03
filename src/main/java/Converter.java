import java.io.*;
import java.util.Scanner;

import PcbFormat.*;


/**
 * Created by pca006132 on 2016/5/3.
 */
public class Converter {
    public static void main(String[] ars) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        System.out.println("文件地址");
        String path = scanner.nextLine();
        System.out.println("设置");
        PcbSettings setting = null;
        try {
            setting = new PcbSettings(scanner.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("输入参数数值错误");
            System.exit(0);
        }
        catch (IllegalArgumentException ex) {
            System.out.println("输入参数错误");
            ex.printStackTrace();
            System.exit(0);
        }
        try (BufferedReader br = new BufferedReader(new FileReader(path));) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
        if (sb.length() != 0) {
            OOC ooc = new OOC();
            String[] commands = null;
            try {
                commands = ooc.getOOC(sb.toString(), setting.getChain());
            } catch (PcbParseException ex) {
                System.out.println(ex.getMessage());
                System.exit(0);
            } catch (IllegalArgumentException ex) {
                System.out.println("输入OOC参数数值错误");
                System.exit(0);
            }

            System.out.println(ooc.checkForCondDir());
            System.out.println("输出OOC到output.txt");
            File newfile = new File("output.txt");
            if (!newfile.exists())
                try {
                    newfile.createNewFile();
                } catch (IOException ex) {
                    System.out.println("cannot create file");
                    ex.printStackTrace();
                    System.exit(0);
                }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
                for (String command : commands) {
                    bw.write(command);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.exit(0);
            }
            System.out.println("success");
            scanner.close();
        }
    }
}
