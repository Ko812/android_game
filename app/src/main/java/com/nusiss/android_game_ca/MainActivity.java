package com.nusiss.android_game_ca;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.util.DebugUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.graphics.Color;
import android.opengl.GLES32;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button fetchButton;
    List<ImageButton> fetchedImageButtons = new ArrayList<ImageButton>();
    private ArrayList<Integer> selectedImagesId = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchButton = findViewById(R.id.fetchButton);
        fetchButton.setOnClickListener(this::onClick);
        fetchedImageButtons.add(findViewById(R.id.imageView1Button));
        fetchedImageButtons.add(findViewById(R.id.imageView2Button));
        fetchedImageButtons.add(findViewById(R.id.imageView3Button));
        fetchedImageButtons.get(0).setOnClickListener(this::onClick);
        fetchedImageButtons.get(1).setOnClickListener(this::onClick);
        fetchedImageButtons.get(2).setOnClickListener(this::onClick);
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
            if(validateGameSetup(view)){
                startActivity(gameActivityIntent);
            }


        } else {
            view.setBackgroundColor(Color.parseColor("#ffcceecc"));
            System.out.println("Button pressed" + view.getId());
        }
    }

    private boolean validateGameSetup(View view){
        return true;
    }
}