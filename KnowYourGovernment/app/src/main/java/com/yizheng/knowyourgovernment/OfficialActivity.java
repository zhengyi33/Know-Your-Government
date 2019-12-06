package com.yizheng.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {

    private TextView officialLocationText, officialNameText, officialOfficeText, officialPartyText, address, officialAddressText, phone, officialPhoneText, email, emailText, website, websiteText;

    private ScrollView scroll;

    private ImageView imageView, logoImage, facebookImage, twitterImage, youtubeImage, plusImage;

    Official o;
    String party;
    String location;

    private final static String gop = "https://www.gop.com";
    private final static String dem = "https://democrats.org";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        scroll = findViewById(R.id.scroll);

        officialLocationText = findViewById(R.id.officialLocationText);
        officialOfficeText = findViewById(R.id.officialOfficeText);
        officialNameText = findViewById(R.id.officialNameText);
        officialPartyText = findViewById(R.id.officialPartyText);
        address = findViewById(R.id.address);
        officialAddressText = findViewById(R.id.officialAddressText);
        phone = findViewById(R.id.phone);
        officialPhoneText = findViewById(R.id.officialPhoneText);
        email = findViewById(R.id.email);
        emailText = findViewById(R.id.emailText);
        website = findViewById(R.id.website);
        websiteText = findViewById(R.id.websiteText);

        imageView = findViewById(R.id.profileImage);
        logoImage = findViewById(R.id.logoImage);
        facebookImage = findViewById(R.id.facebookView);
        twitterImage = findViewById(R.id.twitterView);
        youtubeImage = findViewById(R.id.youtubeView);
        plusImage = findViewById(R.id.plusView);


        Intent intent = getIntent();
        if (intent.hasExtra("currentLocation")) {
            location = intent.getStringExtra("currentLocation");
            officialLocationText.setText(location);
        }
        if (intent.hasExtra("selectedOfficial")) {
            o = (Official) intent.getSerializableExtra("selectedOfficial");
            officialOfficeText.setText(o.getTitle());
            officialNameText.setText(o.getName());
            party = o.getParty();
            if (party.equals("Republican")) {
                party = "(" + party + " Party)";
                scroll.setBackgroundColor(Color.RED);
                logoImage.setImageResource(R.drawable.rep_logo);
            } else if (party.equals("Democratic")) {
                party = "(" + party + " Party)";
                scroll.setBackgroundColor(Color.BLUE);
                logoImage.setImageResource(R.drawable.dem_logo);
            } else {
                party = "(" + party + ")";
                scroll.setBackgroundColor(Color.BLACK);
            }
            officialPartyText.setText(party);

            if (o.getPhotoUrl() != null) {
                loadImage(o.getPhotoUrl());
            }

            if (o.getAddress() != null) {
                officialAddressText.setText(o.getAddress());
            } else {
                this.address.setVisibility((View.GONE));
                officialAddressText.setVisibility(View.GONE);
            }

            String phone = o.getPhone();
            if (phone != null && !phone.isEmpty()) {
                officialPhoneText.setText(phone);
            } else {
                this.phone.setVisibility((View.GONE));
                officialPhoneText.setVisibility(View.GONE);
            }

            String email = o.getEmail();
            if (email != null && !email.isEmpty()) {
                emailText.setText(email);
            } else {
                this.email.setVisibility((View.GONE));
                emailText.setVisibility(View.GONE);
            }

            String website = o.getUrl();
            if (website != null && !website.isEmpty()) {
                websiteText.setText(website);
            } else {
                this.website.setVisibility((View.GONE));
                websiteText.setVisibility(View.GONE);
            }

            if (o.getFacebook() == null) {
                facebookImage.setVisibility(View.INVISIBLE);
            }
            if (o.getTwitter() == null) {
                twitterImage.setVisibility(View.INVISIBLE);
            }
            if (o.getYoutube() == null) {
                youtubeImage.setVisibility(View.INVISIBLE);
            }
            if (o.getGoogle() == null) {
                plusImage.setVisibility(View.INVISIBLE);
            }

            Linkify.addLinks(officialAddressText, Linkify.ALL);
            officialAddressText.setLinkTextColor(Color.WHITE);
            Linkify.addLinks(emailText, Linkify.ALL);
            emailText.setLinkTextColor(Color.WHITE);
            Linkify.addLinks(officialPhoneText, Linkify.ALL);
            officialPhoneText.setLinkTextColor(Color.WHITE);
            Linkify.addLinks(websiteText, Linkify.ALL);
            websiteText.setLinkTextColor(Color.WHITE);
        }
    }

    public void photoClicked(View v){
        if (o.getPhotoUrl()!=null){
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("location", location);
            intent.putExtra("official", o);
            startActivity(intent);
        }
    }

    public void logoClicked(View v) {
        if (o.getParty().equals("Republican")) {
            Uri uri = Uri.parse(gop);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        } else if (o.getParty().equals("Democratic")) {
            Uri uri = Uri.parse(dem);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + o.getFacebook();
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app

                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                urlToUse = "fb://page/" + o.getFacebook();
            }
        } catch (
                PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }

        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));

        startActivity(facebookIntent);

    }


    public void twitterClicked(View v) {
        Intent intent = null;
        String name = o.getTwitter();
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
// no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void youtubeClicked(View v) {
        String name = o.getYoutube();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void plusClicked(View v) {
        String name = o.getGoogle();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://plus.google.com/" + name)));
        }
    }

    private void loadImage(final String url) {
        boolean connected = doNetCheck();
        if (connected) {
            Picasso picasso = new Picasso.Builder(this).build();
            picasso.setLoggingEnabled(true);
            picasso.load(url).error(R.drawable.brokenimage).placeholder(R.drawable.placeholder).into(imageView);
        }
        else{
            imageView.setImageResource(R.drawable.brokenimage);
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
