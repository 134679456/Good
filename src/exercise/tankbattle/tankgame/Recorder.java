package exercise.tankbattle.tankgame;

import java.io.*;
import java.util.Vector;

/**
 * 记录相关信息，与文件交互
 */
@SuppressWarnings({"all"})
public class Recorder {
    // 记录我方击毁敌人坦克数
    private static int allEnemyTankNum=0;
    //定义IO对象，准备写数据到文件中
    private static BufferedReader br = null;
    private static BufferedWriter bw = null;
//    private static String recordFile = "c:\\demo\\a\\b\\c\\myRecorder.txt";
    // 使用src下的文件，避免权限不够
    private static String recordFile = "src\\myRecorder.txt";
    // 指向MyPanel对象的敌人坦克Vector
    private static Vector<EnemyTank> enemyTanks = null;
    // 定义一个Node的Vector，用于保存敌人信息node
    private static Vector<Node> nodes = new Vector<>();

    public static String getRecordFile() {
        return recordFile;
    }

    public static void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }
    //增加一个方法，用于读取recordFile，恢复相关信息
    public static Vector<Node> getNodesAndEnemyTankRec() {
        try {
            br = new BufferedReader(new FileReader(recordFile));
            allEnemyTankNum = Integer.parseInt(br.readLine());
            // 循环读取文件，生成node集合
            String line = "";
            while ((line = br.readLine())!=null) {
                String[] xyd = line.split(" ");
                Node node = new Node(Integer.parseInt(xyd[0]),Integer.parseInt(xyd[1]),Integer.parseInt(xyd[2]));
                nodes.add(node);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(br!=null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return nodes;
    }

    // 当游戏退出时，将 allEnemyTankNum 保存到 recordFile
    // 保存敌人坦克的坐标和方向
    public static void keepRecord() {
        try {
            bw = new BufferedWriter(new FileWriter(recordFile));
            bw.write(allEnemyTankNum + "\r\n");
//            bw.newLine();
            for (int i = 0; i < enemyTanks.size(); i++) {
                EnemyTank enemyTank = enemyTanks.get(i);
                if(enemyTank.isLive) {
                    String record = enemyTank.getX()+" "+enemyTank.getY()+" " + enemyTank.getDirect();
                    bw.write(record);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bw!=null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getAllEnemyTankNum() {
        return allEnemyTankNum;
    }

    public static void setAllEnemyTankNum(int allEnemyTankNum) {
        Recorder.allEnemyTankNum = allEnemyTankNum;
    }
    //当我方坦克击毁敌方坦克，则应该++
    public static void addAllEnemyTankNum() {
        Recorder.allEnemyTankNum++;
    }
}
