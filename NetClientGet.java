import jdk.nashorn.internal.parser.JSONParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


class Product{
    private String name;
    private String category;
    private int price;
    private String  startDate;
    private String endDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}

public class NetClientGet {

    static HashMap<String,Integer>   cat = new HashMap ();

    public static void post(String count, HttpURLConnection httpConnection) {

        try {

          //  JSONObject data = new JSONObject(count);
          //  System.out.println(count);

          //  JSONObject json = new JSONObject(count);

           // JSONArray jsonArr = new JSONArray(json);

            Gson gson = new Gson();
            String json = gson.toJson(cat);

            //JsonParser jsonParser = new JsonParser();
           // JsonObject objectFromString = jsonParser.parse(json).getAsJsonObject();


            // URL and parameters for the connection, This particulary returns the information passed
            URL url = new URL("https://http-hunt.thoughtworks-labs.net/challenge/output");
            httpConnection  = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Cache-Control", "no-cache");
            httpConnection.setRequestProperty("userId", "B1VErBt0G");
            //httpConnection.setRequestProperty("Content-Length",String.valueOf(count.length()));

            DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
            System.out.println(json);
            wr.write(json.getBytes());
            Integer responseCode = httpConnection.getResponseCode();

            BufferedReader bufferedReader;

            // Creates a reader buffer
            if (responseCode > 199 && responseCode < 300) {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
            }

            // To receive the response
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();

            // Prints the response
            System.out.println(content.toString());

        } catch (Exception e) {
            System.out.println("Error Message");
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }

    }

    // http://localhost:8080/RESTfulExample/json/product/get
    public static void main(String[] args) throws JSONException, ParseException {

        try {

            URL url = new URL("https://http-hunt.thoughtworks-labs.net/challenge/input");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("userId", "B1VErBt0G");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

           StringBuffer output = new StringBuffer();
           String str;
            System.out.println("Output from Server .... \n");
            while ((str = br.readLine()) != null) {
               output.append(str);
            }
            System.out.println(output);

            Product[] products=new Gson().fromJson(output.toString(),Product[].class);

            Calendar start = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            today.setTime(formatter.parse("2018-05-17"));
int totalValue = 0;
            for (Product p:products) {
                System.out.println(p);

                start.setTime(formatter.parse(p.getStartDate()));
                if(p.getEndDate()!=null){
                    endDate.setTime(formatter.parse(p.getEndDate()));
                }

                if(start.before(today) && (p.getEndDate()==null || endDate.after(today))){
                    if(cat.get(p.getCategory())==null){
                        //cat.put(p.getCategory(),1);
                        totalValue+=p.getPrice();
                    }else{
                        //cat.put(p.getCategory(),(cat.get(p.getCategory())+1));
                          totalValue+=p.getPrice();
                    }


                }
            }

            System.out.println(cat);

            post("",conn);
            /*JSONArray jsonArr = new JSONArray(output.toString());
           // System.out.println(output.toString());
            int c =0;

            Calendar start = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            today.setTime(formatter.parse("2018-05-17"));

            for(int i = 0; i < jsonArr.length();i++) {
                JSONObject innerObj = jsonArr.getJSONObject(i);
                for(Iterator it = innerObj.keys(); it.hasNext(); ) {
                    String key = (String)it.next();
                    //System.out.println(key + ":" + innerObj.get(key));
                    if (key.equals("endDate")) {
                        if ( "null".equals(innerObj.get(key).toString())) {
                          //  c ++;
                            if (cat.get(innerObj.get("category")) != null && cat.containsKey(innerObj.get("category"))) {
                               int c1 = (int) cat.get(innerObj.get("category")) + 1;
                                cat.put(innerObj.get("category"), c1);
                            }else {
                                cat.put(innerObj.get("category"), 1);
                            }
                        } else {
                           // System.out.println(innerObj.get("endDate"));
                            start.setTime(formatter.parse(innerObj.get("endDate").toString()));
                            if (start.before(today)) {
                            //    c ++;
                                if (cat.get(innerObj.get("category")) != null && cat.containsKey(innerObj.get("category"))) {
                                    int c1 = (int) cat.get(innerObj.get("category")) + 1;
                                    cat.put(innerObj.get("category"), c1);
                                }else {
                                    cat.put(innerObj.get("category"), 1);
                                }
                            }
                        }

                    }

                }
            }

            String json = new ObjectMapper().writeValueAsString(cat);
           // System.out.println(json);
          // String st = cat.toString().replaceAll(":"," :");
            //System.out.println(st);

            //System.out.println(c);
            //post(json.replaceAll("\\{", "{ ").replaceAll("}"," }").replaceAll(":"," : ").replaceAll("\"",""),conn);
            post(json,conn);*/

          /*  conn.disconnect();*/

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}