import java.util.Comparator;

public class ArrivalTimeComparator implements Comparator<ProcessControlBlock> {

    @Override
    public int compare(ProcessControlBlock process1, ProcessControlBlock process2) {
        if (process1.getProcessArrivalTime() < process2.getProcessArrivalTime()) {
            return -1;
        } else if (process1.getProcessArrivalTime() > process2.getProcessArrivalTime()) {
            return 1;
        }
        return 0;
    }
}
