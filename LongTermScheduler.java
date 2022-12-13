import java.io.IOException;

public class LongTermScheduler extends Scheduler {
    private static boolean isFinished;
    private final ProcessQueue jobQueue;
    private final ProcessQueue readyQueue;

    public LongTermScheduler(ProcessQueue jobQueue, ProcessQueue readyQueue, TimeManager timeManager, Logger logger) {
        this.jobQueue = jobQueue;
        this.readyQueue = readyQueue;
        isFinished = false;
        this.timeManager = timeManager;
        this.logger = logger;
    }

    public static boolean isFinished() {
        return isFinished;
    }

    @Override
    public void run() {
        while (!jobQueue.isEmpty()) {
            ProcessControlBlock process = jobQueue.getNextProcess(SortingCriteria.BASED_ON_PROCESS_ARRIVAL_TIME);


            timeManager.waitForLongTermScheduler();

            if (process.getProcessArrivalTime() == timeManager.getCurrentTimeStamp()) {
                jobQueue.remove(process);
                logRemovedProcessFromJobQueue(process);

                readyQueue.add(process);
                logAddedProcessToReadyQueue(process);
            }

            timeManager.signalForCPUScheduler();
            timeManager.tick();
        }

        isFinished = true;
    }

    public void logRemovedProcessFromJobQueue(ProcessControlBlock process) {
        try {
            logger.logRemovedProcessFromJobQueue(timeManager.getCurrentTimeStamp(), process);
        } catch (IOException e) {

        }
    }

    public void logAddedProcessToReadyQueue(ProcessControlBlock process) {
        try {
            logger.logAddedProcessToReadyQueue(timeManager.getCurrentTimeStamp(), process);
        } catch (IOException e) {

        }
    }
}
