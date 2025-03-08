package exercise.small_change_sys;
//使用Java 开发 零钱通项目 , 可以完成收益入账，消费，查看明细，退出系统等功能
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class SmallChangeSys_Procedure {
    public static void main(String[] args) {
        boolean loop = true;
        int key = 0;
        Scanner scanner = new Scanner(System.in);
        // 2、完成零钱通明细
        String details = "----------------零钱通明细----------------";// 后面的收益和消费直接拼接该字符串
        // 3、完成收益入账
        double money = 0;
        double balance = 0;  // 余额
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");  // 用于日期格式化
        // 4、消费
        String note = "";
        // 1、完成零钱通菜单
        do {
            System.out.println("\n----------------零钱通菜单----------------");
            System.out.println("\t\t\t1 零钱通明细");
            System.out.println("\t\t\t2 收益入账");
            System.out.println("\t\t\t3 消费");
            System.out.println("\t\t\t4 退    出");
            System.out.print("请选择(1-4)：");
            key = scanner.nextInt();
            switch (key){
                case 1:
                    System.out.println(details);
                    break;
                case 2:
                    System.out.print("收益入账：");
                    money = scanner.nextDouble();
                    // 金额校验：过关斩将校验方式，建议找不正确金额的调条件，然后break(就像一道关卡，不过就不能继续执行，编程思想)
                    if(money <= 0) {
                        System.out.println("收益入账金额需要大于0");
                        break;
                    }
                    balance += money;
                    date = new Date();// 获取当前日期
                    details += "\n收益入账\t+" + money + "\t" + simpleDateFormat.format(date) + "\t" + balance;
                    break;
                case 3:
                    System.out.print("消费金额：");
                    money = scanner.nextDouble();
                    if(money <= 0 || money > balance) {
                        System.out.println("你的消费金额应该在0-" + balance);
                        break;
                    }
                    System.out.print("消费说明：");
                    note = scanner.next();
                    balance -= money;
                    date = new Date();// 获取当前日期
                    details += "\n" + note + "\t-" + money + "\t" + simpleDateFormat.format(date) + "\t" + balance;
                    break;
                case 4:
                    String choice = "";
                    // 编程思想：一个代码块完成一个功能
                    while (true) {
                        System.out.println("你确定要退出吗? y/n");
                        choice = scanner.next();
                        if("y".equals(choice) || "n".equals(choice)){
                            break;
                        } else {
                            System.out.println("输入错误，请重新选择");
                        }
                    }

                    if("y".equals(choice)){
                        loop = false;
                    }
                    break;
                default:
                    System.out.println("输入有误，请重新输入");
            }
        } while(loop);
        System.out.println("-------------退出了零钱通项目-------------");
    }
}
