import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Vector;

public class Main {

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws IOException, InterruptedException {



        // Create logs directory for logger
        new File("logs").mkdir();

        while (true){
            askUserForChoice();
        }

    }

    private static void askUserForChoice() throws IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Please choose the scheduling algorithm that you want to simulate: ");

        int choice = 0;

        System.out.println("1- Preemptive shortest job first scheduling");
        System.out.println("2- Round robin scheduling");
        System.out.println("3- Preemptive priority scheduling");
        System.out.println("4- Exit");

        choice = scanner.nextInt();

        // System Exit
        if(choice == 4){
            System.exit(0);
        }


        Schedulers scheduler = getSchedulerFromChoice(choice);

        startScheduler(scheduler);



    }
    private static Schedulers getSchedulerFromChoice(int choice){
        switch (choice){
            case 1:
                return Schedulers.SHORTEST_JOB_FIRST;
            case 2:
                return Schedulers.ROUND_ROBIN;
            case 3:
                return Schedulers.PRIORITY;
        }
        return null;
    }
    private static void startScheduler(Schedulers scheduler) throws IOException, InterruptedException {
        switch (scheduler){
            case SHORTEST_JOB_FIRST:
                startShortestJobFirstScheduler();
                break;
            case ROUND_ROBIN:
                startRoundRobinScheduler();
                break;
            case PRIORITY:
                startPriorityScheduler();
                break;
        }
    }
    private static void startShortestJobFirstScheduler() throws IOException, InterruptedException {
        SimulatorSetup simulatorSetup = getSimulatorSetupFromUser(Schedulers.SHORTEST_JOB_FIRST);
        Vector<ProcessControlBlock> processes = getProcessesFromUser(simulatorSetup, Schedulers.SHORTEST_JOB_FIRST);




        ProcessQueue readyQueue = new ProcessQueue();
        ProcessQueue jobQueue = new ProcessQueue();

        jobQueue.addAll(processes);

        ShortestJobFirstScheduler shortestJobFirstScheduler = new ShortestJobFirstScheduler(readyQueue, simulatorSetup.getTimeManager(), simulatorSetup.getLogger(), simulatorSetup.getContextSwitchingCost());
        LongTermScheduler longTermScheduler = new LongTermScheduler(jobQueue, readyQueue, simulatorSetup.getTimeManager(), simulatorSetup.getLogger());

        shortestJobFirstScheduler.start();
        longTermScheduler.start();

        longTermScheduler.join();
        shortestJobFirstScheduler.join();

        simulatorSetup.getLogger().finishLogging();
    }

    private static void startRoundRobinScheduler() throws InterruptedException, IOException {

        SimulatorSetup simulatorSetup = getSimulatorSetupFromUser(Schedulers.ROUND_ROBIN);
        Vector<ProcessControlBlock> processes = getProcessesFromUser(simulatorSetup, Schedulers.ROUND_ROBIN);




        ProcessQueue readyQueue = new ProcessQueue();
        ProcessQueue jobQueue = new ProcessQueue();

        jobQueue.addAll(processes);

        RoundRobinScheduler roundRobinScheduler = new RoundRobinScheduler(readyQueue, simulatorSetup.getTimeManager(), simulatorSetup.getLogger(), simulatorSetup.getQuantumTime(), simulatorSetup.getContextSwitchingCost());
        LongTermScheduler longTermScheduler = new LongTermScheduler(jobQueue, readyQueue, simulatorSetup.getTimeManager(), simulatorSetup.getLogger());

        roundRobinScheduler.start();
        longTermScheduler.start();

        longTermScheduler.join();
        roundRobinScheduler.join();

        simulatorSetup.getLogger().finishLogging();
    }

    private static void startPriorityScheduler() throws InterruptedException, IOException {
        SimulatorSetup simulatorSetup = getSimulatorSetupFromUser(Schedulers.PRIORITY);
        Vector<ProcessControlBlock> processes = getProcessesFromUser(simulatorSetup, Schedulers.PRIORITY);




        ProcessQueue readyQueue = new ProcessQueue();
        ProcessQueue jobQueue = new ProcessQueue();

        jobQueue.addAll(processes);

        PriorityScheduler priorityScheduler = new PriorityScheduler(readyQueue, simulatorSetup.getTimeManager(), simulatorSetup.getLogger(), simulatorSetup.getContextSwitchingCost());
        LongTermScheduler longTermScheduler = new LongTermScheduler(jobQueue, readyQueue, simulatorSetup.getTimeManager(), simulatorSetup.getLogger());

        priorityScheduler.start();
        longTermScheduler.start();

        longTermScheduler.join();
        priorityScheduler.join();

        simulatorSetup.getLogger().finishLogging();
    }

    private static SimulatorSetup getSimulatorSetupFromUser(Schedulers scheduler) throws IOException {
        Scanner scanner = new Scanner(System.in);

        TimeManager timeManager = new TimeManager();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        File logFile = new File("logs\\" + dateFormatter.format(timestamp) + " ShortestJobFirst.txt");
        Logger logger = new Logger(logFile);

        int totalNumberOfProcesses;
        int contextSwitchingCost;

        System.out.println("Please enter the total number of processes: ");
        totalNumberOfProcesses = scanner.nextInt();

        System.out.println("Please enter the context switching cost: ");
        contextSwitchingCost = scanner.nextInt();

        if(scheduler == Schedulers.ROUND_ROBIN){

            System.out.println("Please enter the quantum time: ");
            int quantumTime = scanner.nextInt();

            SimulatorSetup simulatorSetup = new SimulatorSetup(totalNumberOfProcesses, contextSwitchingCost, quantumTime, logger, timeManager);

            return simulatorSetup;
        }else{

            SimulatorSetup simulatorSetup = new SimulatorSetup(totalNumberOfProcesses, contextSwitchingCost, logger, timeManager);

            return simulatorSetup;
        }



    }

    private static Vector<ProcessControlBlock> getProcessesFromUser(SimulatorSetup simulatorSetup, Schedulers scheduler){
        Scanner scanner = new Scanner(System.in);

        int totalNumberOfProcesses = simulatorSetup.getTotalNumberOfProcesses();
        Vector<ProcessControlBlock> processes = new Vector<>();


        for(int i = 0; i < totalNumberOfProcesses; i++){
            String processName;
            int processArrivalTime;
            int processBurstTime;
            System.out.println("Please enter process number " + (i + 1) + " name: ");
            processName = scanner.next();

            System.out.println("Please enter process number " + (i + 1) + " arrival time: ");
            processArrivalTime = scanner.nextInt();

            System.out.println("Please enter process number " + (i + 1) + " burst time: ");
            processBurstTime = scanner.nextInt();

            if(scheduler == Schedulers.PRIORITY){

                System.out.println("Please enter process number " + (i + 1) + " priority: ");
                int processPriority = scanner.nextInt();

                ProcessControlBlock process = new ProcessControlBlock(processName, processArrivalTime, processBurstTime, processPriority);
                processes.add(process);
            }else{
                ProcessControlBlock process = new ProcessControlBlock(processName, processArrivalTime, processBurstTime);
                processes.add(process);
            }
        }

        return processes;
    }


}
