package logProcessing;

import metrics.BrowsersMetric;
import metrics.MetricInterface;
import metrics.OperatingSystemsMetric;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProcessLogTest {

    private MetricInterface countriesMetric;
    private OperatingSystemsMetric operatingSystemsMetric;
    private BrowsersMetric browsersMetric;
    private ProcessLog processLog;

    @Before
    public void setUp() {
        countriesMetric = mock(MetricInterface.class);
        operatingSystemsMetric = mock(OperatingSystemsMetric.class);
        browsersMetric = mock(BrowsersMetric.class);
        processLog = new ProcessLog(countriesMetric, operatingSystemsMetric, browsersMetric);
    }

    @Test
    public void testReadRequestsFromLog_SingleChunk() throws InterruptedException {
        // Mock log data with fewer lines than LINES_PER_THREAD
        String mockLog = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326\n" +
                "192.168.0.2 - - [24/Apr/2023:06:25:25 +0000] \"GET /home.html HTTP/1.1\" 200 2326\n";

        // Use a StringReader to simulate the BufferedReader
        BufferedReader mockReader = new BufferedReader(new StringReader(mockLog));

        // Run the method
        processLog.readRequestsFromLog(mockReader);

        // Capture the argument passed to the metric calculation
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(countriesMetric, times(2)).calculateMetric(captor.capture());

        // Verify that the correct log lines were passed to calculateMetric
        assertEquals("192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326", captor.getAllValues().get(0));
        assertEquals("192.168.0.2 - - [24/Apr/2023:06:25:25 +0000] \"GET /home.html HTTP/1.1\" 200 2326", captor.getAllValues().get(1));
    }

    @Test
    public void testReadRequestsFromLog_MultipleChunks() throws InterruptedException {
        // Mock log data with more lines than LINES_PER_THREAD
        String mockLog = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326\n" +
                "192.168.0.2 - - [24/Apr/2023:06:25:25 +0000] \"GET /home.html HTTP/1.1\" 200 2326\n" +
                "192.168.0.3 - - [24/Apr/2023:06:25:26 +0000] \"GET /contact.html HTTP/1.1\" 200 2326\n";

        // Use a StringReader to simulate the BufferedReader
        BufferedReader mockReader = new BufferedReader(new StringReader(mockLog));

        // Run the method
        processLog.readRequestsFromLog(mockReader);

        // Capture the argument passed to the metric calculation
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(countriesMetric, times(3)).calculateMetric(captor.capture());

        // Verify that the correct log lines were passed to calculateMetric
        assertEquals("192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326", captor.getAllValues().get(0));
        assertEquals("192.168.0.2 - - [24/Apr/2023:06:25:25 +0000] \"GET /home.html HTTP/1.1\" 200 2326", captor.getAllValues().get(1));
        assertEquals("192.168.0.3 - - [24/Apr/2023:06:25:26 +0000] \"GET /contact.html HTTP/1.1\" 200 2326", captor.getAllValues().get(2));
    }
    @Test
    public void testReadRequestsFromLog_EmptyLogFile() throws InterruptedException {
        // Mock an empty log file
        String mockLog = "";

        // Use a StringReader to simulate the BufferedReader
        BufferedReader mockReader = new BufferedReader(new StringReader(mockLog));

        // Run the method
        processLog.readRequestsFromLog(mockReader);

        // Verify that no metrics were calculated since the log is empty
        verify(countriesMetric, never()).calculateMetric(anyString());
        verify(operatingSystemsMetric, never()).calculateMetric(anyString());
        verify(browsersMetric, never()).calculateMetric(anyString());
    }
    @Test
    public void testReadRequestsFromLog_SingleLine() throws InterruptedException {
        // Mock log data with a single line
        String mockLog = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326\n";

        // Use a StringReader to simulate the BufferedReader
        BufferedReader mockReader = new BufferedReader(new StringReader(mockLog));

        // Run the method
        processLog.readRequestsFromLog(mockReader);

        // Capture the argument passed to the metric calculation
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(countriesMetric, times(1)).calculateMetric(captor.capture());

        // Verify that the correct log line was passed to calculateMetric
        assertEquals("192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326", captor.getValue());
    }
    @Test
    public void testReadRequestsFromLog_IncompleteLastChunk() throws InterruptedException {
        // Mock log data where the last chunk is incomplete
        String mockLog = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326\n" +
                "192.168.0.2 - - [24/Apr/2023:06:25:25 +0000] \"GET /home.html HTTP/1.1\" 200 2326\n";

        // Use a StringReader to simulate the BufferedReader
        BufferedReader mockReader = new BufferedReader(new StringReader(mockLog));

        // Run the method
        processLog.readRequestsFromLog(mockReader);

        // Capture the argument passed to the metric calculation
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(countriesMetric, times(2)).calculateMetric(captor.capture());

        // Verify that the correct log lines were passed to calculateMetric
        assertEquals("192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326", captor.getAllValues().get(0));
        assertEquals("192.168.0.2 - - [24/Apr/2023:06:25:25 +0000] \"GET /home.html HTTP/1.1\" 200 2326", captor.getAllValues().get(1));
    }

    @Test
    public void testReadRequestsFromLog_ExceptionHandling() throws InterruptedException, IOException {
        // Mock a BufferedReader that throws an IOException
        BufferedReader mockReader = mock(BufferedReader.class);
        when(mockReader.readLine()).thenThrow(new IOException("Test Exception"));

        // Run the method
        processLog.readRequestsFromLog(mockReader);

        // Verify that no metrics were calculated since an exception occurred
        verify(countriesMetric, never()).calculateMetric(anyString());
        verify(operatingSystemsMetric, never()).calculateMetric(anyString());
        verify(browsersMetric, never()).calculateMetric(anyString());
    }



}
