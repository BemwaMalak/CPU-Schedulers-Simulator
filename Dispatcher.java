public class Dispatcher {
    private ProcessControlBlock currentProcess;
    private ProcessControlBlock switchingToProcess;
    private int contextSwitchingCost;
    private int elapsedContextSwitchTime;
    private boolean startedSwitching;


    public Dispatcher(int contextSwitchingCost){
        this.currentProcess = null;
        this.switchingToProcess = null;
        this.contextSwitchingCost = contextSwitchingCost;
        this.elapsedContextSwitchTime = 0;
        this.startedSwitching = false;
    }

    public void doSwitching(){
        elapsedContextSwitchTime = 0;

        startedSwitching = false;

        currentProcess = switchingToProcess;

        switchingToProcess = null;
    }

    public void incrementElapsedTime(){
        elapsedContextSwitchTime++;
    }
    public boolean checkIfSwitchingIsRequired(ProcessControlBlock candidateProcess){
        if(currentProcess != null && currentProcess != candidateProcess && (switchingToProcess == null || switchingToProcess != candidateProcess)) {
            startedSwitching = true;
            switchingToProcess = candidateProcess;
            return true;
        }
        return false;
    }
    public boolean isFinished(){
        return (elapsedContextSwitchTime == contextSwitchingCost && startedSwitching);
    }

    public boolean isStarted(){
        return (startedSwitching);
    }
    public ProcessControlBlock getCurrentProcess(){
        return this.currentProcess;
    }
    public ProcessControlBlock getSwitchingToProcess(){
        return this.switchingToProcess;
    }

    public void setCurrentProcess(ProcessControlBlock currentProcess){
        this.currentProcess = currentProcess;
    }
    public void setSwitchingToProcess(ProcessControlBlock switchingToProcess){
        this.switchingToProcess = switchingToProcess;
    }


}
