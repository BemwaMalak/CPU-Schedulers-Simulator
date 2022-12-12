import java.io.IOException;

public class ShortestJobFirstScheduler extends Scheduler {
    private ProcessQueue readyQueue;

    public ShortestJobFirstScheduler(ProcessQueue readyQueue, TimeManager timeManager, Logger logger, int contextSwitchingCost){
        this.readyQueue = readyQueue;
        this.timeManager = timeManager;
        this.logger = logger;
        this.dispatcher = new Dispatcher(contextSwitchingCost);
    }
    @Override
    public void run() {
        while(!readyQueue.isEmpty() || !LongTermScheduler.isFinished()){
            if(!LongTermScheduler.isFinished()){
                try {
                    timeManager.waitForCPUScheduler();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if(!readyQueue.isEmpty()){
                    if(dispatcher.isFinished()){
                        try {
                            logger.logFinishedContextSwitching(timeManager.getCurrentTimeStamp(), dispatcher);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        dispatcher.doSwitching();
                    }

                    if(dispatcher.isStarted()){
                        dispatcher.incrementElapsedTime();
                    }else{
                        ProcessControlBlock process = readyQueue.getNextProcess(SortingCriteria.BASED_ON_PROCESS_BURST_TIME);

                        if(dispatcher.checkIfSwitchingIsRequired(process)){
                            try {
                                logger.logStartedContextSwitching(timeManager.getCurrentTimeStamp(), dispatcher);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        // For context switching cost == 0
                        if(dispatcher.isFinished()){
                            try {
                                logger.logFinishedContextSwitching(timeManager.getCurrentTimeStamp(), dispatcher);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            dispatcher.doSwitching();
                        }else if(dispatcher.isStarted()){
                            dispatcher.incrementElapsedTime();
                        }

                        if(!dispatcher.isStarted()){

                            dispatcher.setCurrentProcess(process);
                            dispatcher.getCurrentProcess().execute();
                            try {
                                logger.logExecutedProcess(timeManager.getCurrentTimeStamp(), dispatcher.getCurrentProcess());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            if(dispatcher.getCurrentProcess().isFinished()){
                                readyQueue.remove(dispatcher.getCurrentProcess());
                                try {
                                    logger.logProcessFinished(timeManager.getCurrentTimeStamp(), dispatcher.getCurrentProcess());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    logger.logRemovedProcessFromReadyQueue(timeManager.getCurrentTimeStamp(), dispatcher.getCurrentProcess());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                dispatcher.setCurrentProcess(null);
                                dispatcher.setSwitchingToProcess(null);
                            }
                        }
                    }
                }

                timeManager.signalForLongTermScheduler();
                timeManager.tick();
            }else{
                if(!readyQueue.isEmpty()){
                    if(dispatcher.isFinished()){
                        try {
                            logger.logFinishedContextSwitching(timeManager.getCurrentTimeStamp(), dispatcher);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        dispatcher.doSwitching();
                    }

                    if(dispatcher.isStarted()){
                        dispatcher.incrementElapsedTime();
                    }else{
                        ProcessControlBlock process = readyQueue.getNextProcess(SortingCriteria.BASED_ON_PROCESS_BURST_TIME);

                        if(dispatcher.checkIfSwitchingIsRequired(process)){
                            try {
                                logger.logStartedContextSwitching(timeManager.getCurrentTimeStamp(), dispatcher);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        // For context switching cost == 0
                        if(dispatcher.isFinished()){
                            try {
                                logger.logFinishedContextSwitching(timeManager.getCurrentTimeStamp(), dispatcher);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            dispatcher.doSwitching();
                        }else if(dispatcher.isStarted()){
                            dispatcher.incrementElapsedTime();
                        }

                        if(!dispatcher.isStarted()){

                            dispatcher.setCurrentProcess(process);
                            dispatcher.getCurrentProcess().execute();
                            try {
                                logger.logExecutedProcess(timeManager.getCurrentTimeStamp(), dispatcher.getCurrentProcess());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            if(dispatcher.getCurrentProcess().isFinished()){
                                readyQueue.remove(dispatcher.getCurrentProcess());
                                try {
                                    logger.logProcessFinished(timeManager.getCurrentTimeStamp(), dispatcher.getCurrentProcess());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    logger.logRemovedProcessFromReadyQueue(timeManager.getCurrentTimeStamp(), dispatcher.getCurrentProcess());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                dispatcher.setCurrentProcess(null);
                                dispatcher.setSwitchingToProcess(null);
                            }
                        }
                    }
                }

                // Ticks for LongTermScheduler and Ticks for itself
                timeManager.tick();
                timeManager.tick();
            }
        }

        try {
            logger.logSchedulerFinished(timeManager.getCurrentTimeStamp());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
