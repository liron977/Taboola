package logProcessing;

import metrics.BrowsersMetric;
import metrics.CountriesMetric;
import metrics.MetricInterface;
import metrics.OperatingSystemsMetric;

import java.io.IOException;

public class LinesProcessing implements Runnable{

    private final String linesToProcess;
    private final MetricInterface countriesMetric;
    private final  OperatingSystemsMetric operatingSystemsMetric;
    private final BrowsersMetric browsersMetric;


    public LinesProcessing(String linesToProcess, MetricInterface countriesMetric, OperatingSystemsMetric operatingSystemsMetric, BrowsersMetric browsersMetric){
        this.linesToProcess=linesToProcess;
        this.countriesMetric=countriesMetric;
        this.operatingSystemsMetric=operatingSystemsMetric;
        this.browsersMetric=browsersMetric;
    }


    @Override
    public void run() {
        String[] lines = linesToProcess.split(System.lineSeparator());
          for (String line : lines) {
            if (!line.trim().isEmpty()) {
                countriesMetric.calculateMetric(line);
                operatingSystemsMetric.calculateMetric(line);
                browsersMetric.calculateMetric(line);
            }
        }
    }


}
