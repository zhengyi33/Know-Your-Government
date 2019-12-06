package com.yizheng.knowyourgovernment;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OfficialsDownloader extends AsyncTask<String,Void,String> {

    private static final String TAG = "OfficialsDownloader";

    private MainActivity mainActivity;
    private static final String apiBase = "https://www.googleapis.com/civicinfo/v2";

    public OfficialsDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    protected void onPostExecute(String s){
        if (s == null){
            Log.d(TAG, "onPostExecute: doInBackground returned null.");
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject normalizedInput = jsonObject.getJSONObject("normalizedInput");

            String city = normalizedInput.getString("city");
            String state = normalizedInput.getString("state");
            String zip = normalizedInput.getString("zip");
            String location = city+", "+state+" "+zip;

            JSONArray offices = jsonObject.getJSONArray("offices");

            JSONArray officials = jsonObject.getJSONArray("officials");

            ArrayList<Official> returnedOfficials = new ArrayList<>();

            for (int i=0; i<officials.length(); i++){
                JSONObject jOfficial = officials.getJSONObject(i);
                String name = jOfficial.getString("name");

                String address = null;

                if (jOfficial.has("address")) {
                    JSONObject jAddress = jOfficial.getJSONArray("address").getJSONObject(0);
                    address = "";
                    if (jAddress.has("line1") && !jAddress.getString("line1").isEmpty()) {
                        address = address + jAddress.getString("line1");
                    }
                    if (jAddress.has("line2") && !jAddress.getString("line2").isEmpty()) {
                        address = address + "\n" + jAddress.getString("line2");
                    }
                    if (jAddress.has("line3") && !jAddress.getString("line3").isEmpty()) {
                        address = address + "\n" + jAddress.getString("line3");
                    }
                    address = address + "\n" + jAddress.getString("city");
                    address = address + "\n" + jAddress.getString("state");
                    address = address + "\n" + jAddress.getString("zip");
                }

                String party;

                if (jOfficial.has("party")&&!jOfficial.getString("party").isEmpty()){
                    String prefix = jOfficial.getString("party").toLowerCase().substring(0,4);
                    party = prefix.equals("repu")? "Republican" : (prefix.equals("demo")? "Democratic" : jOfficial.getString("party"));
                }
                else {
                    party = "Unknown";
                }

                String phone = null;
                if (jOfficial.has("phones")){
                    phone = (String) jOfficial.getJSONArray("phones").get(0);
                }

                String url = null;
                if (jOfficial.has("urls")){
                    url = (String) jOfficial.getJSONArray("urls").get(0);
                }

                String email = null;
                if (jOfficial.has("emails")){
                    email = (String) jOfficial.getJSONArray("emails").get(0);
                }

                String photoUrl=null;
                if (jOfficial.has("photoUrl")){
                    photoUrl = jOfficial.getString("photoUrl");
                }

                String facebook = null; String twitter = null; String youtube = null; String google = null;
                if (jOfficial.has("channels")){
                    JSONArray jChannels = jOfficial.getJSONArray("channels");
                    for (int j=0; j<jChannels.length();j++){
                        String type = jChannels.getJSONObject(j).getString("type");
                        if (type.equals("GooglePlus")){
                            google = jChannels.getJSONObject(j).getString("id");
                        }
                        else if (type.equals("Facebook")){
                            facebook = jChannels.getJSONObject(j).getString("id");
                        }
                        else if (type.equals("Twitter")){
                            twitter = jChannels.getJSONObject(j).getString("id");
                        }
                        else if (type.equals("YouTube")){
                            youtube = jChannels.getJSONObject(j).getString("id");
                        }
                    }
                }

                Official official = new Official();
                official.setName(name);
                official.setAddress(address);
                official.setParty(party);
                official.setPhone(phone);
                official.setUrl(url);
                official.setEmail(email);
                official.setPhotoUrl(photoUrl);
                official.setFacebook(facebook);
                official.setGoogle(google);
                official.setTwitter(twitter);
                official.setYoutube(youtube);
                returnedOfficials.add(official);
            }

            for (int i=0;i<offices.length();i++){
                JSONObject jOffice = offices.getJSONObject(i);
                String title = jOffice.getString("name");
                JSONArray jIndices = jOffice.getJSONArray("officialIndices");
                for (int j=0;j<jIndices.length();j++){
                    int k = (Integer) jIndices.get(j);
                    returnedOfficials.get(k).setTitle(title);
                }
            }

            mainActivity.acceptJsonResults(location, returnedOfficials);
        }catch (Exception e){
            Log.d(TAG, "onPostExecute: "+e);
            return;
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String key = strings[0];
        String userInput = strings[1];
        Uri.Builder builder = Uri.parse(apiBase).buildUpon();
        builder.appendEncodedPath("representatives");
        builder.appendQueryParameter("key", key);
        builder.appendQueryParameter("address", userInput);
        String urlToUse = builder.build().toString();
        
        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }catch(Exception e){
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
        return sb.toString();
    }
}
