package exercise.houserent.view;

import exercise.houserent.domain.House;
import exercise.houserent.service.HouseService;
import exercise.houserent.utils.Utility;

/**
 * //界面
 * 1、显示界面
 * 2、接受用户输入
 * 3、调用HouseService完成对房屋信息的各种操作
 */
public class HouseView {
    private boolean loop = true;
    private char key;
    private HouseService houseService = new HouseService(1);

    public void listHouses() {
        System.out.println("=============房屋列表=============");
        System.out.println("编号\t\t房主\t\t电话\t\t地址\t\t月租\t\t状态(未出租/已出租)");
        House[] houses = houseService.list();
        for (int i = 0; i < houses.length; i++) {
            if (houses[i] == null) {
                break;
            }
            System.out.println(houses[i]);
        }
    }

    public void findHouse() {
        System.out.println("=============查找房屋=============");
        System.out.print("请输入你要查找的id：");
        int findID = Utility.readInt();
        House house = houseService.find(findID);
        if (house != null) {
            System.out.println(house);
        } else {
            System.out.println("查找失败，没有该id的房子");
        }
    }

    public void updateHouse() {
        System.out.println("=============修改房屋=============");
        System.out.print("请选择待修改房屋编号(-1退出)：");
        int modifyID = Utility.readInt();
        if(modifyID == -1) {
            System.out.println("\n=============退出修改房屋=============");
            return;
        }
        House house = houseService.find(modifyID);
        if (house == null) {
            System.out.println("=============要修改的房屋编号不存在=============");
            return;
        }
        System.out.print("姓名(" + house.getName() + "): ");
        String name = Utility.readString(20, ""); // 用户如果直接回车表示不修改信息，默认""
        if (!("".equals(name))) {
            house.setName(name);
        }
        System.out.print("电话(" + house.getPhone() + "): ");
        String phone = Utility.readString(20, ""); // 用户如果直接回车表示不修改信息，默认""
        if (!("".equals(phone))) {
            house.setPhone(phone);
        }
        System.out.print("地址(" + house.getAddress() + "): ");
        String address = Utility.readString(20, ""); // 用户如果直接回车表示不修改信息，默认""
        if (!("".equals(address))) {
            house.setAddress(address);
        }
        System.out.print("出租状态(" + house.getState() + "): ");
        String state = Utility.readString(20, ""); // 用户如果直接回车表示不修改信息，默认""
        if (!("".equals(state))) {
            house.setState(state);
        }
        System.out.print("租金(" + house.getRent() + "): ");
        int rent = Utility.readInt(-1);  //用户如果直接回车表示不修改信息，默认-1
        if(rent != -1){
            house.setRent(rent);
        }
    }

    public void delHouses() {
        System.out.println("=============删除房屋信息=============");
        System.out.print("请输入待删除房屋的编号(-1退出)：");
        int delID = Utility.readInt();
        if (delID == -1) {
            System.out.println("=============放弃删除房屋信息=============");
            return;
        }
        // 必须输入Y/N
        char choice = Utility.readConfirmSelection();
        if (choice == 'Y') {
            if (houseService.del(delID)) {
                System.out.println("=============删除房屋信息成功" + "=============");
            } else {
                System.out.println("=============房屋编号不存在，删除房屋信息失败=============");
            }
        } else {
            System.out.println("=============放弃删除房屋信息=============");
        }
    }

    public void addHouses() {
        System.out.println("=============添加房屋=============");
        System.out.print("姓名：");
        String name = Utility.readString(8); //可用ctrl+B查看具体方法
        System.out.print("电话：");
        String phone = Utility.readString(11);
        System.out.print("地址：");
        String address = Utility.readString(10);
        System.out.print("租金：");
        int rent = Utility.readInt();
        System.out.print("出租状态：");
        String state = Utility.readString(3);
        House newHouses = new House(0, name, phone, address, rent, state);
        if (houseService.add(newHouses)) {
            System.out.println("=============添加房屋成功=============");
        } else {
            System.out.println("=============添加房屋失败=============");
        }
    }

    public void exit() {
        char c = Utility.readConfirmSelection();
        if (c == 'Y') {
            loop = false;
        }
    }

    public void mainMenu() {
        do {
            System.out.println("\n=============房屋出租系统=============");
            System.out.println("\t\t\t1 新 增 房 源");
            System.out.println("\t\t\t2 查 找 房 屋");
            System.out.println("\t\t\t3 删 除 房 屋 信 息");
            System.out.println("\t\t\t4 修 改 房 屋 信 息");
            System.out.println("\t\t\t5 房 屋 列 表");
            System.out.println("\t\t\t6 退      出");
            System.out.print("请输入你的选择(1-6): ");
            key = Utility.readChar();
            switch (key) {
                case '1':
                    addHouses();
                    break;
                case '2':
                    findHouse();
                    break;
                case '3':
                    delHouses();
                    break;
                case '4':
                    updateHouse();
                    break;
                case '5':
                    listHouses();
                    break;
                case '6':
                    exit();
                    break;
            }
        } while (loop);
    }
}
