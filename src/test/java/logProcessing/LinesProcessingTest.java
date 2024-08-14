package logProcessing;

import metrics.BrowsersMetric;
import metrics.MetricInterface;
import metrics.OperatingSystemsMetric;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class LinesProcessingTest {

    private MetricInterface countriesMetric;
    private OperatingSystemsMetric operatingSystemsMetric;
    private BrowsersMetric browsersMetric;

    @BeforeEach
    public void setUp() {
        countriesMetric = mock(MetricInterface.class);
        operatingSystemsMetric = mock(OperatingSystemsMetric.class);
        browsersMetric = mock(BrowsersMetric.class);
    }

    @Test
    public void testRun_WithValidLines() {
        String linesToProcess = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326\n" +
                "192.168.0.2 - - [24/Apr/2023:06:25:25 +0000] \"GET /home.html HTTP/1.1\" 200 2326\n";

        LinesProcessing linesProcessing = new LinesProcessing(linesToProcess, countriesMetric, operatingSystemsMetric, browsersMetric);

        linesProcessing.run();

        // Capture the argument passed to the metric calculation
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Verify that the correct log lines were passed to calculateMetric in each metric
        verify(countriesMetric, times(2)).calculateMetric(captor.capture());
        verify(operatingSystemsMetric, times(2)).calculateMetric(captor.capture());
        verify(browsersMetric, times(2)).calculateMetric(captor.capture());

        Assertions.assertEquals("192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326", captor.getAllValues().get(0));
        Assertions.assertEquals("192.168.0.2 - - [24/Apr/2023:06:25:25 +0000] \"GET /home.html HTTP/1.1\" 200 2326", captor.getAllValues().get(1));
    }

    @Test
    public void testRun_WithEmptyLines() {
        String linesToProcess = "\n\n";

        LinesProcessing linesProcessing = new LinesProcessing(linesToProcess, countriesMetric, operatingSystemsMetric, browsersMetric);

        linesProcessing.run();

        // Verify that calculateMetric is never called since lines are empty
        verify(countriesMetric, never()).calculateMetric(anyString());
        verify(operatingSystemsMetric, never()).calculateMetric(anyString());
        verify(browsersMetric, never()).calculateMetric(anyString());
    }

    @Test
    public void testRun_WithMixedLines() {
        String linesToProcess = "   \n192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326\n   \n";

        LinesProcessing linesProcessing = new LinesProcessing(linesToProcess, countriesMetric, operatingSystemsMetric, browsersMetric);

        linesProcessing.run();

        // Capture the argument passed to the metric calculation
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Verify that the correct log lines were passed to calculateMetric in each metric
        verify(countriesMetric, times(1)).calculateMetric(captor.capture());
        verify(operatingSystemsMetric, times(1)).calculateMetric(captor.capture());
        verify(browsersMetric, times(1)).calculateMetric(captor.capture());

        Assertions.assertEquals("192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326", captor.getAllValues().get(0));
    }

    @Test
    public void testRun_WithNoLines() {
        String linesToProcess = "";

        LinesProcessing linesProcessing = new LinesProcessing(linesToProcess, countriesMetric, operatingSystemsMetric, browsersMetric);

        linesProcessing.run();

        // Verify that calculateMetric is never called since there are no lines to process
        verify(countriesMetric, never()).calculateMetric(anyString());
        verify(operatingSystemsMetric, never()).calculateMetric(anyString());
        verify(browsersMetric, never()).calculateMetric(anyString());
    }
    @Test
    public void testRun_WithSpecialCharacters() {
        String linesToProcess = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326\n" +
                "192.168.0.2 - - [24/Apr/2023:06:25:25 +0000] \"GET /home.html?name=张三 HTTP/1.1\" 200 2326\n";

        LinesProcessing linesProcessing = new LinesProcessing(linesToProcess, countriesMetric, operatingSystemsMetric, browsersMetric);

        linesProcessing.run();

        // Capture the argument passed to the metric calculation
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Verify that the correct log lines were passed to calculateMetric in each metric
        verify(countriesMetric, times(2)).calculateMetric(captor.capture());
        verify(operatingSystemsMetric, times(2)).calculateMetric(captor.capture());
        verify(browsersMetric, times(2)).calculateMetric(captor.capture());

        Assertions.assertEquals("192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326", captor.getAllValues().get(0));
        Assertions.assertEquals("192.168.0.2 - - [24/Apr/2023:06:25:25 +0000] \"GET /home.html?name=张三 HTTP/1.1\" 200 2326", captor.getAllValues().get(1));
    }
    @Test
    public void testRun_WithLongRequestLines() {
        String longRequest = "GET /" + "a".repeat(5000) + " HTTP/1.1";
        String linesToProcess = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"" + longRequest + "\" 200 2326\n";

        LinesProcessing linesProcessing = new LinesProcessing(linesToProcess, countriesMetric, operatingSystemsMetric, browsersMetric);

        linesProcessing.run();

        // Capture the argument passed to the metric calculation
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Verify that the correct log line was passed to calculateMetric in each metric
        verify(countriesMetric, times(1)).calculateMetric(captor.capture());
        verify(operatingSystemsMetric, times(1)).calculateMetric(captor.capture());
        verify(browsersMetric, times(1)).calculateMetric(captor.capture());

        Assertions.assertEquals("192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"" + longRequest + "\" 200 2326", captor.getValue());
    }
    @Test
    public void testRun_WithDifferentDateFormats() {
        String linesToProcess = "192.168.0.1 - - [24-Apr-2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326\n" +
                "192.168.0.2 - - [2023/04/24 06:25:25] \"GET /home.html HTTP/1.1\" 200 2326\n";

        LinesProcessing linesProcessing = new LinesProcessing(linesToProcess, countriesMetric, operatingSystemsMetric, browsersMetric);

        linesProcessing.run();

        // Capture the argument passed to the metric calculation
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // Verify that the correct log lines were passed to calculateMetric in each metric
        verify(countriesMetric, times(2)).calculateMetric(captor.capture());
        verify(operatingSystemsMetric, times(2)).calculateMetric(captor.capture());
        verify(browsersMetric, times(2)).calculateMetric(captor.capture());

        Assertions.assertEquals("192.168.0.1 - - [24-Apr-2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326", captor.getAllValues().get(0));
        Assertions.assertEquals("192.168.0.2 - - [2023/04/24 06:25:25] \"GET /home.html HTTP/1.1\" 200 2326", captor.getAllValues().get(1));
    }

}