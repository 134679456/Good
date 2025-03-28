package exercise.tankbattle.tankgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 坦克大战的绘图区域
 */
@SuppressWarnings({"all"})
public class MyPanel extends JPanel implements KeyListener, Runnable {
    HeroTank heroTank = null;
    Vector<EnemyTank> enemyTanks = new Vector<>();  // 线程安全
    int enemyTankSize = 8;
    //定义一个存放Node对象的Vector，用于恢复敌人坦克的坐标与方向
    Vector<Node> nodes = new Vector<>();
    private CopyOnWriteArrayList<WallBlock> walls = new CopyOnWriteArrayList<>();
    // 定义一个Vector，用于存放炸弹
    // 当子弹击中坦克时，加入一个Bomb对象到bombs
    Vector<Bomb> bombs = new Vector<>();
    // 定义三张炸弹图片，用于显示爆炸效果
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;

    public MyPanel(int key) {
        // 先判断记录的文件是否存在
        File file = new File(Recorder.getRecordFile());
        if(file.exists()) {
            nodes = Recorder.getNodesAndEnemyTankRec();
        } else {
            System.out.println("文件不存在，只能开启新游戏");
            key=1;
        }
        Recorder.setEnemyTanks(enemyTanks);
        heroTank = new HeroTank(100, 500); // 初始化自己的坦克
        switch (key) {
            case 1:
                for (int i = 0; i < enemyTankSize; i++) {  // 初始化敌人坦克
                    EnemyTank enemyTank = new EnemyTank((100) * (i + 1), 0,this);
                    enemyTank.setEnemyTanks(enemyTanks);
                    // 设置方向
                    enemyTank.setDirect(2);
                    //启动敌人坦克线程
                    new Thread(enemyTank).start();
                    //给该enemyTank加入一颗子弹
                    Shot shot = new Shot(enemyTank.getX()+20,enemyTank.getY() + 60,enemyTank.getDirect());
                    // 加入enemyTank的Vector成员
                    enemyTank.shots.add(shot);
                    //启动子弹线程
                    new Thread(shot).start();
                    // 加入
                    enemyTanks.add(enemyTank);
                }
                break;
            case 2:
                for (int i = 0; i < nodes.size(); i++) {  // 初始化敌人坦克
                    Node node = nodes.get(i);
                    EnemyTank enemyTank = new EnemyTank(node.getX(), node.getY(),this);
                    enemyTank.setEnemyTanks(enemyTanks);
                    // 设置方向
                    enemyTank.setDirect(node.getDirect());
                    //启动敌人坦克线程
                    new Thread(enemyTank).start();
                    //给该enemyTank加入一颗子弹
                    Shot shot = new Shot(enemyTank.getX()+20,enemyTank.getY() + 60,enemyTank.getDirect());
                    // 加入enemyTank的Vector成员
                    enemyTank.shots.add(shot);
                    //启动子弹线程
                    new Thread(shot).start();
                    // 加入
                    enemyTanks.add(enemyTank);
                }
                break;
            default:
                System.out.println("输入有误");
        }
        // 初始化测试墙壁（创建3行障碍）
        createWall(300, 200, 20, 1);  // 横向墙壁
        createWall(300, 600, 20, 1);  // 横向墙壁
        createWall(400, 150, 30, 0);   // 纵向墙壁
        createWall(600, 150, 30, 0);   // 纵向墙壁
    //初始化图片对象
        image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_1.gif"));
        image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_2.gif"));
        image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bomb_3.gif"));

        //这里，播放指定的音乐
        new AePlayWave("src\\111.wav").start();
    }
    // 新增墙壁创建方法
    private void createWall(int startX, int startY, int count, int direction) {
        for (int i = 0; i < count; i++) {
            if (direction == 0) { // 纵向排列
                walls.add(new WallBlock(startX, startY + i * WallBlock.HEIGHT));
            } else { // 横向排列
                walls.add(new WallBlock(startX + i * WallBlock.WIDTH, startY));
            }
        }
    }
    // 显示我方击毁敌方坦克的数量
    public void showInfo(Graphics g) {
        // 画出玩家的总成绩
        g.setColor(Color.BLACK);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);

        g.drawString("您累计击毁敌方坦克",1020,30);
        drawTank(1020,60,g,0,0);
        g.setColor(Color.BLACK);
        g.drawString(Recorder.getAllEnemyTankNum() + "",1100,98);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 800); // 默认是黑色,为背景
        showInfo(g);
        // 绘制墙壁
        g.setColor(Color.WHITE); // 白色
        for (WallBlock wall : walls) {
            if (wall.isLive) {
                g.fill3DRect(wall.x, wall.y, WallBlock.WIDTH, WallBlock.HEIGHT, true);
            }
        }
        // 增加游戏结束提示
        if (heroTank != null && !heroTank.isLive) {
            g.setColor(Color.RED);
            g.setFont(new Font("宋体", Font.BOLD, 100));
            g.drawString("GAME OVER", 280, 400);
            for (int i = 0; i < enemyTanks.size(); i++) {
                EnemyTank enemyTank = enemyTanks.get(i);
                enemyTank.isLive=false;
            }
        }
        if(enemyTanks.size() == 0) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("宋体", Font.BOLD, 100));
            g.drawString("WINNER", 280, 400);
        }
        if(heroTank!=null && heroTank.isLive) {
            // 画出坦克-封装方法
            drawTank(heroTank.getX(), heroTank.getY(), g, heroTank.getDirect(), 1);
        }
        // 画出hero射出的子弹
        for (int i = 0; i < heroTank.shots.size(); i++) {
            Shot shot = heroTank.shots.get(i);
            if (heroTank.isLive && shot != null && shot.isLive == true) {
                g.draw3DRect(shot.x, shot.y, 2, 2, false);
            } else {
                heroTank.shots.remove(shot);
            }
        }


        // 如果bombs集合中有对象，就画出
        for (int i = 0; i < bombs.size(); i++) {
            //取出炸弹
            Bomb bomb = bombs.get(i);
            // 根据当前这个bomb对象的life值去画出对应的图片
            if(bomb.life > 6) {
                g.drawImage(image1, bomb.x, bomb.y, 60,60,this);
            } else if(bomb.life > 3) {
                g.drawImage(image2,bomb.x, bomb.y, 60,60,this);
            } else {
                g.drawImage(image3, bomb.x, bomb.y, 60,60,this);
            }
            // 让这个炸弹的生命值减少
            // 线程run方法中调用repaint方法，所以会一直调用paint方法
            bomb.lifeDown();
            // 如果bomb.life = 0，就从bombs集合中删除
            if(bomb.life == 0) {
                bombs.remove(bomb);
            }
        }

        // 画出敌人坦克
        for (int i = 0; i < enemyTanks.size(); i++) {
            //取出坦克
            EnemyTank enemyTank = enemyTanks.get(i);
            // 判断当前坦克是否还存活
            if(enemyTank.isLive) {
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 0);
                // 取出enemyTank的所有子弹
                for (int i1 = 0; i1 < enemyTank.shots.size(); i1++) {
                    //取出子弹
                    Shot shot = enemyTank.shots.get(i1);
                    if (shot.isLive == true) {
                        g.draw3DRect(shot.x, shot.y, 2, 2, false);
                    } else {
                        //从Vector中移除
                        enemyTank.shots.remove(shot);
                    }
                }
            }
        }
    }
    // 新增碰撞检测方法
    public void checkBulletHitWall(Shot shot) {
        if (shot == null || !shot.isLive) return;

        for (int i = 0; i < walls.size(); i++) {
            WallBlock wall = walls.get(i);
            if (wall.isLive &&
                    shot.x > wall.x &&
                    shot.x < wall.x + WallBlock.WIDTH &&
                    shot.y > wall.y &&
                    shot.y < wall.y + WallBlock.HEIGHT)
            {
                wall.isLive = false;
                shot.isLive = false;
                walls.remove(wall); // 直接操作集合
                break;
            }
        }
    }
    public boolean judgeWalls(int x1,int y1,int x2,int y2) {
        for (int i = 0; i < walls.size(); i++) {
            WallBlock wall = walls.get(i);
            if(x1 >= wall.x &&
                    x1 <= wall.x + WallBlock.WIDTH &&
                    y1 >= wall.y &&
                    y1 <= wall.y + WallBlock.HEIGHT) {
                return true;
            }
            // 判断当前坦克的第二个点
            if(x2 >= wall.x &&
                    x2 <= wall.x + WallBlock.WIDTH &&
                    y2 >= wall.y &&
                    y2 <= wall.y + WallBlock.HEIGHT) {
                return true;
            }
        }
        return false;
    }
    public boolean isCollideWithWalls(int x, int y, int direct) {
        switch (direct) {
            case 0:
                boolean b = judgeWalls(x, y, x + 40, y);
                if(b) return true;
                break;
            case 1:
                boolean b1 = judgeWalls(x + 60, y, x + 60, y + 40);
                if(b1) return true;
                break;
            case 2:
                boolean b2 = judgeWalls(x, y + 60, x + 40, y + 60);
                if(b2) return true;
                break;
            case 3:
                boolean b3 = judgeWalls(x, y, x, y + 40);
                if(b3) return true;
                break;
        }
        return false;
    }
    /**
     * @param x      坦克的左上角x坐标
     * @param y      坦克的左上角y坐标
     * @param g      画笔
     * @param direct 坦克方向（上下左右）
     * @param type   坦克类型
     */
    public void drawTank(int x, int y, Graphics g, int direct, int type) {
        switch (type) {
            case 0:  // 我们的坦克
                g.setColor(Color.cyan);
                break;
            case 1:  // 敌人的坦克
                g.setColor(Color.yellow);
                break;
        }
        switch (direct) {
            case 0:  // 表示向上
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y, x + 20, y + 30);
                break;
            case 1:  // 表示向右
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x + 60, y + 20);
                break;
            case 2:  // 表示向下
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                g.fillOval(x + 10, y + 20, 20, 20);
                g.drawLine(x + 20, y + 60, x + 20, y + 30);
                break;
            case 3:  // 表示向左
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y + 30, 60, 10, false);
                g.fill3DRect(x + 10, y + 10, 40, 20, false);
                g.fillOval(x + 20, y + 10, 20, 20);
                g.drawLine(x + 30, y + 20, x, y + 20);
                break;
        }
    }
    //判断敌人坦克是否击中我方坦克
    public void hitHero() {
        //遍历所有敌人坦克
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            // 遍历enemyTank 对象的所有子弹
            for (int i1 = 0; i1 < enemyTank.shots.size(); i1++) {
                // 取出子弹
                Shot shot = enemyTank.shots.get(i1);
                // 判断shot是否击中我方坦克
                if(heroTank.isLive && shot.isLive) {
                    hitTank(shot,heroTank);
                }
            }
        }
    }
    public void hitEnemy() {
        // 遍历本坦克子弹
        for (int i = 0; i < heroTank.shots.size(); i++) {
            Shot shot = heroTank.shots.get(i);
            if(shot != null && shot.isLive) { // 当我的子弹还存活
                // 遍历敌人所有的坦克
                for (int j = 0; j < enemyTanks.size(); j++) {
                    EnemyTank enemyTank = enemyTanks.get(j);
                    hitTank(shot,enemyTank);
                }
            }
        }
    }
    // 编写方法，判断我方子弹是否击中敌人坦克,是在子弹重绘的时候，即run方法内
    public void hitTank(Shot shot,Tank tank) {
        switch (tank.getDirect()) {
            case 0:
            case 2:
                if(shot.x > tank.getX() && shot.x < tank.getX()+40
                  && shot.y >tank.getY() && shot.y <tank.getY()+60){
                    shot.isLive = false;
                    tank.isLive = false;
                    //如果子弹击中敌方坦克，则将其移除
                    enemyTanks.remove(tank);
                    if(tank instanceof EnemyTank) {
                        Recorder.addAllEnemyTankNum();
                    }
                    // 创建Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(tank.getX(),tank.getY());
                    bombs.add(bomb);
                }
                break;
            case 1:
            case 3:
                if(shot.x > tank.getX() && shot.x < tank.getX()+60
                        && shot.y >tank.getY() && shot.y <tank.getY()+40){
                    shot.isLive = false;
                    tank.isLive = false;
                    //如果子弹击中敌方坦克，则将其移除
                    enemyTanks.remove(tank);
                    if(tank instanceof EnemyTank) {
                        Recorder.addAllEnemyTankNum();
                    }
                    // 创建Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(tank.getX(),tank.getY());
                    bombs.add(bomb);
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 只在坦克存活时响应移动和射击
        if (heroTank != null && heroTank.isLive) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    heroTank.setDirect(0);
                    if (heroTank.getY() > 0 && !isCollideWithWalls(heroTank.getX(),heroTank.getY(),heroTank.getDirect())) heroTank.moveUp();
                    break;
                case KeyEvent.VK_D:
                    heroTank.setDirect(1);
                    if (heroTank.getX() + 60 < 1000 && !isCollideWithWalls(heroTank.getX(),heroTank.getY(),heroTank.getDirect())) heroTank.moveRight();
                    break;
                case KeyEvent.VK_S:
                    heroTank.setDirect(2);
                    if (heroTank.getY() + 60 < 800 && !isCollideWithWalls(heroTank.getX(),heroTank.getY(),heroTank.getDirect())) heroTank.moveDown();
                    break;
                case KeyEvent.VK_A:
                    heroTank.setDirect(3);
                    if (heroTank.getX() > 0 && !isCollideWithWalls(heroTank.getX(),heroTank.getY(),heroTank.getDirect())) heroTank.moveLeft();
                    break;
                case KeyEvent.VK_J:  // 增加存活检查
                    heroTank.shotEnemyTank();
                    break;
            }
        }
        this.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    // 需要线程来不断重绘子弹
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.getStackTrace();
            }
            // 检测敌方子弹碰撞
//            for (EnemyTank et : enemyTanks) {
//                for (Shot shot : et.shots) {
//                    checkBulletHitWall(shot);
//                }
//            }
            for (int i = 0; i < enemyTanks.size(); i++) {
                EnemyTank enemyTank = enemyTanks.get(i);
                for (int i1 = 0; i1 < enemyTank.shots.size(); i1++) {
                    checkBulletHitWall(enemyTank.shots.get(i1));
                }
            }
            // 检测我方子弹碰撞
//            for (Shot shot : heroTank.shots) {
//                checkBulletHitWall(shot);
//            }
            for (int i = 0; i < heroTank.shots.size(); i++) {
                Shot shot = heroTank.shots.get(i);
                checkBulletHitWall(shot);
            }

            // 判断是否击中了敌人坦克
            hitEnemy();
            //判断是否被敌人击中
            hitHero();
            this.repaint();  // 不断重绘子弹
        }
    }
}
