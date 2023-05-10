package com.example.miniprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.net.Uri;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class Flashscore extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashscore);
    }
    public void video(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://jokerlivestream.co/basketball"));
        startActivity(intent);
    }
}