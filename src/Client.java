import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

class Client extends Thread {
    static MyMatrix matrix;
    private int name;

    Client(int name) {
        this.name = name;
    }

    public void run() {
        System.out.println("Starting client " + name + "...");
        boolean lastTime = false;
        while (!lastTime) {
            try {
                InetAddress host = InetAddress.getLocalHost();
                Socket socket = new Socket(host.getHostName(), 4444);

                //Read message from server
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();
                System.out.println("C" + name + " received:\t" + message.toString());
                lastTime = message.isLastTime();
                matrix = new MyMatrix(message.getMatrix());

                //Processing message
                processMatrix(message);

                //Send message to server
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(message);
                System.out.println("C" + name + " sent:\t" + message.getWork().getStart() + " " + message.getWork().getEnd() + "\n");

                ois.close();
                oos.close();
                socket.close();
            } catch (Exception e) {
                System.out.println("Exception on Client. " + e);
            }
        }
        System.out.println("Client " + name + " ended.\n");
    }

    private void processMatrix(Message message) {
        int startR = message.getWork().getStart().getR();
        int startC = message.getWork().getStart().getC();
        int endR = message.getWork().getEnd().getR();
        int endC = message.getWork().getEnd().getC();

        int numRows = endR - startR;
        int[][] convertedMatrix = message.getMatrix();

        while (numRows >= 1) {
            for (int c = startC; c < matrix.getColsSize(); c++) {
                convertedMatrix[startR][c] = (int) (new Cell(startR, c)).getMedianFilter();
            }
            startR++;
            startC = 0;
            numRows--;
        }

        for (int c = startC; c <= endC; c++) {
            convertedMatrix[endR][c] = (int) (new Cell(endR, c)).getMedianFilter();
        }

        message.setMatrix(convertedMatrix);
    }
}

class MyMatrix {
    private int[][] matrix;

    MyMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    int getValue(int r, int c) {
        return matrix[r][c];
    }

    int getRowsSize() {
        return matrix.length;
    }

    int getColsSize() {
        return matrix[0].length;
    }

    @Override
    public String toString() {
        StringBuilder toPrint = new StringBuilder();

        for (int[] row : matrix) {
            for (int cell : row) {
                toPrint.append(cell).append("\t\t ");
            }
            toPrint.append("\n");
        }

        return toPrint.toString();
    }
}
