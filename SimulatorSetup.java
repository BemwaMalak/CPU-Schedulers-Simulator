public class SimulatorSetup {
    private int totalNumberOfProcesses;
    private int contextSwitchingCost;
    private int quantumTime;
    private Logger logger;
    private TimeManager timeManager;


    public SimulatorSetup(int totalNumberOfProcesses, int contextSwitchingCost, int quantumTime, Logger logger, TimeManager timeManager){
        this.totalNumberOfProcesses = totalNumberOfProcesses;
        this.contextSwitchingCost = contextSwitchingCost;
        this.logger = logger;
        this.timeManager = timeManager;
        this.quantumTime = quantumTime;
    }
    public SimulatorSetup(int totalNumberOfProcesses, int contextSwitchingCost, Logger logger, TimeManager timeManager){
        this.totalNumberOfProcesses = totalNumberOfProcesses;
        this.contextSwitchingCost = contextSwitchingCost;
        this.logger = logger;
        this.timeManager = timeManager;
        this.quantumTime = 0;
    }


    public int getContextSwitchingCost() {
        return contextSwitchingCost;
    }

    public void setContextSwitchingCost(int contextSwitchingCost) {
        this.contextSwitchingCost = contextSwitchingCost;
    }

    public int getTotalNumberOfProcesses() {
        return totalNumberOfProcesses;
    }

    public void setTotalNumberOfProcesses(int totalNumberOfProcesses) {
        this.totalNumberOfProcesses = totalNumberOfProcesses;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public TimeManager getTimeManager() {
        return timeManager;
    }

    public void setTimeManager(TimeManager timeManager) {
        this.timeManager = timeManager;
    }

    public int getQuantumTime() {
        return quantumTime;
    }

    public void setQuantumTime(int quantumTime) {
        this.quantumTime = quantumTime;
    }
}
