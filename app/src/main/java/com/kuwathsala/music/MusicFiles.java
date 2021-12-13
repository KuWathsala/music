package com.kuwathsala.music;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class MusicFiles {

    private ArrayList<File> allMusicFilesObject = new ArrayList<>();

    private MusicFiles(){}

    private static MusicFiles instance;

    public static MusicFiles getInstance(){
        if(instance==null){
            synchronized (MusicFiles.class) {
                if (instance==null){
                    instance = new MusicFiles();
                }
            }
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
