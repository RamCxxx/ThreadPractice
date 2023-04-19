package A1B2C3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock+ Condition + await() + signal()
 * 输出：1A2B3C4D5E6FG
 * ReentrantLock 底层的实现 其实也是LockSupport.park()
 */

public class useReentrantLock {
    public static void main(String[] args) throws InterruptedException {
        String s1 = "123456";
        String s2 = "ABCDEFG";

        Lock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        Runnable r1 = new TestRunable(lock,condition1,condition2,s1);
        Runnable r2 = new TestRunable(lock,condition2,condition1,s2);
        new Thread(r1).start();
        new Thread(r2).start();
    }
    static class TestRunable implements Runnable{
        Lock lock;
        Condition condition;
        Condition otherCondition;
        String s;

        public TestRunable(Lock lock, Condition condition, Condition otherCondition, String s) {
            this.lock = lock;
            this.condition = condition;
            this.otherCondition = otherCondition;
            this.s = s;
        }

        @Override
        public void run() {
            lock.lock();
            for (int i = 0; i < s.length(); i++) {
                System.out.print(s.charAt(i));
                otherCondition.signalAll();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            otherCondition.signalAll();
            lock.unlock();
        }
    }
}
