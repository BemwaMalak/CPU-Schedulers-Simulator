import java.util.Comparator;

public class PriorityComparator implements Comparator<ProcessControlBlock> {

    @Override
    public int compare(ProcessControlBlock process1, ProcessControlBlock process2) {
        if(process1.getProcessPriority() < process2.getProcessPriority()){
            return -1;
        }else if(process1.getProcessPriority() > process2.getProcessPriority()){
            return 1;
        }
        return 0;
    }
}
