package org.example;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import logProcessing.ProcessLog;
import lombok.Value;
import metrics.BrowsersMetric;
import metrics.CountriesMetric;
import metrics.OperatingSystemsMetric;
import org.json.JSONObject;

import java.io.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


import static constants.constants.FILE_PATH;

public class Main {


    public static void main(String[] args) throws IOException, GeoIp2Exception, InterruptedException {


        CountriesMetric countriesMetric=new CountriesMetric();
        OperatingSystemsMetric operatingSystemsMetric=new OperatingSystemsMetric();
        BrowsersMetric browsersMetric=new BrowsersMetric();
        ProcessLog readRequestsFromLog=new ProcessLog(countriesMetric,operatingSystemsMetric,browsersMetric);

        File logFile = new File(FILE_PATH);
        BufferedReader bufferedReader= new BufferedReader(new FileReader(logFile));

        readRequestsFromLog.readRequestsFromLog(bufferedReader);

        countriesMetric.printResults();
        operatingSystemsMetric.printResults();
        browsersMetric.printResults();


    }

}