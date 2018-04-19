import java.io.Serializable;

class Cell implements Serializable {
    private int r;
    private int c;

    Cell(int r, int c) {
        this.r = r;
        this.c = c;
    }

    double getMedianFilter() {
        int colsSize = Client.matrix.getColsSize();
        int rowsSize = Client.matrix.getRowsSize();

        double total = 0;
        int filter = 1;
        for (int row = this.r - filter; row <= this.r + filter; row++) {
            for (int col = this.c - filter; col <= this.c + filter; col++) {
                if (row < 0 || row >= rowsSize || col < 0 || col >= colsSize) {
                    Cell mirror = new Cell(row, col).getMirror(this);
                    total += Client.matrix.getValue(mirror.getR(), mirror.getC());
                } else {
                    total += Client.matrix.getValue(row, col);
                }
            }
        }

        double divider = Math.pow(2 * filter + 1, 2);

        return Math.ceil((total / divider) * 100) / 100;
    }

    private Cell getMirror(Cell actual) {
        Cell toret = new Cell(0, 0);

        if (r < 0) {
            toret.setR(-r);
        } else if (r < Client.matrix.getRowsSize()) {
            toret.setR(r);
        } else {
            int rOut = actual.getR() - r + actual.getR();
            toret.setR(rOut);
        }

        if (c < 0) {
            toret.setC(-c);
        } else if (c < Client.matrix.getColsSize()) {
            toret.setC(c);
        } else {
            int cOut = actual.getC() - c + actual.getC();
            toret.setC(cOut);
        }

        return toret;
    }

    int getR() {
        return r;
    }

    private void setR(int r) {
        this.r = r;
    }

    int getC() {
        return c;
    }

    private void setC(int c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "r=" + r +
                ", c=" + c +
                '}';
    }
}