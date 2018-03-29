package com.divanxan.shovcase.json;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.divanxan.shovcase.dto.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static JSONArray readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray jsonArray = new JSONArray(jsonText);
//            JSONObject json = new JSONObject(jsonText);
//            return json;
            return jsonArray;
        } finally {
            is.close();
        }
    }


    public List<Product> getTop() throws IOException, JSONException {
        JSONArray json = readJsonFromUrl("http://192.168.99.100:8081/InternetShop_war/json/data/top");
        Gson gson = new Gson();
        List<Product> list = new ArrayList<>();
        for (int i = 0; i <json.length() ; i++) {
            Product product = gson.fromJson(json.get(i).toString(), Product.class);
            list.add(product);
        }

        return list;
    }
}
