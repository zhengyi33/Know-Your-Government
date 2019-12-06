package com.yizheng.knowyourgovernment;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private Official o;
    private String location;

    private TextView detailLocation, detailOffice,detailNameParty;
    private ImageView iv;
    private ScrollView scroll;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        scroll = findViewById(R.id.detailScroll);

        detailLocation = findViewById(R.id.detailLocation);
        detailOffice = findViewById(R.id.detailOffice);
        detailNameParty = findViewById(R.id.detailName);
        iv = findViewById(R.id.detailPhoto);
        logo = findViewById(R.id.detailLogo);

        Intent intent = getIntent();

        if (intent.hasExtra("location")){
            location = intent.getStringExtra("location");
        }

        if (intent.hasExtra("official")){
            o = (Official) intent.getSerializableExtra("official");
        }

        detailLocation.setText(location);
        detailOffice.setText(o.getTitle());
        String name = o.getName();
        String party = o.getParty();
        if (party.equals("Republican")){
            scroll.setBackgroundColor(Color.RED);
            logo.setImageResource(R.drawable.rep_logo);

        }
        else if (party.equals("Democratic")){
            scroll.setBackgroundColor(Color.BLUE);
            logo.setImageResource(R.drawable.dem_logo);
        }

        detailNameParty.setText(name+"\n"+"("+party+" Party)");

        loadImage(o.getPhotoUrl());



    }
    private void loadImage(final String url) {
        boolean connceted = doNetCheck();
        if (connceted) {
            Picasso picasso = new Picasso.Builder(this).build();
            picasso.setLoggingEnabled(true);
            picasso.load(url).error(R.drawable.brokenimage).placeholder(R.drawable.placeholder).into(iv);
        }
        else{
            iv.setImageResource(R.drawable.brokenimage);
        }
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
