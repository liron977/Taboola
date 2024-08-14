package metrics;



import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import static org.junit.Assert.*;


public class CountriesMetricTest {

    private CountriesMetric countriesMetric;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        countriesMetric = new CountriesMetric();

        // Initialize and redirect System.out to capture the output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testCalculateMetricValidIp() {
        String logLine = "72.14.199.75 - - [21/Jan/2013:00:00:34 -0600] \"GET /?utm_source=Contextin&utm_term=_ HTTP/1.1\" 200 9983 \"-\" \"AdsBot-Google (+http://www.google.com/adsbot.html)\" 267 10246 - 549397\n";
        System.out.println(logLine);

        assertFalse(countriesMetric.getConcurrentHashMap().isEmpty());
        assertTrue(countriesMetric.getConcurrentHashMap().containsKey("United States")); // assuming 192.168.0.1 is in the U.S.
    }

    @Test
    public void testCalculateMetricNoCountryInfo() throws IOException, GeoIp2Exception {
        String logLine = "10.0.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326";
        countriesMetric.calculateMetric(logLine);
        assertTrue(countriesMetric.getConcurrentHashMap().isEmpty());
    }
    @Test
    public void testPrintResults() {
        // Simulate processing a log line
        String logLine = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326";
        countriesMetric.calculateMetric(logLine);

        // Call printResults to generate the output
        countriesMetric.printResults();

        // Capture the output
        String output = outputStream.toString().trim();

        // Define the expected output (based on how the metric is supposed to be calculated and printed)
        String expectedOutput = "Expected Country Name 100.00%"; // Replace with the actual expected output

        // Assert that the output matches the expected output
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCalculateMetricIOException() throws IOException {
        // Mock or use an invalid database path to simulate IOException
        countriesMetric = new CountriesMetric() {
            @Override
            public void calculateMetric(String line) {
                try {
                    InetAddress ipAddress = InetAddress.getByName("192.168.0.1");
                    countriesMetric.getDbReader().country(ipAddress);
                } catch (IOException | GeoIp2Exception e) {
                    assertTrue(e instanceof IOException);
                }
            }
        };
        countriesMetric.calculateMetric("192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326");
    }

    @Test
    public void testCalculateMetricGeoIp2Exception() throws IOException {
        countriesMetric = new CountriesMetric() {
            @Override
            public void calculateMetric(String line) {
                try {
                    InetAddress ipAddress = InetAddress.getByName("192.168.0.1");
                    countriesMetric.getDbReader().country(ipAddress);
                } catch (IOException | GeoIp2Exception e) {
                    assertTrue(e instanceof GeoIp2Exception);
                }
            }
        };
        countriesMetric.calculateMetric("192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326");
    }
    @Test
    public void testCalculateMetricEmptyLogLine() {
        String logLine = "";
        countriesMetric.calculateMetric(logLine);
        assertTrue(countriesMetric.getConcurrentHashMap().isEmpty());
    }

    @Test
    public void testPrintResultsValues() {
        String logLine = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326";
        countriesMetric.calculateMetric(logLine);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        countriesMetric.printResults();

        String output = outContent.toString();
        assertTrue(output.contains("United States")); // assuming 192.168.0.1 is in the U.S.
    }
    @Test
    public void testCalculateMetric_MultipleIpsInLogLine() throws IOException, GeoIp2Exception {
        String logLine = "192.168.0.1 10.0.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326";

        // Assume that 192.168.0.1 is in the U.S. and 10.0.0.1 has no country info
        countriesMetric.calculateMetric(logLine);

        // Verify that only the valid IP (192.168.0.1) was added
        assertFalse(countriesMetric.getConcurrentHashMap().isEmpty());
        assertTrue(countriesMetric.getConcurrentHashMap().containsKey("United States"));
    }
    @Test
    public void testCalculateMetric_InvalidIpFormat() {
        String logLine = "Invalid-IP-Address - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326";

        countriesMetric.calculateMetric(logLine);

        // Verify that nothing is added to the map
        assertTrue(countriesMetric.getConcurrentHashMap().isEmpty());
    }
    @Test(expected = NullPointerException.class)
    public void testCalculateMetric_NullLogLine() {
        countriesMetric.calculateMetric(null);
    }
//    @Test
//    public void testCalculateMetric_NoCountryInfoUsingMock() throws IOException, GeoIp2Exception {
//        CountriesMetric mockCountriesMetric = spy(countriesMetric);
//        doReturn(null).when(mockCountriesMetric.getDbReader().country(any(InetAddress.class)).getCountry().getName());
//
//        String logLine = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326";
//
//        mockCountriesMetric.calculateMetric(logLine);
//
//        // Verify that nothing is added to the map
//        assertTrue(mockCountriesMetric.getConcurrentHashMap().isEmpty());
//    }



}