import java.util.Comparator;
import java.util.Vector;

public class ProcessQueue {
    private final Vector<ProcessControlBlock> processes;


    public ProcessQueue() {
        this.processes = new Vector<>();
    }

    public ProcessControlBlock getNextProcess(SortingCriteria sortingCriteria) {
        sort(sortingCriteria);
        return processes.firstElement();
    }

    private void sort(SortingCriteria sortingCriteria) {
        Comparator<ProcessControlBlock> comparator = null;
        if (sortingCriteria != null) {
            switch (sortingCriteria) {
                case BASED_ON_PROCESS_PRIORITY -> comparator = new PriorityComparator();
                case BASED_ON_PROCESS_ARRIVAL_TIME -> comparator = new ArrivalTimeComparator();
                case BASED_ON_PROCESS_BURST_TIME -> comparator = new BurstTimeComparator();
            }
        }

        if (comparator != null) {
            processes.sort(comparator);
        }
    }

    public void add(ProcessControlBlock process) {
        processes.add(process);
    }

    public void addAll(Vector<ProcessControlBlock> processes) {
        this.processes.addAll(processes);
    }

    public void remove(ProcessControlBlock process) {
        processes.remove(process);
    }

    public void increaseAllPrioritiesBy(int value) {
        for (ProcessControlBlock process : processes) {
            process.incrementPriorityBy(value);
        }
    }

    public boolean isEmpty() {
        return (processes.size() == 0);
    }

}