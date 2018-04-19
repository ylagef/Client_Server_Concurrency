import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.concurrent.CountDownLatch;

class MainServer {

    public static void main(String args[]) {

        try {
            //Read initial image
            File file_in = new File("lenna.png");
            BufferedImage img = ImageIO.read(file_in);
            final int width = img.getWidth();
            final int height = img.getHeight();
            int[][] data = new int[width][height];
            Raster raster_in = img.getData();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    final int d = raster_in.getSample(i, j, 0);
                    data[i][j] = d;
                }
            }

            //Initialize server with data
            final CountDownLatch latch = new CountDownLatch(1);
            Server server = new Server(data, latch);
            server.start();

            //Wait until threads end
            latch.await();  //main thread is waiting on Co  untDownLatch to finish

            //Write final image
            double[][] dataOut = server.getFinalData();
            WritableRaster raster_out = img.getRaster();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    raster_out.setSample(i, j, 0, dataOut[i][j] / 2);
                }
            }
            img.setData(raster_out);
            File file_out = new File("out.png");
            ImageIO.write(img, "png", file_out);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
