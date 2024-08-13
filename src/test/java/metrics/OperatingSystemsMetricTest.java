package metrics;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OperatingSystemsMetricTest {

    private OperatingSystemsMetric operatingSystemsMetric;

    @Before
    public void setUp() throws IOException {
        operatingSystemsMetric = new OperatingSystemsMetric();
    }

    @Test
    public void testCalculateMetric_ValidUserAgent() {
        // Mock the UserAgent and OperatingSystem classes
        String logLine = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326 \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36\"";
        operatingSystemsMetric.calculateMetric(logLine);

        // Check that the OS was added to the ConcurrentHashMap
        assertTrue(operatingSystemsMetric.getConcurrentHashMap().containsKey("Windows 10"));
    }

    @Test
    public void testCalculateMetric_InvalidUserAgent() {
        String logLine = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326 \"InvalidUserAgentString\"";
        operatingSystemsMetric.calculateMetric(logLine);

        // Check that the map is still empty since no valid OS was found
        assertTrue(operatingSystemsMetric.getConcurrentHashMap().isEmpty());
    }

    @Test
    public void testCalculateMetric_NoUserAgent() {
        String logLine = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326";
        operatingSystemsMetric.calculateMetric(logLine);

        // Check that the map is still empty since there was no user agent
        assertTrue(operatingSystemsMetric.getConcurrentHashMap().isEmpty());
    }

    @Test
    public void testCalculateMetric_NullOperatingSystem() {
        // Mocking a log line with a valid UserAgent but an OS that cannot be identified
        String logLine = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326 \"Mozilla/5.0 (UnknownOS) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36\"";
        operatingSystemsMetric.calculateMetric(logLine);

        // The map should remain empty as the OS was not found
        assertTrue(operatingSystemsMetric.getConcurrentHashMap().isEmpty());
    }

    @Test
    public void testCalculateMetric_MultipleLogLines() {
        String logLine1 = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326 \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36\"";
        String logLine2 = "192.168.0.2 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326 \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.3 Safari/605.1.15\"";
        operatingSystemsMetric.calculateMetric(logLine1);
        operatingSystemsMetric.calculateMetric(logLine2);

        // Check that both OSes were added to the map
        assertEquals(2, operatingSystemsMetric.getConcurrentHashMap().size());
        assertTrue(operatingSystemsMetric.getConcurrentHashMap().containsKey("Windows 10"));
        assertTrue(operatingSystemsMetric.getConcurrentHashMap().containsKey("Mac OS X"));
    }

    @Test
    public void testPrintResults() {
        // Capture the console output for testing printResults
        String logLine = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326 \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36\"";
        operatingSystemsMetric.calculateMetric(logLine);

        // Use ByteArrayOutputStream to capture output for assertions
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        operatingSystemsMetric.printResults();

        String output = outContent.toString().trim();
        assertTrue(output.contains("Windows 10"));
    }

}
