package metrics;

import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static constants.constants.*;


public class OperatingSystemsMetric extends MetricAbstractClass {


    public OperatingSystemsMetric() throws IOException {
        super(Pattern.compile(OS_PATTERN));
    }

    @Override
    public void calculateMetric(String line) {
        Pattern userAgentRegex = Pattern.compile(USER_AGENT_PATTERN);
        Matcher userAgentMatcher = userAgentRegex.matcher(line);

        if(userAgentMatcher.find()){
            String userAgentString = userAgentMatcher.group(2);
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
            OperatingSystem os = userAgent.getOperatingSystem();

            if (os != null) {
                this.getConcurrentHashMap().put(os.getName(), this.getConcurrentHashMap().getOrDefault(os.getName(), 0.0) + 1);
            } else {
                System.err.println("Operating system not found");
            }
        }
    }

}
