public class ProcessControlBlock {
    private final String processName;
    private final int processArrivalTime;
    private int processBurstTime;
    private int processPriority;


    public ProcessControlBlock(String processName, int processArrivalTime, int processBurstTime, int processPriority) {
        this.processName = processName;
        this.processArrivalTime = processArrivalTime;
        this.processBurstTime = processBurstTime;
        this.processPriority = processPriority;
    }

    public ProcessControlBlock(String processName, int processArrivalTime, int processBurstTime) {
        this.processName = processName;
        this.processArrivalTime = processArrivalTime;
        this.processBurstTime = processBurstTime;
        this.processPriority = 0;
    }


    public void execute() {
        if (this.processBurstTime > 0) this.processBurstTime--;

    }

    public boolean isFinished() {
        return (processBurstTime == 0);
    }

    public void incrementPriorityBy(int value) {
        if (this.processPriority - value >= 0) this.processPriority -= value;
        else this.processPriority = 0;
    }

    public String getProcessName() {
        return this.processName;
    }

    public int getProcessArrivalTime() {
        return this.processArrivalTime;
    }

    public int getProcessBurstTime() {
        return this.processBurstTime;
    }

    public int getProcessPriority() {
        return this.processPriority;
    }


}