package org.example;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import logProcessing.ProcessLog;
import metrics.BrowsersMetric;
import metrics.CountriesMetric;
import metrics.OperatingSystemsMetric;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, GeoIp2Exception, InterruptedException {

        CountriesMetric countriesMetric=new CountriesMetric();
        OperatingSystemsMetric operatingSystemsMetric=new OperatingSystemsMetric();
        BrowsersMetric browsersMetric=new BrowsersMetric();
        ProcessLog readRequestsFromLog=new ProcessLog(countriesMetric,operatingSystemsMetric,browsersMetric);

        readRequestsFromLog.readRequestsFromLog();
        countriesMetric.printResults();
        operatingSystemsMetric.printResults();
        browsersMetric.printResults();

    }
}