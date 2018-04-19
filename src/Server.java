import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

class Server extends Thread {
    private final CountDownLatch latch;
    private int[][] initialData;
    private double[][] finalData;
    private int division = 4;

    Server(int[][] data, CountDownLatch latch) {
        this.initialData = data;
        this.finalData = new double[data.length][data[0].length];
        this.latch = latch;
    }

    public void run() {

        try {
            System.out.println("Starting server...");
            ServerSocket server = new ServerSocket(4444);

            int i = 0;

            System.out.println("Server hearing...");
            Work[] work = shareWork(division);
            int w = 0;
            while (w < work.length) {
                Socket socket = server.accept();

                //Send message to client
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                Message messageSend = new Message(work[w++], initialData);
                if (w == work.length) {
                    //If last time
                    messageSend.setLastTime(true);
                }
                oos.writeObject(messageSend);

                //Read message from client
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message messageReceived = (Message) ois.readObject();
                fillFinalMatrix(messageReceived);

                ois.close();
                oos.close();
                socket.close();

                if (i == initialData[0].length - 1) {
                    i = 0;
                } else {
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception on Server. " + e);
        }

        System.out.println("Server ended");
        latch.countDown(); //reduce count of CountDownLatch by 1
    }

    double[][] getFinalData() {
        return finalData;
    }

    private Work[] shareWork(int size) {
        Work[] array = new Work[size];

        int cellsQuantity = initialData.length * initialData[0].length;
        int cellsPerWork;
        if (size >= cellsQuantity) {
            //If more works than cells, just one work per cell.
            cellsPerWork = 1;
            size = cellsQuantity;
        } else {
            cellsPerWork = cellsQuantity / size;
        }

        //Assigning cells to works. By blocks.
        int id = 0;
        int endR, endC, startR = 0, startC = 0;
        int[][] times = new int[size][4];

        while (id != size - 1) { //While not last work.
            if (startC + cellsPerWork >= initialData.length) { // If doesnt fit on actual row
                int cellsForAssign = cellsPerWork - (initialData.length - startC);
                int fullRows = cellsForAssign / initialData.length;
                cellsForAssign = cellsForAssign - initialData.length * fullRows;
                if (cellsForAssign == 0) {
                    endC = initialData.length - 1;
                    endR = startR + fullRows;
                } else {
                    endC = cellsForAssign - 1;
                    endR = startR + fullRows + 1;
                }
            } else {
                endC = startC + cellsPerWork - 1;
                endR = startR;
            }

            times[id][0] = startR;
            times[id][1] = startC;
            times[id][2] = endR;
            times[id][3] = endC;

            if (endC == initialData.length - 1) {
                startC = 0;
                startR = endR + 1;
            } else {
                startC = endC + 1;
                startR = endR;
            }

            id++;
        }

        // Last work goes until the end
        times[id][0] = startR;
        times[id][1] = startC;
        times[id][2] = initialData[0].length - 1;
        times[id][3] = initialData.length - 1;

        int j = 0;
        for (int[] i : times) {
            array[j++] = new Work(new Cell(i[0], i[1]), new Cell(i[2], i[3]));
        }

        return array;
    }

    private void fillFinalMatrix(Message received) {
        Cell startCell = received.getWork().getStart();
        int startR = startCell.getR();
        int startC = startCell.getC();

        Cell endCell = received.getWork().getEnd();
        int endR = endCell.getR();
        int endC = endCell.getC();

        int[][] result = received.getMatrix();

        int numRows = endR - startR;

        while (numRows >= 1) {
            for (int c = startC; c < initialData[0].length; c++) {
                finalData[startR][c] = result[startR][c];
            }
            startR++;
            startC = 0;
            numRows--;
        }

        for (int c = startC; c <= endC; c++) {
            finalData[endR][c] = result[endR][c];
        }
    }
}

