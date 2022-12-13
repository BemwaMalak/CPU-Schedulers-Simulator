public class SchedulerFactory {
    public static Scheduler getScheduler(Schedulers scheduler, ProcessQueue readyQueue, TimeManager timeManager, Logger logger, int contextSwitchingCost, int quantumTime){
        return new RoundRobinScheduler(readyQueue, timeManager, logger, contextSwitchingCost, quantumTime);
    }

    public static Scheduler getScheduler(Schedulers scheduler, ProcessQueue readyQueue, TimeManager timeManager, Logger logger, int contextSwitchingCost){
        switch (scheduler){
            case SHORTEST_JOB_FIRST:{
                return new ShortestJobFirstScheduler(readyQueue, timeManager, logger, contextSwitchingCost);
            }
            case PRIORITY:{
                return new PriorityScheduler(readyQueue, timeManager, logger, contextSwitchingCost);
            }
        }
        return null;
    }
}
