package A1B2C3;

/**
 * 用Synchronize+ wait() + notifyAll()
 *
 *  这个方案就是两个线程持有了相同的一个object，对同一个objeck加锁。
 * 这样两个线程就不会互相持有了，只是持有了同样一个加锁对象。
 * 并且Synchronize加锁的效率，是一个锁升级的过程，在争抢不激烈的过程中，应该是一直是自旋锁的一个样子，不会因为调度系统线程资源的而浪费效率。
 * 所以，情况应该是好一些。
 *
 *  输出：1A2B3C4D5E6F
 */
public class useSynchronize {
    public static void main(String[] args) throws InterruptedException {
        String s1 = "123456";
        String s2 = "ABCDEFG";

        Object lock1 = new Object();
        MyRunnable r1 = new MyRunnable(lock1, s1);
        MyRunnable r2 = new MyRunnable(lock1, s2);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
    static class MyRunnable implements Runnable {

        private Object mylock;
        private String s;

        public MyRunnable(Object mylock, String s) {
            this.mylock = mylock;
            this.s = s;
        }

        @Override
        public void run() {
            synchronized (mylock) {
                for (int i = 0; i < s.length(); i++) {
                    System.out.print(s.charAt(i));
                    try {
                        mylock.notifyAll();
                        mylock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
