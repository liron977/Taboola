package metrics;

import com.maxmind.geoip2.DatabaseReader;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static constants.constants.COUNTRIES_DB;

public abstract class MetricAbstractClass implements MetricInterface {

    private final Pattern regex;
    private final ConcurrentHashMap<String,Double> concurrentHashMap;

    public MetricAbstractClass(Pattern regex) throws IOException {
        this.concurrentHashMap=new ConcurrentHashMap<>();
        this.regex=regex;

    }

    public Pattern getRegex() {
        return regex;
    }


    public ConcurrentHashMap<String, Double> getConcurrentHashMap() {
        return concurrentHashMap;
    }

    @Override
    public abstract void  calculateMetric(String line);

    @Override
    public List<String> getResultsList() {
        List<String> countriesResult = new ArrayList<>();
        double usersAmount = 0;

        // Calculate total number of users
        for (double count : this.concurrentHashMap.values()) {
            usersAmount += count;
        }

        // Calculate percentage for each country
        for (String key : this.concurrentHashMap.keySet()) {
            double count = this.concurrentHashMap.get(key);
            double percentage = ((double) count / usersAmount) * 100;

            this.concurrentHashMap.put(key,percentage);

        }

        HashMap<String, Double> sortedHashMap=sortByValueDescending(this.concurrentHashMap);
        for (String key : sortedHashMap.keySet()) {

            String formattedPercentage = String.format("%.2f", sortedHashMap.get(key));
            String countryEntity = key + " " + formattedPercentage + "%";

            countriesResult.add(countryEntity);
        }


        return countriesResult;
    }

    @Override
    public void printResults(){
        List<String> resultsList=getResultsList();

        for (String result:resultsList) {
            System.out.println(result);
        }
        System.out.println();
    }
    private  HashMap<String, Double> sortByValueDescending(ConcurrentHashMap<String, Double> map) {
        // Convert map to list of entries
        List<Map.Entry<String, Double>> list = new ArrayList<>(map.entrySet());

        // Sort the list based on values in descending order
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Put sorted entries back into a LinkedHashMap
        LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
