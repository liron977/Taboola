package metrics;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static constants.constants.*;

public class BrowsersMetric extends MetricAbstractClass{
    public BrowsersMetric() throws IOException {
        super(Pattern.compile(BROWSER_PATTERN));
    }

    @Override
    public void calculateMetric(String line) {
        Pattern userAgentRegex = Pattern.compile(USER_AGENT_PATTERN);
        Matcher userAgentMatcher = userAgentRegex.matcher(line);

        if(userAgentMatcher.find()){
            String userAgentString = userAgentMatcher.group(2);
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
            Browser browser = userAgent.getBrowser();

            if (browser != null) {
                this.updateConcurrentHashMap(browser.getName());
            } else {
                System.err.println("Browser not found");
            }
        }
    }
}
