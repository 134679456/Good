package exercise.tankbattle.tankgame;

import java.util.Vector;

public class EnemyTank extends Tank implements Runnable{
    Vector<Shot> shots = new Vector<>();
    Vector<EnemyTank> enemyTanks = new Vector<>();
    private long lastShotTime = 0; // 记录上次发射时间
    private static final int SHOT_INTERVAL = 1500; // 发射间隔1500毫秒
    private MyPanel panel = null;
    public EnemyTank(int x, int y,MyPanel panel) {
        super(x, y);
        this.panel = panel;
    }
    // 提供set方法可以将MyPanel的Vector<EnemyTank> enemyTanks = new Vector<>();
    // 设置到 EnemyTank 类的enemyTanks 成员
    public void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        this.enemyTanks = enemyTanks;
    }
    public boolean judge(int x1,int y1,int x2,int y2) {
        // 返回true即为发生碰撞
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            if(enemyTank!=this) {
                // 如果另一坦克的方向为向上或向下
                if(enemyTank.getDirect() == 0 || enemyTank.getDirect() == 2) {
                    // 判断当前坦克的第一个点
                    if(x1 >= enemyTank.getX() &&
                            x1 <= enemyTank.getX() + 40 &&
                            y1 >= enemyTank.getY() &&
                            y1 <= enemyTank.getY() + 60) {
                        return true;
                    }
                    // 判断当前坦克的第二个点
                    if(x2 >= enemyTank.getX() &&
                            x2 <= enemyTank.getX() + 40 &&
                            y2 >= enemyTank.getY() &&
                            y2 <= enemyTank.getY() + 60) {
                        return true;
                    }
                }
                // 如果另一个坦克的方向为向左或向右
                if(enemyTank.getDirect() == 1 || enemyTank.getDirect() == 3) {
                    // 判断当前坦克的第一个点
                    if(x1 >= enemyTank.getX() &&
                            x1 <= enemyTank.getX() + 60 &&
                            y1 >= enemyTank.getY() &&
                            y1 <= enemyTank.getY() + 40) {
                        return true;
                    }
                    // 判断当前坦克的第二个点
                    if(x2 >= enemyTank.getX() &&
                            x2 <= enemyTank.getX() + 60 &&
                            y2 >= enemyTank.getY() &&
                            y2 <= enemyTank.getY() + 40) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean isTouchEnemyTank() {
        switch (this.getDirect()) {
            case 0:
                boolean judge = judge(this.getX(), this.getY(), this.getX() + 40, this.getY());
                if(judge) return true;
                break;
            case 1:
                boolean judge1 = judge(this.getX() + 60, this.getY(), this.getX() + 60, this.getY() + 40);
                if(judge1) return true;
                break;
            case 2:
                boolean judge2 = judge(this.getX(), this.getY() + 60, this.getX() + 40, this.getY() + 60);
                if(judge2) return true;
                break;
            case 3:
                boolean judge3 = judge(this.getX(), this.getY(), this.getX(), this.getY() + 40);
                if(judge3) return true;
                break;
        }
        return false;
    }
    public void shotAgain() {
        // 非阻塞检查：在shotAgain()中使用当前时间检查，而非Thread.sleep()，避免阻塞移动线程。
        if (isLive && shots.size() < 3) {
            long currentTime = System.currentTimeMillis();
            // 检查时间间隔是否足够
            if (currentTime - lastShotTime >= SHOT_INTERVAL) {
                Shot shot = null;
                switch (getDirect()) {
                    case 0:
                        shot = new Shot(getX() + 20, getY(), 0);
                        break;
                    case 1:
                        shot = new Shot(getX() + 60, getY() + 20, 1);
                        break;
                    case 2:
                        shot = new Shot(getX() + 20, getY() + 60, 2);
                        break;
                    case 3:
                        shot = new Shot(getX(), getY() + 20, 3);
                        break;
                }
                if (shot != null) {
                    shots.add(shot);
                    new Thread(shot).start();
                    lastShotTime = currentTime; // 更新发射时间
                }
            }
        }
    }

    @Override
    public void run() {
        int borderChangeDirect = -1;
        while (true) {
            // 根据坦克的方向来继续移动
            switch (getDirect()) {
                case 0:
                    for (int i = 0; i < 50; i++) {
                        if(getY() > 0 && !isTouchEnemyTank() && !panel.isCollideWithWalls(getX(),getY(),getDirect())) {
                            moveUp();
                        }
                        // 坦克遇到边界就改变方向
                        else {
                            borderChangeDirect = (int)(Math.random() * 4);
                            break;
                        }
                        shotAgain();
                        // 休眠50毫秒
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.getStackTrace();
                        }
                    }
                    break;
                case 1:
                    for (int i = 0; i < 50; i++) {
                        if(getX() + 60 < 1000 && !isTouchEnemyTank() && !panel.isCollideWithWalls(getX(),getY(),getDirect())) {
                            moveRight();
                        }
                        else {
                            borderChangeDirect = (int)(Math.random() * 4);
                            break;
                        }
                        shotAgain();

                        // 休眠50毫秒
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.getStackTrace();
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i < 50; i++) {
                        if(getY() + 60 < 800 && !isTouchEnemyTank() && !panel.isCollideWithWalls(getX(),getY(),getDirect())) {
                            moveDown();
                        }
                        else  {
                            borderChangeDirect = (int)(Math.random() * 4);
                            break;
                        }
                        shotAgain();

                        // 休眠50毫秒
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.getStackTrace();
                        }
                    }
                    break;
                case 3:
                    for (int i = 0; i < 50; i++) {
                        if(getX() > 0 && !isTouchEnemyTank() && !panel.isCollideWithWalls(getX(),getY(),getDirect())) {
                            moveLeft();
                        }
                        else {
                            borderChangeDirect = (int)(Math.random() * 4);
                            break;
                        }
                        shotAgain();
                        // 休眠50毫秒
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.getStackTrace();
                        }
                    }
                    break;
            }
            if(borderChangeDirect != -1) {
                setDirect(borderChangeDirect);
                borderChangeDirect = -1;
            } else {
                // 然后随机改变坦克方向
                setDirect((int) (Math.random() * 4));
            }
            // 退出进程条件
            if(!isLive) {
                break;
            }
        }
    }
}
