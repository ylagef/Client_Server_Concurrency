import java.io.Serializable;

public class Message implements Serializable {
    private Work work;
    private int[][] matrix;
    private boolean lastTime = false;

    Message(Work work, int[][] matrix) {
        this.work = work;
        this.matrix = matrix;
    }

    Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    int[][] getMatrix() {
        return matrix;
    }

    void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    boolean isLastTime() {
        return lastTime;
    }

    void setLastTime(boolean lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public String toString() {
        return "Message{" +
                "work=" + work.toString() +
                ", lastTime=" + lastTime +
                '}';
    }

}
