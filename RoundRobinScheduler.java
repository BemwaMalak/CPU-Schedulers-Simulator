import java.io.IOException;

public class RoundRobinScheduler extends CPUScheduler {
    private final int quantumTime;
    private int elapsedQuantumTime;

    public RoundRobinScheduler(ProcessQueue readyQueue, TimeManager timeManager, Logger logger, int quantumTime, int contextSwitchingCost) {
        this.readyQueue = readyQueue;
        this.timeManager = timeManager;
        this.logger = logger;
        this.quantumTime = quantumTime;
        this.elapsedQuantumTime = 0;
        this.dispatcher = new Dispatcher(contextSwitchingCost, logger);
    }

    @Override
    protected void execute() {
        if (!readyQueue.isEmpty()) {

            if (dispatcher.isFinished()) {

                dispatcher.logFinishedContextSwitching(timeManager.getCurrentTimeStamp());
                dispatcher.doSwitching();

            }

            if (dispatcher.isStarted()) {
                dispatcher.incrementElapsedTime();
            } else {
                ProcessControlBlock candidateProcess = readyQueue.getNextProcess(null);

                if (dispatcher.checkIfSwitchingIsRequired(candidateProcess)) {
                    dispatcher.logStartedContextSwitching(timeManager.getCurrentTimeStamp());
                }

                // For context switching cost == 0
                if (dispatcher.isFinished()) {

                    dispatcher.logFinishedContextSwitching(timeManager.getCurrentTimeStamp());
                    dispatcher.doSwitching();

                } else if (dispatcher.isStarted()) {
                    dispatcher.incrementElapsedTime();
                }

                if (!dispatcher.isStarted()) {
                    dispatcher.setCurrentProcess(candidateProcess);
                    dispatcher.getCurrentProcess().execute();

                    logExecutedProcess();

                    elapsedQuantumTime++;

                    if (dispatcher.getCurrentProcess().isFinished()) {
                        logProcessFinished();
                        elapsedQuantumTime = 0;

                        readyQueue.remove(dispatcher.getCurrentProcess());
                        logRemovedProcessFromReadyQueue();


                        dispatcher.setCurrentProcess(null);
                        dispatcher.setSwitchingToProcess(null);
                    } else if (elapsedQuantumTime == quantumTime) {
                        logProcessFinishedQuantumTime();
                        elapsedQuantumTime = 0;

                        readyQueue.remove(dispatcher.getCurrentProcess());
                        logRemovedProcessFromReadyQueue();

                        readyQueue.add(dispatcher.getCurrentProcess());
                        logAddedProcessToReadyQueue();

                        dispatcher.setCurrentProcess(null);
                        dispatcher.setSwitchingToProcess(null);
                    }
                }
            }
        }
    }

    private void logProcessFinishedQuantumTime() {
        try {
            logger.logProcessFinishedQuantumTime(timeManager.getCurrentTimeStamp(), dispatcher.getCurrentProcess(), quantumTime);
        } catch (IOException e) {

        }
    }

    private void logAddedProcessToReadyQueue() {
        try {
            logger.logAddedProcessToReadyQueue(timeManager.getCurrentTimeStamp(), dispatcher.getCurrentProcess());
        } catch (IOException e) {

        }
    }


}
