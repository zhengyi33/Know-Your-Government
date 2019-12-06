package com.yizheng.knowyourgovernment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    private static final String googleURL = "https://developers.google.com/civic-information/";
    private TextView googleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        googleText = findViewById(R.id.googleText);
        googleText.setPaintFlags(googleText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public void googleClicked(View v){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(googleURL));
        startActivity(i);
    }
}
