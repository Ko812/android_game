package com.nusiss.android_game_ca.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.ImageDecoder;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.nusiss.android_game_ca.MainActivity;
import com.nusiss.android_game_ca.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import coil.ImageLoader;
import coil.request.ImageRequest;


public class CustomAdapter extends BaseAdapter {

    private Context context;

    private final String[] urls = {
            "https://cdn.stocksnap.io/img-thumbs/280h/beach-sunset_QDGNZDRHZK.jpg",
            "https://cdn.stocksnap.io/img-thumbs/280h/garden-plants_HNMAUHKDMN.jpg"
    };


    private final String[] images = {
            "num1", "num2", "num3", "num4", "num5", "num6", "num7", "num8", "num9", "num10",
            "num11", "num12", "num13", "num14", "num15", "num16", "num17", "num18", "num19", "num20"
    };


    public CustomAdapter(Context context){
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @androidx.annotation.NonNull
    public View getView(int pos, View view, @NonNull ViewGroup parent){
        if(view == null && context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.image, parent, false);
        }
        ImageView imgView = (ImageView) view;

        try{
            ImageLoader loader = new ImageLoader.Builder(context).build();
            ImageRequest request = new ImageRequest
                    .Builder(context)
                    .data(urls[0])
                    .target(imgView)
                    .build();
            loader.enqueue(request);
        } catch(Exception e){
            Log.d("Resource", "Exception thrown when loading drawable resource " + e.getMessage());
        }
        return imgView;
    }
}
