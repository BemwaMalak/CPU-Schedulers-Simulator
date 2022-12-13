import java.io.IOException;

public class Dispatcher {
    private final Logger logger;
    private final int contextSwitchingCost;
    private ProcessControlBlock currentProcess;
    private ProcessControlBlock switchingToProcess;
    private int elapsedContextSwitchTime;
    private boolean startedSwitching;


    public Dispatcher(int contextSwitchingCost, Logger logger) {
        this.currentProcess = null;
        this.switchingToProcess = null;
        this.contextSwitchingCost = contextSwitchingCost;
        this.elapsedContextSwitchTime = 0;
        this.startedSwitching = false;
        this.logger = logger;
    }

    public void doSwitching() {
        elapsedContextSwitchTime = 0;

        startedSwitching = false;

        currentProcess = switchingToProcess;

        switchingToProcess = null;
    }

    public void incrementElapsedTime() {
        elapsedContextSwitchTime++;
    }

    public boolean checkIfSwitchingIsRequired(ProcessControlBlock candidateProcess) {
        if (currentProcess != null && currentProcess != candidateProcess && (switchingToProcess == null || switchingToProcess != candidateProcess)) {
            startedSwitching = true;
            switchingToProcess = candidateProcess;
            return true;
        }
        return false;
    }

    public void logFinishedContextSwitching(int currentTimeStamp) {
        try {
            logger.logFinishedContextSwitching(currentTimeStamp, this);
        } catch (IOException e) {

        }
    }

    public void logStartedContextSwitching(int currentTimeStamp) {
        try {
            logger.logStartedContextSwitching(currentTimeStamp, this);
        } catch (IOException e) {

        }
    }

    public boolean isFinished() {
        return (elapsedContextSwitchTime == contextSwitchingCost && startedSwitching);
    }

    public boolean isStarted() {
        return (startedSwitching);
    }

    public ProcessControlBlock getCurrentProcess() {
        return this.currentProcess;
    }

    public void setCurrentProcess(ProcessControlBlock currentProcess) {
        this.currentProcess = currentProcess;
    }

    public ProcessControlBlock getSwitchingToProcess() {
        return this.switchingToProcess;
    }

    public void setSwitchingToProcess(ProcessControlBlock switchingToProcess) {
        this.switchingToProcess = switchingToProcess;
    }


}
