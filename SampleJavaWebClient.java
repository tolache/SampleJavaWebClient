import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SampleJavaWebClient {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java SampleJavaWebClient <\"Header1: Value1[, Header2: Value2]\"> <DownloadURL> <OutputFile>");
            System.exit(1);
        }

        String headers = args[0];
        String downloadUrl = args[1];
        String outputFile = args[2];

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
                byte[] buffer = new byte[65536];
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
            double totalTimeInSeconds = (endTime - startTime) / 1000.0;
            double averageSpeedInMegabytesPerSecond = fileSizeInMegabytes / totalTimeInSeconds;

            System.out.println("File size: " + fileSizeInMegabytes + " MB");
            System.out.println("Total time: " + totalTimeInSeconds + " seconds");
            System.out.println("Average download speed: " + averageSpeedInMegabytesPerSecond + " MB/s");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}