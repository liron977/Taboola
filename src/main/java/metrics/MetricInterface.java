package metrics;

import java.util.List;

public interface MetricInterface {


    public void calculateMetric(String line);
    public List<String> getResultsList();
    public void printResults();
}
