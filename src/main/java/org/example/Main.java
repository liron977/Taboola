package org.example;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import logProcessing.ProcessLog;
import metrics.BrowsersMetric;
import metrics.CountriesMetric;
import metrics.OperatingSystemsMetric;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, GeoIp2Exception, InterruptedException {
//        File database = new File("/Users/liron.kotev/Documents/GeoLite2-Country_20240809/GeoLite2-Country.mmdb");
//        DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
//        InetAddress ipAddress = InetAddress.getByName("72.14.199.75");
//        CountryResponse response = dbReader.country(ipAddress);
//        String countryName = response.getCountry().getName();

        //String userAgentString = "Mozilla/5.0 (iPad; CPU OS 6_0_1 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A523 Safari/8536.25";

//        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
//
//        Browser browser = userAgent.getBrowser();
//        OperatingSystem os = userAgent.getOperatingSystem();
//        System.out.println("Browser: " + browser.getName());
//        System.out.println("OS: " + os.getName());
//
//
//        String logFilePath = "/Users/liron.kotev/IdeaProjects/TaboolasHomeTest/src/resources/apache copy.log";
//        String ipPattern = "^([\\d.]+)";
//        String userAgentPattern = "\"([^\"]+)\"\\s+\"([^\"]+)\"\\s+\\d+\\s+\\d+\\s+-\\s+\\d+$";
//        String osPattern = "\\(([^)]+)\\)";
//        String browserPattern = "\"[^\"]*\"\\s\"[^\"]*\"\\s\"([^\"]+)\"";
//
//
//
//        // Compiled patterns
//        Pattern ipRegex = Pattern.compile(ipPattern);
//        Pattern userAgentRegex = Pattern.compile(userAgentPattern);
//
//        Pattern osRegex = Pattern.compile(osPattern);
//        Pattern browserRegex = Pattern.compile(browserPattern);
//        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//                // Match IP Address
//                Matcher ipMatcher = ipRegex.matcher(line);
//                String ipAddress = ipMatcher.find() ? ipMatcher.group(1) : "N/A";
//
//
//                // Match User Agent (Browser and OS)
//                Matcher userAgentMatcher = userAgentRegex.matcher(line);
//                String userAgentString = userAgentMatcher.find() ? userAgentMatcher.group(2) : "N/A";
//
//
//                UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
//
//                Browser browser = userAgent.getBrowser();
//                OperatingSystem os = userAgent.getOperatingSystem();
//                System.out.println("Browser: " + browser.getName());
//                System.out.println("OS: " + os.getName());
//
//
//                // Print extracted information
//                System.out.println("IP Address: " + ipAddress);
//                System.out.println("User Agent: " + userAgent);
//                System.out.println("-----------------------------");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();

 //   }
        CountriesMetric countriesMetric=new CountriesMetric();
        OperatingSystemsMetric operatingSystemsMetric=new OperatingSystemsMetric();
        BrowsersMetric browsersMetric=new BrowsersMetric();
        ProcessLog readRequestsFromLog=new ProcessLog(countriesMetric,operatingSystemsMetric,browsersMetric);

        readRequestsFromLog.readRequestsFromLog();
        countriesMetric.printResults();
        System.out.println("**************");
        operatingSystemsMetric.printResults();
        System.out.println("**************");
        browsersMetric.printResults();

    }
}