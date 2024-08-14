package metrics;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import io.ipgeolocation.api.Geolocation;
import io.ipgeolocation.api.GeolocationParams;
import io.ipgeolocation.api.IPGeolocationAPI;
import io.ipgeolocation.api.exceptions.IPGeolocationError;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static constants.constants.*;


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


                //if we use the api
               // String countryName=getCountryFromIp(ipAddressString);

                if (countryName != null) {
                    this.updateConcurrentHashMap(countryName);
                } else {
                    System.err.println("Country name not found for IP: " + ipAddressString);
                }
            } catch (IOException e) {
                System.err.println("Error processing IP: " + ipAddressString + " - " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    //I wanted to use the api but, I reached the request limit
//    public static String getCountryFromIp(String ipAddress) throws Exception {
//
//        String apiUrl = "https://api.ipgeolocation.io/ipgeo?apiKey=" + API_KEY + "&ip=" + ipAddress;
//        URL url = new URL(apiUrl);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        String inputLine;
//        StringBuilder content = new StringBuilder();
//
//        while ((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//
//        in.close();
//        connection.disconnect();
//
//        JSONObject jsonObject = new JSONObject(content.toString());
//
//        return jsonObject.optString("country_name", "Unknown");
//    }

}
