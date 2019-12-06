package com.yizheng.knowyourgovernment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String key = "AIzaSyDrdYCcu2z7JCdFWpOFwupkKyZUca4ZYak";

    private static final String TAG = "MainActivity";

    private ArrayList<Official> officials = new ArrayList<>();
    private RecyclerView recyclerView;
    private OfficialAdapter officialAdapter;

    private TextView locationText;

    private static int CODE_FOR_OFFICIAL_ACTIVITY = 100;
    private static int MY_LOCATION_REQUEST_CODE_ID = 329;
    private LocationManager locationManager;
    private Criteria criteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationText = findViewById(R.id.locationText);

        recyclerView = findViewById(R.id.recycler);
        officialAdapter = new OfficialAdapter(officials, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        criteria = new Criteria();

        criteria.setPowerRequirement(Criteria.POWER_LOW);

        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);

        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE_ID);
        } else {
            getCurrentInfo();
        }

        //makeList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_LOCATION_REQUEST_CODE_ID) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[0] == PERMISSION_GRANTED) {
                getCurrentInfo();
                return;
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentInfo() {
        boolean connected = doNetCheck();

        if (connected) {

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            String bestProvider = locationManager.getBestProvider(criteria, true);
            Location currentLocation = locationManager.getLastKnownLocation(bestProvider);
            if (currentLocation != null) {
                double lat = currentLocation.getLatitude();
                double lon = currentLocation.getLongitude();
                try {
                    List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                    String postal = addresses.get(0).getPostalCode();
                    new OfficialsDownloader(this).execute(key, postal);
                } catch (Exception e) {
                    Log.d(TAG, "getCurrentInfo: " + e);
                    return;
                }
            } else {
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("No Network Connection");
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

//    private void makeList() {
//        for (int i = 0; i < 10; i++) {
//            Official o = new Official("title "+i, "first last "+i, "party party "+i);
//            officials.add(o);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_menu_icon:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            case R.id.location_menu_icon:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Enter a City, State or a Zip Code:");
                final EditText editText = new EditText(this);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                editText.setGravity(Gravity.CENTER_HORIZONTAL);
                builder.setView(editText);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = editText.getText().toString();
                        new OfficialsDownloader(MainActivity.this).execute(key, userInput);
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void acceptJsonResults(String location, ArrayList<Official> os){
        officials.clear();
        officials.addAll(os);
        officialAdapter.notifyDataSetChanged();
        locationText.setText(location);
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildLayoutPosition(v);
        Official o = officials.get(position);
        Intent intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("selectedOfficial", o);
        intent.putExtra("currentLocation", locationText.getText().toString());
        startActivityForResult(intent, CODE_FOR_OFFICIAL_ACTIVITY);
    }

    private Boolean doNetCheck(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null){
            return null;
        }
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else{
            return false;
        }
    }
}
