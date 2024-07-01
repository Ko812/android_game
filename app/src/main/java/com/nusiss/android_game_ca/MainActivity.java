package com.nusiss.android_game_ca;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.util.DebugUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.nusiss.android_game_ca.adapters.CustomAdapter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.net.URLConnection;

import coil.ImageLoader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button fetchButton;
    private CustomAdapter adapter;
    private ArrayList<String> selectedUrls = new ArrayList<>();

    private final String[] urls = {
            "https://cdn.stocksnap.io/img-thumbs/280h/beach-sunset_QDGNZDRHZK.jpg",
            "https://cdn.stocksnap.io/img-thumbs/280h/garden-plants_HNMAUHKDMN.jpg",
            "https://cdn.stocksnap.io/img-thumbs/280h/yellow-flower_WUORV2UBUI.jpg",
            "https://cdn.stocksnap.io/img-thumbs/280h/senior-couple_IJV0VL7FAG.jpg",
            "https://cdn.stocksnap.io/img-thumbs/280h/city-lights_OY811SXWQW.jpg",
            "https://cdn.stocksnap.io/img-thumbs/280h/senior-people_HFSNYZP8LE.jpg",
            "https://cdn.stocksnap.io/img-thumbs/280h/sea-sky_IXHJ4316GH.jpg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupButtonsAndAdapter();

    }

    private void setupButtonsAndAdapter(){
        ProgressBar bar = (ProgressBar) findViewById(R.id.determinateBar);
        adapter = new CustomAdapter(this, urls, bar);
        fetchButton = findViewById(R.id.fetchButton);
        fetchButton.setOnClickListener(this);
        Button startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(this);
        GridView gridView = findViewById(R.id.downloadedImgs);
        if(gridView != null){
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
            try {
                HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                con.setRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_5_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.1 Mobile/15E148 Safari/604.1");
                int responseCode = con.getResponseCode();
                if(responseCode != HttpURLConnection.HTTP_OK){
                    Toast.makeText(this, "Not able to fetch content of url " + url, Toast.LENGTH_LONG).show();
                } else {
                    StringBuffer sb = new StringBuffer();
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String x;
                    while((x = br.readLine()) != null){
                        sb.append(x);
                    }
                    Log.d("HttpResponse", sb.toString());
                }
            } catch(IOException ioexception){
                ioexception.printStackTrace();
                Toast.makeText(this, "Not able to fetch content of url " + url + ". " + ioexception.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else if(id == R.id.startGameButton){
            if(selectedUrls.size() < 6){
                Toast.makeText(this, "Require 6 images to start game.", Toast.LENGTH_LONG).show();
                return;
            }
            Intent gameActivityIntent = new Intent(this, GameActivity.class);
            gameActivityIntent.putExtra("url_0", selectedUrls.get(0));
            gameActivityIntent.putExtra("url_1", selectedUrls.get(1));
            gameActivityIntent.putExtra("url_2", selectedUrls.get(2));
            gameActivityIntent.putExtra("url_3", selectedUrls.get(3));
            gameActivityIntent.putExtra("url_4", selectedUrls.get(4));
            gameActivityIntent.putExtra("url_5", selectedUrls.get(5));
            startActivity(gameActivityIntent);
        } else {
            view.setBackgroundColor(Color.parseColor("#ffcceecc"));
        }
    }

    public void onItemClick(AdapterView adapterView, View view, int pos, long id){
//        adapter.
//        Drawable imgvDrawable = ((ImageView) view).get();
        if(!selectedUrls.contains(urls[pos % 7])){
            if(selectedUrls.size() < 6){
                selectedUrls.add(urls[pos % 7]);
                Toast.makeText(this, "Added 1 image for the game.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "You have already selected 6 images.", Toast.LENGTH_LONG).show();
            }
        } else {
            selectedUrls.remove(urls[pos % 7]);
        }
    }


}