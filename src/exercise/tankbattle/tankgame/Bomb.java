package exercise.tankbattle.tankgame;

public class Bomb {
    int x,y;  // 炸弹的坐标
    int life = 9; // 炸弹的生命周期
    boolean isLive = true; // 是否还存活

    public Bomb(int x,int y) {
        this.x = x;
        this.y = y;
    }

    public void lifeDown() {  // 配合出现图片的爆炸效果
        if(life > 0) {
            life--;
        } else {
            isLive = false;
        }
    }
}
