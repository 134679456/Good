package exercise.houserent;

import exercise.houserent.view.HouseView;
// 采用分层模式进行设计
public class HouseRentApp {
    public static void main(String[] args) {
        new HouseView().mainMenu();
        System.out.println("====你退出房屋出租系统====");
    }
}