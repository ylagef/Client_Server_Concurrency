import java.io.Serializable;

public class Message implements Serializable {
    private Work work;
    private int[][] initialMatrix;
    private int[][] resultMatrix;
    private boolean lastTime = false;

    Message(Work work, int[][] matrix) {
        this.work = work;
        this.initialMatrix = matrix;
    }

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    int[][] getInitialMatrix() {
        return initialMatrix;
    }

    public void setInitialMatrix(int[][] initialMatrix) {
        this.initialMatrix = initialMatrix;
    }

    public int[][] getResultMatrix() {
        return resultMatrix;
    }

    void setResultMatrix(int[][] resultMatrix) {
        this.resultMatrix = resultMatrix;
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
