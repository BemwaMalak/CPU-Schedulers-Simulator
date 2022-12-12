import java.util.concurrent.Semaphore;

public class TimeManager {
    private int currentTimeStamp;
    private int ticks;
    private Semaphore longTermScheduler;
    private Semaphore cpuScheduler;

    public TimeManager(){
        this.currentTimeStamp = 0;
        this.ticks = 0;
        this.longTermScheduler = new Semaphore(1);
        this.cpuScheduler = new Semaphore(0);

    }
    public void waitForLongTermScheduler() throws InterruptedException {
        longTermScheduler.acquire();
    }

    public void waitForCPUScheduler() throws InterruptedException {
        cpuScheduler.acquire();
    }
    public void signalForLongTermScheduler(){
        longTermScheduler.release();
    }

    public void signalForCPUScheduler(){
        cpuScheduler.release();
    }

    public void tick(){
        this.ticks++;
        if(ticks == 2){
            increaseTimeBy(1);
            ticks = 0;
        }
    }
    private void increaseTimeBy(int step){
        this.currentTimeStamp += step;
    }
    public int getCurrentTimeStamp(){
        return currentTimeStamp;
    }
}
