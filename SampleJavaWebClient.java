import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.Integer.parseInt;

public class SampleJavaWebClient {

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java SampleJavaWebClient <\"Header1: Value1[, Header2: Value2]\"> <DownloadURL> <OutputFile> <BufferSizeBytes>");
            System.exit(1);
        }

        String headers = args[0];
        String downloadUrl = args[1];
        String outputFile = args[2];
        int bufferSizeBytes = parseInt(args[3]);

        long startTime = System.currentTimeMillis();

        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set headers
            String[] headerArray = headers.split(";");
            for (String header : headerArray) {
                String[] headerParts = header.split(":");
                connection.setRequestProperty(headerParts[0].trim(), headerParts[1].trim());
            }

            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {

                // Download the file
                byte[] buffer = new byte[bufferSizeBytes];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            long endTime = System.currentTimeMillis();
            double fileSizeInBytes = Files.size(Path.of(outputFile));
            double fileSizeInMegabytes = fileSizeInBytes / (1024 * 1024);
            double totalTimeInSeconds = round((endTime - startTime) / 1000.0, 2);
            double averageSpeedInMegabytesPerSecond = round(fileSizeInMegabytes / totalTimeInSeconds, 2);

            System.out.println("Buffer size: " + bufferSizeBytes / 1024 + " KB");
            System.out.println("File size: " + fileSizeInMegabytes + " MB");
            System.out.println("Total time: " + totalTimeInSeconds + " seconds");
            System.out.println("Average download speed: " + averageSpeedInMegabytesPerSecond + " MB/s");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}