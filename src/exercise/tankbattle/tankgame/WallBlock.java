package exercise.tankbattle.tankgame;

public class WallBlock {
    public int x;
    public int y;
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;
    public boolean isLive = true;

    public WallBlock(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
