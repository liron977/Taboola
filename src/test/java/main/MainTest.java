package main;


import com.maxmind.geoip2.exception.GeoIp2Exception;
import logProcessing.ProcessLog;
import metrics.BrowsersMetric;
import metrics.CountriesMetric;
import metrics.OperatingSystemsMetric;
import org.example.Main;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.BufferedReader;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class MainTest {

    @Mock
    private CountriesMetric countriesMetric;

    @Mock
    private OperatingSystemsMetric operatingSystemsMetric;

    @Mock
    private BrowsersMetric browsersMetric;

    @Mock
    private ProcessLog processLog;

    @Mock
    private BufferedReader bufferedReader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMain() throws IOException, GeoIp2Exception, InterruptedException {
        // Arrange
        doNothing().when(processLog).readRequestsFromLog(any(BufferedReader.class));
        doNothing().when(countriesMetric).printResults();
        doNothing().when(operatingSystemsMetric).printResults();
        doNothing().when(browsersMetric).printResults();

        // Mock the behavior of BufferedReader
        when(bufferedReader.readLine()).thenReturn(null); // Simulate end of file

        // Act
        Main.main(new String[]{});

        // Assert
        verify(processLog).readRequestsFromLog(any(BufferedReader.class));
        verify(countriesMetric).printResults();
        verify(operatingSystemsMetric).printResults();
        verify(browsersMetric).printResults();
    }
}
