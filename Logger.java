import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private final FileWriter fileWriter;
    public Logger(String logFileName) throws IOException {
        fileWriter = new FileWriter(logFileName, true);
    }
    public void logSchedulerFinished(int currentTimeStamp) throws IOException {
        fileWriter.write("[Second " + currentTimeStamp + "] " + "Scheduler finished \n");
    }
    public void logProcessFinished(int currentTimeStamp, ProcessControlBlock process) throws IOException {
        fileWriter.write("[Second " + currentTimeStamp + "] " + "Process " + process.getProcessName() + " finished \n");
    }
    public void logExecutedProcess(int currentTimeStamp, ProcessControlBlock process) throws IOException {
        fileWriter.write("[Second " + currentTimeStamp + "] " + "Process " + process.getProcessName() + " executed successfully and has " + process.getProcessBurstTime() + " seconds burst time remaining \n");
    }
    public void logStartedContextSwitching(int currentTimeStamp, Dispatcher dispatcher) throws IOException {
        fileWriter.write("[Second " + currentTimeStamp + "] " + "Process " + dispatcher.getCurrentProcess().getProcessName() + " started context switching with process " + dispatcher.getSwitchingToProcess().getProcessName() + " successfully \n");
    }
    public void logFinishedContextSwitching(int currentTimeStamp, Dispatcher dispatcher) throws IOException {
        fileWriter.write("[Second " + currentTimeStamp + "] " + "Process " + dispatcher.getCurrentProcess().getProcessName() + " finished context switching with process " + dispatcher.getSwitchingToProcess().getProcessName() + " successfully \n");
    }

    public void logProcessFinishedQuantumTime(int currentTimeStamp, ProcessControlBlock process, int quantumTime) throws IOException {
        fileWriter.write("[Second " + currentTimeStamp + "] " + "Process " + process.getProcessName() + " finished quantum time " + quantumTime + " seconds and has " + process.getProcessBurstTime() + " seconds burst time remaining \n");
    }

    public void logRemovedProcessFromJobQueue(int currentTimeStamp, ProcessControlBlock process) throws IOException {
        fileWriter.write("[Second " + currentTimeStamp + "] " + "Process " + process.getProcessName() + " removed from job queue successfully \n");
    }

    public void logAddedProcessToReadyQueue(int currentTimeStamp, ProcessControlBlock process) throws IOException {
        fileWriter.write("[Second " + currentTimeStamp + "] " + "Process " + process.getProcessName() + " added to ready queue successfully \n");
    }
    public void logRemovedProcessFromReadyQueue(int currentTimeStamp, ProcessControlBlock process) throws IOException {
        fileWriter.write("[Second " + currentTimeStamp + "] " + "Process " + process.getProcessName() + " removed from ready queue successfully \n");
    }

    public void finishLogging() throws IOException {
        fileWriter.close();
    }

}
