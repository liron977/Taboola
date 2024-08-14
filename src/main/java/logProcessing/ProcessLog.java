package logProcessing;

import lombok.Value;
import metrics.BrowsersMetric;
import metrics.MetricInterface;
import metrics.OperatingSystemsMetric;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static constants.constants.*;

public class ProcessLog {
    private final MetricInterface countriesMetric;
    private final OperatingSystemsMetric operatingSystemsMetric;
    private final BrowsersMetric browsersMetric;

    public ProcessLog(MetricInterface countriesMetric, OperatingSystemsMetric operatingSystemsMetric, BrowsersMetric browsersMetric){
        this.countriesMetric=countriesMetric;
        this.operatingSystemsMetric=operatingSystemsMetric;
        this.browsersMetric=browsersMetric;
    }
    public void readRequestsFromLog(BufferedReader bufferedReader) throws InterruptedException {

            ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

            try (BufferedReader reader =bufferedReader) {
                String line;
                int lineCount = 0;
                StringBuilder chunk = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    chunk.append(line).append(System.lineSeparator());
                    lineCount++;

                    // Process the chunk once we reach the desired number of lines
                    if (lineCount == LINES_PER_THREAD) {
                        final String chunkToProcess = chunk.toString();
                        executorService.submit(new LinesProcessing(chunkToProcess,countriesMetric,operatingSystemsMetric,browsersMetric));
                        chunk.setLength(0); // Reset the chunk builder
                        lineCount = 0; // Reset line counter
                    }
                }

                // Process any remaining lines in the final chunk
                if (lineCount > 0) {
                    final String remainingChunk = chunk.toString();
                    executorService.submit(new LinesProcessing(remainingChunk,countriesMetric,operatingSystemsMetric,browsersMetric));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        // Shutdown the executor service and wait for all tasks to complete
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        }

}
