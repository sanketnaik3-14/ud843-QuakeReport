package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanket on 01/03/17.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String link;
    public EarthquakeLoader(Context context, String url) {
        super(context);
        link = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {

        URL url = createUrl(link);
        String json = "";

        try
        {
            json = makeHttpRequest(url);
        }
        catch(IOException e)
        {
            Log.e("EarthquakeLoader","IOException" ,e);
        }

        if(TextUtils.isEmpty(json))
        {
            return null;
        }

        List<Earthquake> earthquakes = new ArrayList<Earthquake>();
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray jsonArray = jsonObject.getJSONArray("features");


            for(int i = 0; i<jsonArray.length(); i++)
            {
                JSONObject obj = jsonArray.getJSONObject(i);

                JSONObject properties = obj.getJSONObject("properties");

                Double magnitude = properties.getDouble("mag");
                String place = properties.getString("place");
                long time = properties.getLong("time");
                String details = properties.getString("url");

                earthquakes.add(new Earthquake(magnitude ,place ,time ,details));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("EarthquakeLoader", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;

    }

    public static URL createUrl(String link)
    {
        URL url = null;
        try
        {
            url = new URL(link);
        }catch (MalformedURLException e) {
            Log.e("EarthquakeLoader", "Error with creating URL ", e);
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse = "";

        if(url == null)
        {
            return jsonResponse;
        }

        HttpURLConnection connection = null;
        InputStream input = null;

        try
        {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() == 200)
            {
                input = connection.getInputStream();
                jsonResponse = readFromStream(input);
            }
            else
            {
                Log.e("EarthquakeLoader","Error response code :" + connection.getResponseCode());
            }
        }catch (IOException e)
        {
            Log.e("EarthquakeLoader","Problem retreiving string" ,e);
        }

        return jsonResponse;
    }

    public static String readFromStream(InputStream is) throws IOException
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(is != null) {
            InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                line = reader.readLine();
            }
        }
        return stringBuilder.toString();
    }
}
