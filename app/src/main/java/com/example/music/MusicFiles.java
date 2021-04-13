package com.example.music;

import java.io.File;
import java.util.ArrayList;

public class MusicFiles {

    private ArrayList<File> allMusicFilesObject = new ArrayList<>();

    private MusicFiles(){}

    private static MusicFiles instance;

    public static MusicFiles getInstance(){
        if(instance==null){
            instance = new MusicFiles();
        }
        return instance;
    }

    public void setAllMusicFilesObject(ArrayList<File> allMusicFilesObject){
        this.allMusicFilesObject = allMusicFilesObject;
    }

    public ArrayList<File> getAllMusicFilesObject() {
        return this.allMusicFilesObject;
    }
}
