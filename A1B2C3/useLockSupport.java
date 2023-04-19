package A1B2C3;

import java.util.concurrent.locks.LockSupport;

/**
 *两个线程，一个线程打印数字123456，一个线程打印字母ABCDEF,交替打印出1A2B3C...
 *
 * 1.t1线程，打印数字1，然后唤醒t2线程，
 * 2.t1线程自己睡眠
 * 3.t2线程打印字母，A，然后唤醒t1线程
 *
 * 用LockSupport.unpark() LockSupport.park()的方案来实现，t1、t2 线程必须相互持有
 * 输出：A1B2C3D4E5F6G
 * 这个方案，就是两个线程互相持有，线程的频繁挂起，阻塞，比较消耗系统资源。因为这里其实只是少量的打印，其实看不出什么影响
 */

public class useLockSupport {
    public static void main(String[] args) throws InterruptedException {
        String s1 = "123456";
        String s2 = "ABCDEFG";
        MyRunnable myRunnable1 = new MyRunnable();
        MyRunnable myRunnable2 = new MyRunnable();
        Thread t1 = new Thread(myRunnable1);
        Thread t2 = new Thread(myRunnable2);
        myRunnable1.setS(s1);
        myRunnable2.setS(s2);
        myRunnable1.setOther(t2);
        myRunnable2.setOther(t1);

        t2.start();
        Thread.sleep(10);
        t1.start();
    }
}
class MyRunnable implements Runnable {
    Thread otherThread;

    private String s;

    public void setS(String s) {
        this.s = s;
    }

    public void setOther (Thread other) {
        this.otherThread = other;
    }
    @Override
    public void run() {
        for (int i = 0; i < s.length(); i++) {
            System.out.print(s.charAt(i));
            //频繁的加锁解锁，影响性能，但是这个题目影响比较小
            LockSupport.unpark(otherThread);
            LockSupport.park();
        }
    }
}
