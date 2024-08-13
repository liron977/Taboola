package metrics;



import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CountriesMetricTest {

    private CountriesMetric countriesMetric;

    @Before
    public void setUp() throws IOException {
        countriesMetric = new CountriesMetric();
    }

    @Test
    public void testCalculateMetricValidIp() {
        String logLine = "72.14.199.75 - - [21/Jan/2013:00:00:34 -0600] \"GET /?utm_source=Contextin&utm_term=_ HTTP/1.1\" 200 9983 \"-\" \"AdsBot-Google (+http://www.google.com/adsbot.html)\" 267 10246 - 549397\n";
        System.out.println(logLine);
        assertTrue(countriesMetric.getConcurrentHashMap().size() > 0);
        assertFalse(countriesMetric.getConcurrentHashMap().isEmpty());
        assertTrue(countriesMetric.getConcurrentHashMap().containsKey("United States")); // assuming 192.168.0.1 is in the U.S.
    }

    @Test
    public void testGetResultsList() {
        String logLine = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326";
        countriesMetric.calculateMetric(logLine);
        assertFalse(countriesMetric.getResultsList().isEmpty());
    }
    @Test
    public void testCalculateMetricNoCountryInfo() throws IOException, GeoIp2Exception {
        String logLine = "10.0.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326";
        countriesMetric.calculateMetric(logLine);
        assertTrue(countriesMetric.getConcurrentHashMap().isEmpty());
    }
    @Test
    public void testPrintResults() {
         String logLine = "192.168.0.1 - - [24/Apr/2023:06:25:24 +0000] \"GET /index.html HTTP/1.1\" 200 2326";
        countriesMetric.calculateMetric(logLine);
        countriesMetric.printResults();
        // Visual inspection or use a ByteArrayOutputStream to capture output for assertion
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

}