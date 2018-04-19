import java.io.Serializable;

class Work implements Serializable {
    private Cell start, end;

    Work(Cell start, Cell end) {
        this.start = start;
        this.end = end;
    }

    public Cell getStart() {
        return start;
    }

    public Cell getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Work{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
