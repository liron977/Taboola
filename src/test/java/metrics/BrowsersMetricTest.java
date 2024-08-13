package metrics;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static constants.constants.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class BrowsersMetricTest {

    private BrowsersMetric browsersMetric;
    private ConcurrentHashMap<String, Double> concurrentHashMap;

    @Before
    public void setUp() throws IOException {
        // Mock the MetricAbstractClass's ConcurrentHashMap
        concurrentHashMap = new ConcurrentHashMap<>();
        browsersMetric = spy(new BrowsersMetric());
        doReturn(concurrentHashMap).when(browsersMetric).getConcurrentHashMap();
    }

    @Test
    public void testCalculateMetric_ValidUserAgent() {
        String logLine = "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";

        // Simulate UserAgent parsing
        UserAgent mockUserAgent = mock(UserAgent.class);
        Browser mockBrowser = mock(Browser.class);

        when(mockBrowser.getName()).thenReturn("Chrome");
        when(mockUserAgent.getBrowser()).thenReturn(mockBrowser);
        when(UserAgent.parseUserAgentString(anyString())).thenReturn(mockUserAgent);

        // Call the method
        browsersMetric.calculateMetric(logLine);

        // Verify the results
        assertEquals(1.0, concurrentHashMap.get("Chrome"), 0.001);
    }

    @Test
    public void testCalculateMetric_InvalidUserAgent() {
        String logLine = "User-Agent: Some invalid user agent string";

        // Simulate UserAgent parsing with no browser found
        UserAgent mockUserAgent = mock(UserAgent.class);
        when(mockUserAgent.getBrowser()).thenReturn(null);
        when(UserAgent.parseUserAgentString(anyString())).thenReturn(mockUserAgent);

        // Call the method
        browsersMetric.calculateMetric(logLine);

        // Verify that nothing is added to the map
        assertEquals(0, concurrentHashMap.size());
    }

    @Test
    public void testCalculateMetric_EmptyLogLine() {
        String logLine = "";

        // Call the method
        browsersMetric.calculateMetric(logLine);

        // Verify that nothing is added to the map
        assertEquals(0, concurrentHashMap.size());
    }
}