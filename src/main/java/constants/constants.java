package constants;

public class constants {
    public  static final int LINES_PER_THREAD = 3; // Number of lines each thread processes
    public static final int NUM_THREADS = 10; // Number of threads to use
    public static final String FILE_PATH = "src/resources/apache.log";
    public static final String IP_PATTERN = "^([\\d.]+)";
    public static final String USER_AGENT_PATTERN = "\"([^\"]+)\"\\s+\"([^\"]+)\"\\s+\\d+\\s+\\d+\\s+-\\s+\\d+$";
    public static final String OS_PATTERN = "\\(([^)]+)\\)";
    public static final String BROWSER_PATTERN = "\"[^\"]*\"\\s\"[^\"]*\"\\s\"([^\"]+)\"";
    public static final String API_KEY = "8f3b09d1cab64f2aa656f02616d070db";

    public static final String COUNTRIES_DB ="src/resources/GeoLite2-Country_20240809/GeoLite2-Country.mmdb";
}
