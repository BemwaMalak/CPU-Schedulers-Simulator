public class SchedulerFactory {


    public static Scheduler getScheduler(Schedulers schedulerType, ProcessQueue readyQueue, TimeManager timeManager, Logger logger, int contextSwitchingCost, int quantumTime) {
        switch (schedulerType) {
            case SHORTEST_JOB_FIRST: {
                return new ShortestJobFirstScheduler(readyQueue, timeManager, logger, contextSwitchingCost);
            }
            case PRIORITY: {
                return new PriorityScheduler(readyQueue, timeManager, logger, contextSwitchingCost);
            }
            case ROUND_ROBIN: {
                return new RoundRobinScheduler(readyQueue, timeManager, logger, quantumTime, contextSwitchingCost);
            }
        }
        return null;
    }
}
