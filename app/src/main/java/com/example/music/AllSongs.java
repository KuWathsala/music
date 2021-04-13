package com.example.music;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllSongs extends Fragment {

    private AllSongs(){};

    private static AllSongs instance;

    public static AllSongs getInstance(){
        if(instance==null)
            instance = new AllSongs();
        return instance;
    }

    private RecyclerView allSongsListView;
    private ProgressBar allSongsProgressBar;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<File> fileObjects = new ArrayList();
    private ArrayList<String> songs = new ArrayList();
    private ArrayList<Song> songsDetails = new ArrayList<>();
    MusicFiles musicFiles = MusicFiles.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_songs, container, false);

        allSongsListView = view.findViewById(R.id.all_songs_list);
        allSongsProgressBar = view.findViewById(R.id.all_songs_progress_bar);

        loadMusicFiles();

        adapter = new MusicAdapter(songs, getContext());
        layoutManager = new LinearLayoutManager(view.getContext());
        allSongsListView.setAdapter(adapter);
        allSongsListView.setLayoutManager(layoutManager);

        return view;
    }

    private void loadMusicFiles(){
        allSongsProgressBar.setVisibility(View.VISIBLE);
        fileObjects = getMusicFiles(Environment.getExternalStorageDirectory());
        if (fileObjects==null) return;
        musicFiles.setAllMusicFilesObject(fileObjects);
        for (int i=0; i<fileObjects.size(); i++) {
            songs.add(fileObjects.get(i).getName());
        }
        songs.trimToSize();
        allSongsProgressBar.setVisibility(View.GONE);
    }

    private ArrayList<File> getMusicFiles(File file){
        try {
            ArrayList<File> allMusicFilesObject = new ArrayList<>();
            File files[] = file.listFiles();
            for (File f : files){
                if(f.isDirectory() && !f.isHidden()){
                    allMusicFilesObject.addAll(getMusicFiles(f));
                } else {
                    if(f.getName().endsWith(".mp3")||f.getName().endsWith(".mp4a")||f.getName().endsWith(".mp3")) {
                        allMusicFilesObject.add(f);
                    }
                }
            }
            allMusicFilesObject.trimToSize();
            return allMusicFilesObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}