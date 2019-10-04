package com.example.demo;

import org.json.JSONArray;
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

public class IndexController {

    static String confirmationResponse = "960cb2e7";

    static String accessToken = "c18a7cce50b4402109ccd85e0e17eb48e70e1029e857540ef3420ee4a1ed86bcd0defbce4af107112800b";


    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String type(@RequestBody String req) {
        JSONObject jsonObject = new JSONObject(req);
        if(jsonObject.get("type").equals("confirmation")) {
            return confirmationResponse;
        }
        if (jsonObject.get("type").equals("message_new")) {
            JSONObject object = new JSONObject(jsonObject.getJSONObject("object"));
            send("https://api.vk.com/method/messages.send?user_id="
                    +object.get("user_id")+
                    "&message="+object.get("body")
                    +"&access_token="+accessToken+"&v=5.101");
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
