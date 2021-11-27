package com.kuwathsala.music;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class MusicFiles {

    private LinkedList<File> allMusicFilesObject = new LinkedList<>();

    private MusicFiles(){}

    private static MusicFiles instance;

    public static MusicFiles getInstance(){
        if(instance==null){
            instance = new MusicFiles();
        }
        return instance;
    }

    public void setAllMusicFilesObject(LinkedList<File> allMusicFilesObject){
        this.allMusicFilesObject = allMusicFilesObject;
    }

    public LinkedList<File> getAllMusicFilesObject() {
        return this.allMusicFilesObject;
    }
}
