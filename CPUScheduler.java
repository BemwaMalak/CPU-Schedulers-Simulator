import java.io.IOException;

public abstract class CPUScheduler extends Scheduler {

    protected ProcessQueue readyQueue;

    protected abstract void execute();


    protected void logExecutedProcess() {
        try {
            logger.logExecutedProcess(timeManager.getCurrentTimeStamp(), dispatcher.getCurrentProcess());
        } catch (IOException e) {

        }
    }

    protected void logProcessFinished() {
        try {
            logger.logProcessFinished(timeManager.getCurrentTimeStamp(), dispatcher.getCurrentProcess());
        } catch (IOException e) {

        }
    }

    protected void logRemovedProcessFromReadyQueue() {
        try {
            logger.logRemovedProcessFromReadyQueue(timeManager.getCurrentTimeStamp(), dispatcher.getCurrentProcess());
        } catch (IOException e) {

        }
    }

    protected void logSchedulerFinished() {
        try {
            logger.logSchedulerFinished(timeManager.getCurrentTimeStamp());
        } catch (IOException e) {

        }
    }
}
