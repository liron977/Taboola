package constants;

public class constants {
    public  static final int LINES_PER_THREAD = 3; // Number of lines each thread processes
    public static final int NUM_THREADS = 10; // Number of threads to use
   // public static final String FILE_PATH = "/Users/liron.kotev/IdeaProjects/TaboolasHomeTest/src/resources/apache copy.log";
    public static final String FILE_PATH = "/Users/liron.kotev/IdeaProjects/TaboolasHomeTest/src/resources/apache.log";
    public static final String IP_PATTERN = "^([\\d.]+)";
    public static final String USER_AGENT_PATTERN = "\"([^\"]+)\"\\s+\"([^\"]+)\"\\s+\\d+\\s+\\d+\\s+-\\s+\\d+$";
    public static final String OS__PATTERN = "\\(([^)]+)\\)";
    public static final String BROWSER_PATTERN = "\"[^\"]*\"\\s\"[^\"]*\"\\s\"([^\"]+)\"";

    public static final String COUNTRIES_DB ="/Users/liron.kotev/Documents/GeoLite2-Country_20240809/GeoLite2-Country.mmdb";
}
