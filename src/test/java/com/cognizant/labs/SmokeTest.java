package com.cognizant.labs;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class SmokeTest {

    private static final Log logger = LogFactory.getLog(SmokeTest.class);

    @Test
    public void testConnectivity() throws IOException {
        if (System.getProperty("url") != null) {
            //hit the
            HttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(System.getProperty("url"));
            HttpResponse response = client.execute(get);
            assertTrue(response.getStatusLine().getStatusCode() == HttpStatus.OK.value());
        } else {
            logger.warn("Bypassed as no URL set in the System.properties");
        }//end if
    }

    @Test
    public void testEndpointConnectivity() throws IOException {
        if (System.getProperty("url") != null) {
            //hit the
            HttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(System.getProperty("url") + "/persons");
            HttpResponse response = client.execute(get);
            //if not secured
            assertTrue(response.getStatusLine().getStatusCode() == HttpStatus.OK.value());
        } else {
            logger.warn("Bypassed as no URL set in the System.properties");
        }//end if
    }
}
