package exercise.tankbattle.tankgame;

public class Shot implements Runnable{
    int x;
    int y;
    int direct = 0;
    int speed = 6;
    boolean isLive = true;

    public Shot(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }

    @Override
    public void run() {
        while (true) {
            // 休眠50毫秒
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.getStackTrace();
            }
            // 根据方向来改变方向
            switch (direct) {
                case 0:
                    y-=speed;
                    break;
                case 1:
                    x+=speed;
                    break;
                case 2:
                    y+=speed;
                    break;
                case 3:
                    x-=speed;
                    break;
            }
            // 当子弹移动到面板的边界时，就应该销毁（把启动的子弹线程销毁）
            if(!(x>=0 && x<=1000 && y>=0 && y<=800 && isLive)){
                isLive = false;
                break;
            }
        }
    }
}
