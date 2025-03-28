package exercise.tankbattle.tankgame;

import java.util.Vector;

/**
 * 自己的坦克
 */
public class HeroTank extends Tank {
    Shot shot = null;
    Vector<Shot> shots = new Vector<>();
    public HeroTank(int x, int y) {
        super(x, y);
    }
    // 射击
    public void shotEnemyTank() {
        // 根据当前heroTank的位置和方向来创建Shot
        if (!isLive /*|| shots.size() >= 5 可增加使被坦克只能射五发子弹*/) {
            return;
        }
        switch (getDirect()) {
            case 0:
                shot = new Shot(getX() + 20,getY(),0);
                shots.add(shot);
                // 启动子弹线程
                new Thread(shot).start();
                break;
            case 2:
                shot = new Shot(getX() + 20,getY() +60,2);
                shots.add(shot);
                // 启动子弹线程
                new Thread(shot).start();
                break;
            case 1:
                shot = new Shot(getX() + 60,getY() +20,1);
                shots.add(shot);
                // 启动子弹线程
                new Thread(shot).start();
                break;
            case 3:
                shot = new Shot(getX(),getY() +20,3);
                shots.add(shot);
                // 启动子弹线程
                new Thread(shot).start();
                break;
        }
    }
}
