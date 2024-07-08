package com.nusiss.android_game_ca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.nusiss.android_game_ca.adapters.GridAdapter;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import coil.ImageLoader;
import coil.request.ImageRequest;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button fetchButton;
    private GridAdapter adapter;
    private ArrayList<String> selectedUrls = new ArrayList<>();

    private List<String> urls = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupButtonsAndAdapter();
//        loadImg();
    }

    private void setupButtonsAndAdapter(){
        ProgressBar bar = (ProgressBar) findViewById(R.id.determinateBar);
        adapter = new GridAdapter(this, urls);
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
            tryFetch(url);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        if(!selectedUrls.contains(urls.get(pos))){
            if(selectedUrls.size() < 6){
                selectedUrls.add(urls.get(pos));
                Toast.makeText(this, "Added 1 image for the game.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "You have already selected 6 images.", Toast.LENGTH_LONG).show();
            }
        } else {
            selectedUrls.remove(urls.get(pos));
        }
    }

    private void tryFetch(String url){
        urls = new ArrayList<String>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://stocksnap.io").get();
                    Elements links = doc.select("img[src]");
                    for (Element link : links) {
                        if(link.attr("src").startsWith("https://cdn.") && link.attr("src").endsWith("jpg")){
                            urls.add(link.attr("src"));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                } catch(Exception e){
                    Log.d("FETCH", "Exception on fetching " + url);
                }

                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        adapter.setUrls(urls);
                        adapter.notifyDataSetChanged();
                        Log.d("ADAPTER", "Adapter changed");
                    }
                });

            }
        }).start();
    }

}