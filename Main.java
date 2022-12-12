import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Vector;

public class Main {

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);
        new File("logs").mkdir();
        while (true){
            System.out.println("Please choose the scheduling algorithm that you want to simulate: ");
            int choice = 0;
            System.out.println("1- Preemptive shortest job first scheduling");
            System.out.println("2- Round robin scheduling");
            System.out.println("3- Preemptive priority scheduling");
            System.out.println("4- Exit");

            choice = scanner.nextInt();

            switch (choice){
                case 1:
                    startShortestJobFirst();
                    break;
                case 2:
                    startRoundRobin();
                    break;
                case 3:
                    startPriorityScheduler();
                    break;
                case 4:
                    System.exit(0);
            }
        }

    }

    private static void startShortestJobFirst() throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);
        TimeManager timeManager = new TimeManager();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        File logFile = new File("logs\\" + dateFormatter.format(timestamp) + " ShortestJobFirst.txt");
        logFile.createNewFile();
        Logger logger = new Logger(logFile.getPath());

        int totalNumberOfProcesses;
        int contextSwitchingCost;

        System.out.println("Please enter the total number of processes: ");
        totalNumberOfProcesses = scanner.nextInt();

        System.out.println("Please enter the context switching cost: ");
        contextSwitchingCost = scanner.nextInt();





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

            ProcessControlBlock process = new ProcessControlBlock(processName, processArrivalTime, processBurstTime);
            processes.add(process);
        }


        ProcessQueue readyQueue = new ProcessQueue();
        ProcessQueue jobQueue = new ProcessQueue();

        jobQueue.addAll(processes);

        ShortestJobFirstScheduler shortestJobFirstScheduler = new ShortestJobFirstScheduler(readyQueue, timeManager, logger, contextSwitchingCost);
        LongTermScheduler longTermScheduler = new LongTermScheduler(jobQueue, readyQueue, timeManager, logger);

        shortestJobFirstScheduler.start();
        longTermScheduler.start();

        longTermScheduler.join();
        shortestJobFirstScheduler.join();

        logger.finishLogging();
    }

    private static void startRoundRobin() throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);
        TimeManager timeManager = new TimeManager();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        File logFile = new File("logs\\" + dateFormatter.format(timestamp) + " RoundRobin.txt");
        logFile.createNewFile();

        Logger logger = new Logger(logFile.getPath());


        int totalNumberOfProcesses;
        int contextSwitchingCost;
        int quantumTime;

        System.out.println("Please enter the total number of processes: ");
        totalNumberOfProcesses = scanner.nextInt();

        System.out.println("Please enter the context switching cost: ");
        contextSwitchingCost = scanner.nextInt();

        System.out.println("Please enter the quantum time: ");
        quantumTime = scanner.nextInt();

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

            ProcessControlBlock process = new ProcessControlBlock(processName, processArrivalTime, processBurstTime);
            processes.add(process);
        }


        ProcessQueue readyQueue = new ProcessQueue();
        ProcessQueue jobQueue = new ProcessQueue();

        jobQueue.addAll(processes);

        RoundRobinScheduler roundRobinScheduler = new RoundRobinScheduler(readyQueue, timeManager, logger, quantumTime, contextSwitchingCost);
        LongTermScheduler longTermScheduler = new LongTermScheduler(jobQueue, readyQueue, timeManager, logger);

        roundRobinScheduler.start();
        longTermScheduler.start();

        longTermScheduler.join();
        roundRobinScheduler.join();

        logger.finishLogging();
    }

    private static void startPriorityScheduler() throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);
        TimeManager timeManager = new TimeManager();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        File logFile = new File("logs\\" + dateFormatter.format(timestamp) + " Priority.txt");
        logFile.createNewFile();
        Logger logger = new Logger(logFile.getPath());

        int totalNumberOfProcesses;
        int contextSwitchingCost;

        System.out.println("Please enter the total number of processes: ");
        totalNumberOfProcesses = scanner.nextInt();

        System.out.println("Please enter the context switching cost: ");
        contextSwitchingCost = scanner.nextInt();





        Vector<ProcessControlBlock> processes = new Vector<>();

        for(int i = 0; i < totalNumberOfProcesses; i++){
            String processName;
            int processArrivalTime;
            int processBurstTime;
            int processPriority;
            System.out.println("Please enter process number " + (i + 1) + " name: ");
            processName = scanner.next();

            System.out.println("Please enter process number " + (i + 1) + " arrival time: ");
            processArrivalTime = scanner.nextInt();

            System.out.println("Please enter process number " + (i + 1) + " burst time: ");
            processBurstTime = scanner.nextInt();

            System.out.println("Please enter process number " + (i + 1) + " priority: ");
            processPriority = scanner.nextInt();

            ProcessControlBlock process = new ProcessControlBlock(processName, processArrivalTime, processBurstTime, processPriority);
            processes.add(process);
        }


        ProcessQueue readyQueue = new ProcessQueue();
        ProcessQueue jobQueue = new ProcessQueue();

        jobQueue.addAll(processes);

        PriorityScheduler priorityScheduler = new PriorityScheduler(readyQueue, timeManager, logger, contextSwitchingCost);
        LongTermScheduler longTermScheduler = new LongTermScheduler(jobQueue, readyQueue, timeManager, logger);

        priorityScheduler.start();
        longTermScheduler.start();

        longTermScheduler.join();
        priorityScheduler.join();

        logger.finishLogging();
    }

}
