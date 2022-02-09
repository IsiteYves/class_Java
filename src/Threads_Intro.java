import java.io.IOException;

class RunnableDemo implements Runnable {
    Thread t;
    String threadName;
    public RunnableDemo(String name) {
        threadName=name;
    }
    @Override
    public void run() {
        for(int i=0;i<10;i++) {
            System.out.println("Thread: "+i+" is: "+threadName);
        }
    }
    public void execute() {
        if(t==null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
}

public class Threads_Intro {
    public static void main(String[] args) throws IOException {
        RunnableDemo thread1=new RunnableDemo("First Thr.");
        RunnableDemo thread2=new RunnableDemo("Second Thr.");
        thread2.execute();
        thread1.execute();
    }
}
