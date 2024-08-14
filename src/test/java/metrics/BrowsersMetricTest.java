package metrics;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static constants.constants.*;
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

        // Mock the Browser and UserAgent objects
        Browser mockBrowser = mock(Browser.class);
        UserAgent mockUserAgent = mock(UserAgent.class);

        // Stub the methods to return expected values
        when(mockBrowser.getName()).thenReturn("Chrome");
        when(mockUserAgent.getBrowser()).thenReturn(mockBrowser);

        // Mock the static method UserAgent.parseUserAgentString
        try (MockedStatic<UserAgent> mockedUserAgent = mockStatic(UserAgent.class)) {
            mockedUserAgent.when(() -> UserAgent.parseUserAgentString(anyString())).thenReturn(mockUserAgent);

            // Call the method under test
            browsersMetric.calculateMetric(logLine);

            // Verify that the browser "Chrome" was correctly added to the map
            assertEquals(1.0, concurrentHashMap.get("Chrome"), 0.001);
        }
    }


//
//    @Test
//    public void testCalculateMetric_InvalidUserAgent() {
//        String logLine = "User-Agent: Some invalid user agent string";
//
//        // Simulate UserAgent parsing with no browser found
//        UserAgent mockUserAgent = mock(UserAgent.class);
//        when(mockUserAgent.getBrowser()).thenReturn(null);
//        when(UserAgent.parseUserAgentString(anyString())).thenReturn(mockUserAgent);
//
//        // Call the method
//        browsersMetric.calculateMetric(logLine);
//
//        // Verify that nothing is added to the map
//        assertEquals(0, concurrentHashMap.size());
//    }
//
//    @Test
//    public void testCalculateMetric_EmptyLogLine() {
//        String logLine = "";
//
//        // Call the method
//        browsersMetric.calculateMetric(logLine);
//
//        // Verify that nothing is added to the map
//        assertEquals(0, concurrentHashMap.size());
//    }
//    @Test
//    public void testCalculateMetric_MultipleBrowsers() {
//        String logLine1 = "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
//        String logLine2 = "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Firefox/89.0 Safari/537.36";
//
//        // Simulate UserAgent parsing
//        UserAgent mockUserAgentChrome = mock(UserAgent.class);
//        Browser mockBrowserChrome = mock(Browser.class);
//        when(mockBrowserChrome.getName()).thenReturn("Chrome");
//        when(mockUserAgentChrome.getBrowser()).thenReturn(mockBrowserChrome);
//
//        UserAgent mockUserAgentFirefox = mock(UserAgent.class);
//        Browser mockBrowserFirefox = mock(Browser.class);
//        when(mockBrowserFirefox.getName()).thenReturn("Firefox");
//        when(mockUserAgentFirefox.getBrowser()).thenReturn(mockBrowserFirefox);
//
//        when(UserAgent.parseUserAgentString(contains("Chrome"))).thenReturn(mockUserAgentChrome);
//        when(UserAgent.parseUserAgentString(contains("Firefox"))).thenReturn(mockUserAgentFirefox);
//
//        // Call the method with multiple log lines
//        browsersMetric.calculateMetric(logLine1);
//        browsersMetric.calculateMetric(logLine2);
//
//        // Verify the results
//        assertEquals(1.0, concurrentHashMap.get("Chrome"), 0.001);
//        assertEquals(1.0, concurrentHashMap.get("Firefox"), 0.001);
//    }
//    @Test
//    public void testCalculateMetric_SameBrowserMultipleTimes() {
//        String logLine = "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
//
//        // Simulate UserAgent parsing
//        UserAgent mockUserAgent = mock(UserAgent.class);
//        Browser mockBrowser = mock(Browser.class);
//        when(mockBrowser.getName()).thenReturn("Chrome");
//        when(mockUserAgent.getBrowser()).thenReturn(mockBrowser);
//        when(UserAgent.parseUserAgentString(anyString())).thenReturn(mockUserAgent);
//
//        // Call the method multiple times
//        browsersMetric.calculateMetric(logLine);
//        browsersMetric.calculateMetric(logLine);
//
//        // Verify the results
//        assertEquals(2.0, concurrentHashMap.get("Chrome"), 0.001);
//    }
//    @Test
//    public void testCalculateMetric_EdgeCaseUserAgent() {
//        String logLine = "User-Agent: ";
//
//        // Simulate UserAgent parsing with an empty user agent string
//        UserAgent mockUserAgent = mock(UserAgent.class);
//        when(UserAgent.parseUserAgentString(anyString())).thenReturn(mockUserAgent);
//
//        // Call the method
//        browsersMetric.calculateMetric(logLine);
//
//        // Verify that nothing is added to the map
//        assertEquals(0, concurrentHashMap.size());
//    }
//    @Test(expected = NullPointerException.class)
//    public void testCalculateMetric_NullLogLine() {
//        String logLine = null;
//
//        // Call the method
//        browsersMetric.calculateMetric(logLine);
//
//    }

}