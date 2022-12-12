import java.io.IOException;

public class LongTermScheduler extends Scheduler{
    private ProcessQueue jobQueue;
    private ProcessQueue readyQueue;
    private static boolean isFinished;

    public LongTermScheduler(ProcessQueue jobQueue, ProcessQueue readyQueue, TimeManager timeManager, Logger logger){
        this.jobQueue = jobQueue;
        this.readyQueue = readyQueue;
        this.isFinished = false;
        this.timeManager = timeManager;
        this.logger = logger;
    }
    @Override
    public void run() {
        while(!jobQueue.isEmpty()){
            ProcessControlBlock process = jobQueue.getNextProcess(SortingCriteria.BASED_ON_PROCESS_ARRIVAL_TIME);

            try {
                timeManager.waitForLongTermScheduler();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(process.getProcessArrivalTime() == timeManager.getCurrentTimeStamp()){
                jobQueue.remove(process);
                try {
                    logger.logRemovedProcessFromJobQueue(timeManager.getCurrentTimeStamp(), process);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                readyQueue.add(process);
                try {
                    logger.logAddedProcessToReadyQueue(timeManager.getCurrentTimeStamp(), process);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            timeManager.signalForCPUScheduler();
            timeManager.tick();
        }

        isFinished = true;
    }

    public static boolean isFinished(){
        return isFinished;
    }
}
