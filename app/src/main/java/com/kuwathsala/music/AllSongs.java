package com.kuwathsala.music;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kuwathsala.music.models.Song;

public class AllSongs extends Fragment {

    private AllSongs(){}

    private static AllSongs instance;

    public static AllSongs getInstance(){
        if(instance==null){
            synchronized (AllSongs.class) {
                if (instance==null){
                    instance = new AllSongs();
                }
            }
        }
        return instance;
    }

    private RecyclerView allSongsListView;
    private ProgressBar allSongsProgressBar;
    private TextView noMusicFilesText;
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
        noMusicFilesText = view.findViewById(R.id.noMusicFilesText);

        allSongsProgressBar.setVisibility(View.VISIBLE);
        loadMusicFiles();

        if (songs == null || songs.isEmpty()){
            noMusicFilesText.setVisibility(View.VISIBLE);
        } else {
            noMusicFilesText.setVisibility(View.GONE);
            adapter = new MusicAdapter(songs, getContext());
            layoutManager = new LinearLayoutManager(view.getContext());
            allSongsListView.setAdapter(adapter);
            allSongsListView.setLayoutManager(layoutManager);
        }

        return view;
    }

    private void loadMusicFiles(){
        fileObjects = getMusicFiles(Environment.getExternalStorageDirectory());
        if (fileObjects==null) {
            allSongsProgressBar.setVisibility(View.INVISIBLE);
            return;
        }
        musicFiles.setAllMusicFilesObject(fileObjects);
        for (int i=0; i<fileObjects.size(); i++) {
            songs.add(fileObjects.get(i).getName());
        }
        songs.trimToSize();
        allSongsProgressBar.setVisibility(View.INVISIBLE);
    }

    private ArrayList<File> getMusicFiles(File file){
        try {
            ArrayList<File> allMusicFilesObject = new ArrayList<>();
            File files[] = file.listFiles();
            for (File f : files){
                if(f.isDirectory() && !f.isHidden()){
                    allMusicFilesObject.addAll(getMusicFiles(f));
                } else {
                    if(f.getName().endsWith(".mp3")||f.getName().endsWith(".mp4a")) {
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