package com.nusiss.android_game_ca.adapters;

import static java.lang.Math.min;

import android.app.Activity;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.nusiss.android_game_ca.R;

import java.util.ArrayList;
import java.util.List;

import coil.ImageLoader;
import coil.request.ImageRequest;
import kotlinx.coroutines.Job;


public class GridAdapter extends BaseAdapter {

    private Context context;
    private ImageLoader loader;
    private List<String> urls;
    private List<Job> loadingJobs;

    private List<Integer> viewIds = new ArrayList<>();

    public GridAdapter(Context context, List<String> urls){
        super();
        this.context = context;
        this.urls = urls;
        this.loader = new ImageLoader.Builder(context).build();
        this.loadingJobs = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return min(urls.size(),20) ;
    }

    @Override
    public Object getItem(int i) {
        return urls.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @androidx.annotation.NonNull
    public View getView(int pos, View view, @NonNull ViewGroup parent){
        if(view == null && context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.image, parent, false);
        }
        ImageView imgView = (ImageView) view;
        Log.d("ADAPTER", "Adapter getView called. Loading view at pos " + pos);
        ImageRequest imageRequest = new ImageRequest
                .Builder(context)
                .data(urls.get(pos))
                .target(imgView)
                .crossfade(true)
                .build();
        loader.enqueue(imageRequest);

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

    public List<Integer> getViewIds(){
        return viewIds;
    }


    public void setUrls(List<String> urls){
        this.urls = urls;
    }
}
