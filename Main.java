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

        while (true) {
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
        if (choice == 4) {
            System.exit(0);
        }


        Schedulers scheduler = getSchedulerTypeFromChoice(choice);

        startScheduler(scheduler);


    }

    private static Schedulers getSchedulerTypeFromChoice(int choice) {
        switch (choice) {
            case 1:
                return Schedulers.SHORTEST_JOB_FIRST;
            case 2:
                return Schedulers.ROUND_ROBIN;
            case 3:
                return Schedulers.PRIORITY;
        }
        return null;
    }

    private static void startScheduler(Schedulers schedulerType) throws IOException, InterruptedException {
        SimulatorSetup simulatorSetup = getSimulatorSetupFromUser(schedulerType);
        Vector<ProcessControlBlock> processes = getProcessesFromUser(simulatorSetup, schedulerType);

        ProcessQueue readyQueue = new ProcessQueue();
        ProcessQueue jobQueue = new ProcessQueue();

        jobQueue.addAll(processes);

        Scheduler scheduler = SchedulerFactory.getScheduler(schedulerType, readyQueue, simulatorSetup.getTimeManager(), simulatorSetup.getLogger(), simulatorSetup.getContextSwitchingCost(), simulatorSetup.getQuantumTime());
        LongTermScheduler longTermScheduler = new LongTermScheduler(jobQueue, readyQueue, simulatorSetup.getTimeManager(), simulatorSetup.getLogger());

        scheduler.start();
        longTermScheduler.start();

        longTermScheduler.join();
        scheduler.join();

        simulatorSetup.getLogger().finishLogging();

    }

    private static SimulatorSetup getSimulatorSetupFromUser(Schedulers schedulerType) throws IOException {
        Scanner scanner = new Scanner(System.in);

        TimeManager timeManager = new TimeManager();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String logFilePath = "logs\\" + dateFormatter.format(timestamp) + " ";

        switch (schedulerType) {
            case SHORTEST_JOB_FIRST -> logFilePath += "ShortestJobFirst.txt";
            case PRIORITY -> logFilePath += "Priority.txt";
            case ROUND_ROBIN -> logFilePath += "RoundRobin.txt";
        }


        File logFile = new File(logFilePath);
        Logger logger = new Logger(logFile);

        int totalNumberOfProcesses;
        int contextSwitchingCost;

        System.out.println("Please enter the total number of processes: ");
        totalNumberOfProcesses = scanner.nextInt();

        System.out.println("Please enter the context switching cost: ");
        contextSwitchingCost = scanner.nextInt();

        if (schedulerType == Schedulers.ROUND_ROBIN) {

            System.out.println("Please enter the quantum time: ");
            int quantumTime = scanner.nextInt();

            SimulatorSetup simulatorSetup = new SimulatorSetup(totalNumberOfProcesses, contextSwitchingCost, quantumTime, logger, timeManager);

            return simulatorSetup;
        } else {

            SimulatorSetup simulatorSetup = new SimulatorSetup(totalNumberOfProcesses, contextSwitchingCost, logger, timeManager);

            return simulatorSetup;
        }


    }

    private static Vector<ProcessControlBlock> getProcessesFromUser(SimulatorSetup simulatorSetup, Schedulers schedulerType) {
        Scanner scanner = new Scanner(System.in);

        int totalNumberOfProcesses = simulatorSetup.getTotalNumberOfProcesses();
        Vector<ProcessControlBlock> processes = new Vector<>();


        for (int i = 0; i < totalNumberOfProcesses; i++) {
            String processName;
            int processArrivalTime;
            int processBurstTime;
            System.out.println("Please enter process number " + (i + 1) + " name: ");
            processName = scanner.next();

            System.out.println("Please enter process number " + (i + 1) + " arrival time: ");
            processArrivalTime = scanner.nextInt();

            System.out.println("Please enter process number " + (i + 1) + " burst time: ");
            processBurstTime = scanner.nextInt();

            if (schedulerType == Schedulers.PRIORITY) {

                System.out.println("Please enter process number " + (i + 1) + " priority: ");
                int processPriority = scanner.nextInt();

                ProcessControlBlock process = new ProcessControlBlock(processName, processArrivalTime, processBurstTime, processPriority);
                processes.add(process);
            } else {
                ProcessControlBlock process = new ProcessControlBlock(processName, processArrivalTime, processBurstTime);
                processes.add(process);
            }
        }

        return processes;
    }


}
