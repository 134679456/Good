package exercise.houserent.service;

import exercise.houserent.domain.House;

/**
 * //业务层
 * //定义House[]，保存House对象
 * 1、响应HouseView的调用
 * 2、完成对房屋信息的各种操作
 */
public class HouseService {
    private House[] houses;
    private int houseNum = 0;   // 记录当前有多少个房屋
    private int idCounter = 1;  // 记录当前id增长到哪个值

    public HouseService(int n) {
        houses = new House[n];
    }

    public House[] list() {
        return houses;
    }

    public boolean add(House newHouses) {
        if (houseNum == houses.length) {
//            System.out.println("没有空房子了");
//            return false;
//            扩容版
            House[] houses1 = new House[houses.length + 1];
            for (int i = 0; i < houses.length; i++) {
                houses1[i] = houses[i];
            }
            houses1[houseNum++] = newHouses;
            newHouses.setId(idCounter++);
            houses = houses1;
            return true;
        }
        houses[houseNum++] = newHouses;
        newHouses.setId(idCounter++);
        return true;
    }
    public boolean del(int delID) {
        int index = -1;
        for(int i = 0;i < houseNum;i++) {
            if(delID == houses[i].getId()) {
                index = i;
            }
        }
        if(index == -1) {
            return false;
        }
        for(int i = index;i < houseNum -1;i++) {
            houses[i] = houses[i+1];
        }
//        houses[houseNum-1] = null;
//        houseNum--;
//        两句可合为一句
        houses[--houseNum] = null;
        return true;
    }
    public House find(int findID) {
        for(int i =0;i< houseNum;i++) {
            if(findID == houses[i].getId()) {
                return houses[i];
            }
        }
        return null;
    }
}
