package com.example.demo;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@RestController
@RequestMapping(value = "/")
public class IndexController {

    static String confirmationResponse = "960cb2e7";

    static String key = "c18a7cce50b4402109ccd85e0e17eb48e70e1029e857540ef3420ee4a1ed86bcd0defbce4af107112800b";


    @PostMapping("/{type}")
    public String type(@PathVariable("type") String type) {
        if(type.equals("confirmation")) {
            return key;
        }
        if (type.equals("message_new")) {
            return "ok";
        }
        return null;
    }

    public void send(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response);
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
