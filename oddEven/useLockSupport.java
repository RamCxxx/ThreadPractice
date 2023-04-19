package oddEven;

import java.util.concurrent.locks.LockSupport;

public class useLockSupport {
    static Thread A;
    static Thread B;

    public static void main(String[] args) {

        A = new Thread(()->{
            for (int i = 1;i <= 9;i += 2){
                System.out.println(i);
                LockSupport.unpark(B);
                LockSupport.park();
            }
        });

        B = new Thread(()->{
            for (int i = 2;i <= 10;i += 2){
                System.out.println(i);
                LockSupport.unpark(A);
                LockSupport.park();
            }
        });

        //开启线程
        A.start();
        B.start();
    }
}
