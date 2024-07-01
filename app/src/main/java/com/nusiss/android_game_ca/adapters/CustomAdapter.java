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
import android.widget.ProgressBar;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import coil.ComponentRegistry;
import coil.ImageLoader;
import coil.request.Disposable;
import coil.request.ImageRequest;
import kotlin.Unit;
import kotlinx.coroutines.Job;


public class CustomAdapter extends BaseAdapter {

    private Context context;

    private ProgressBar bar;

    private ImageLoader loader;
    private String[] urls;
    private List<Job> loadingJobs;

    public CustomAdapter(Context context, String[] urls, ProgressBar bar){
        super();
        this.context = context;
        this.urls = urls;
        this.loader = new ImageLoader.Builder(context).build();
        this.loadingJobs = new ArrayList<>();
        this.bar = bar;
    }

    @Override
    public int getCount() {
        return 20;
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
            ImageRequest request = new ImageRequest
                    .Builder(context)
                    .data(urls[pos % 7])
                    .target(imgView)
                    .build();
            Job loadingJob = loader.enqueue(request).getJob();
            loadingJobs.add(loadingJob);
//            loadingJob.invokeOnCompletion(e -> {
//                Log.d("LoadingComplete", "Loaded 1 job");
//                if(loadingComplete()){
//                    bar.setVisibility(View.INVISIBLE);
//                    bar.invalidate();
//                } else {
//                    bar.incrementProgressBy(5);
//                }
//                return Unit.INSTANCE;
//            });
//            loadingJobs.add(loadingJob);

        } catch(Exception e){
            Log.d("Resource", "Exception thrown when loading drawable resource " + e.getMessage());
        }
        return imgView;
    }

    private boolean loadingComplete(){
        Log.d("CheckingLoadProgess", "Load progress: "+ loadingJobs.stream().map(j -> j.isCompleted()).filter(c -> c).count() + " jobs completed, out of " + loadingJobs.stream().count());
        return loadingJobs
                .stream()
                .map(j -> j.isCompleted())
                .reduce((c1, c2) -> c1 && c2)
                .get();
    }

    public int loadingProgress(){
        if(loadingJobs.isEmpty()){
            return 100;
        }
        return (int)(loadingJobs.stream().map(j -> j.isCompleted()).count() / loadingJobs.size() * 100);
    }


}
