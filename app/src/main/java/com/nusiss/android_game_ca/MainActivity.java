package com.nusiss.android_game_ca;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.util.DebugUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.opengl.GLES32;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.nusiss.android_game_ca.adapters.CustomAdapter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button fetchButton;
    private CustomAdapter adapter;
    private ArrayList<Integer> selectedImagesId = new ArrayList<Integer>();

    private final String[] urls = {
            "https://cdn.stocksnap.io/img-thumbs/280h/beach-sunset_QDGNZDRHZK.jpg",
            "https://cdn.stocksnap.io/img-thumbs/280h/garden-plants_HNMAUHKDMN.jpg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupButtonsAndAdapter();

    }

    private void setupButtonsAndAdapter(){
        adapter = new CustomAdapter(this);
        fetchButton = findViewById(R.id.fetchButton);
        fetchButton.setOnClickListener(this);
        Button startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(this);
        GridView gridView = findViewById(R.id.downloadedImgs);
        if(gridView != null){
            Log.d("CREATION","gridview setting up");
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(this::onItemClick);
        }
    }
    @Override
    public void onClick(View view){
        int id = view.getId();
        if (id == R.id.fetchButton) {
            TextInputEditText searchInput = findViewById(R.id.searchurl);
            String url = searchInput.getText().toString();
            Toast.makeText(this, url, Toast.LENGTH_LONG).show();
        } else if(id == R.id.startGameButton){
            Intent gameActivityIntent = new Intent(this, GameActivity.class);
            gameActivityIntent.putExtra("selected_images", selectedImagesId);
            if(validateGameSetup(view)){
                startActivity(gameActivityIntent);
            }
        } else {
            view.setBackgroundColor(Color.parseColor("#ffcceecc"));
        }
    }

    public void onItemClick(AdapterView adapterView, View view, int pos, long id){
        Toast.makeText(this, "Already selected " + id + ": " + selectedImagesId.contains(Integer.valueOf((int)id)), Toast.LENGTH_SHORT).show();
    }

    private boolean validateGameSetup(View view){
        return true;
    }


}