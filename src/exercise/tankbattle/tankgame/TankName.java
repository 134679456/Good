package exercise.tankbattle.tankgame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

@SuppressWarnings({"all"})
public class TankName extends JFrame {
    // 定义MyPanel
    private MyPanel myPanel = null;
    Scanner sc = new Scanner(System.in);

    public TankName() {
        System.out.println("请输入选择\n1：新游戏\n2：继续上局");
        int key = sc.nextInt();
        myPanel = new MyPanel(key);
        new Thread(myPanel).start();
        this.add(myPanel);
        this.setSize(1300, 856);
        this.addKeyListener(myPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // 在JFrame 中增加相应关闭窗口的处理
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Recorder.keepRecord();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        TankName tankName01 = new TankName();
    }
}
