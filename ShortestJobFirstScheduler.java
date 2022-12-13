public class ShortestJobFirstScheduler extends CPUScheduler {
    public ShortestJobFirstScheduler(ProcessQueue readyQueue, TimeManager timeManager, Logger logger, int contextSwitchingCost) {
        this.readyQueue = readyQueue;
        this.timeManager = timeManager;
        this.logger = logger;
        this.dispatcher = new Dispatcher(contextSwitchingCost, logger);
    }

    @Override
    public void run() {
        while (!readyQueue.isEmpty() || !LongTermScheduler.isFinished()) {
            if (!LongTermScheduler.isFinished()) {

                timeManager.waitForCPUScheduler();

                execute();

                timeManager.signalForLongTermScheduler();
                timeManager.tick();
            } else {
                execute();

                // Ticks for LongTermScheduler and Ticks for itself
                timeManager.tick();
                timeManager.tick();
            }
        }

        logSchedulerFinished();
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
                ProcessControlBlock candidateProcess = readyQueue.getNextProcess(SortingCriteria.BASED_ON_PROCESS_BURST_TIME);

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

                    if (dispatcher.getCurrentProcess().isFinished()) {
                        logProcessFinished();

                        readyQueue.remove(dispatcher.getCurrentProcess());
                        logRemovedProcessFromReadyQueue();

                        dispatcher.setCurrentProcess(null);
                        dispatcher.setSwitchingToProcess(null);
                    }
                }
            }
        }
    }
}
