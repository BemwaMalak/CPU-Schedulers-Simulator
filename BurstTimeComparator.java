import java.util.Comparator;

public class BurstTimeComparator implements Comparator<ProcessControlBlock> {

    @Override
    public int compare(ProcessControlBlock process1, ProcessControlBlock process2) {
        if (process1.getProcessBurstTime() < process2.getProcessBurstTime()) {
            return -1;
        } else if (process1.getProcessBurstTime() > process2.getProcessBurstTime()) {
            return 1;
        }
        return 0;
    }
}
