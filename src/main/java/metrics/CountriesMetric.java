package metrics;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static constants.constants.COUNTRIES_DB;
import static constants.constants.IP_PATTERN;

public class CountriesMetric extends MetricAbstractClass {

    private final DatabaseReader dbReader;
    public CountriesMetric() throws IOException {
        super(Pattern.compile(IP_PATTERN));
        File database = new File(COUNTRIES_DB);
        this.dbReader=new DatabaseReader.Builder(database).build();
    }

    public DatabaseReader getDbReader() {
        return dbReader;
    }

    @Override
    public void calculateMetric(String line) {
        Matcher ipMatcher = this.getRegex().matcher(line);

        if (ipMatcher.find()) {
            String ipAddressString = ipMatcher.group(1);
            try {
                InetAddress ipAddress = InetAddress.getByName(ipAddressString);
                CountryResponse response =dbReader.country(ipAddress);
                String countryName = response.getCountry().getName();

                if (countryName != null) {
                    this.getConcurrentHashMap().put(countryName, this.getConcurrentHashMap().getOrDefault(countryName, 0.0) + 1);
                } else {
                    System.err.println("Country name not found for IP: " + ipAddressString);
                }
            } catch (IOException | GeoIp2Exception e) {
                System.err.println("Error processing IP: " + ipAddressString + " - " + e.getMessage());
            }
        }
    }

}
