package dto;

import com.ibm.security.appscan.Log4Bigbucks;
import com.ibm.security.appscan.bigbucks.dto.Headers;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class HeadersTest extends TestCase {
    @Test
    public void test(){
        List<Headers> headers = new ArrayList<>();
        headers.add(new Headers("Symbol", "Date", "High(price)", "Low(price)", "closed(price)"));
    }

    @Test
    public void testLog4(){
        Log4Bigbucks.getInstance().logInfo("Database initialized");
        System.out.println(Log4Bigbucks.getInstance().getLogFileLocation());
    }

}