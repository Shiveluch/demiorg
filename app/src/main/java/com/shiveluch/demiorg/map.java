package com.shiveluch.demiorg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class map extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle arguments = getIntent().getExtras();
        String maplink = arguments.get("maplink").toString();    // Hello World
        PhotoView photoView = findViewById(R.id.photo_view);
        Glide.with(this)
                .load(maplink)
                .into(photoView);


    }
}