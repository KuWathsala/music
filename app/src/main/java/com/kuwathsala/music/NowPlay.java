package com.kuwathsala.music;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kuwathsala.music.models.SingleMusic;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class NowPlay extends Fragment {

    private NowPlay(){};

    private static NowPlay instance;

    public static NowPlay getInstance(){
        if(instance==null){
            synchronized (NowPlay.class) {
                if (instance==null){
                    instance = new NowPlay();
                }
            }
        }
        return instance;
    }

    private SeekBar mMusicSeekBar;
    private TextView mMusicTitle;
    private ImageView btnPlay;
    private ImageView btnNext;
    private ImageView btnPre;
    private TextView timeDuration;
    private TextView timeNow;
    private ImageView mAlbumArt;
    int pos;
    static MediaPlayer mMediaPlayer;
    MusicFiles musicFiles;
    ArrayList<File> files;
    SingleMusic singleMusic = SingleMusic.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_play, container, false);

        musicFiles = MusicFiles.getInstance();
        files = musicFiles.getAllMusicFilesObject();
        pos = (singleMusic==null) ? 0 : singleMusic.getPosition();
        singleMusic.setPosition(pos);

        mMusicSeekBar = view.findViewById(R.id.music_seek_bar);
        mMusicTitle = view.findViewById(R.id.music_title);
        btnPlay = view.findViewById(R.id.btn_play);
        btnNext = view.findViewById(R.id.btn_next);
        btnPre = view.findViewById(R.id.btn_pre);
        timeDuration = view.findViewById(R.id.thime_duration);
        timeNow = view.findViewById(R.id.time_now);
        mAlbumArt = view.findViewById(R.id.album_art);

        initMusicPlayer(singleMusic.getPosition());

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (files == null || files.isEmpty()) return;
                pos = (pos<files.size()-1) ? pos+1 : 0;
                singleMusic.setPosition(pos);
                initMusicPlayer(singleMusic.getPosition());
            }
        });

        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (files == null || files.isEmpty()) return;
                pos = (pos>0) ? pos-1 : files.size()-1;
                singleMusic.setPosition(pos);
                initMusicPlayer(singleMusic.getPosition());
            }
        });

        return view;
    }

    public void initMusicPlayer(final int position){
        if(singleMusic.isStart()) {

            singleMusic.setPosition(position);

            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.reset();
            }
            String name = files.get(position).getName();
            mMusicTitle.setText(name);

            Uri musicPath = Uri.parse(files.get(position).toString());
            mMediaPlayer = MediaPlayer.create(getContext(), musicPath);

            //set album art
            try {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(musicPath.getPath());
                byte[] data = mmr.getEmbeddedPicture();
                String albumTitle=mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);// Get the title of the music album
                String artist=mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);// Get music artist information
                String title=mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);// Get music title information
                //Log.d("", "MediaMetadataRetriever data "+title);
                if(data != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    mAlbumArt.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //btnPlay.setImageResource((mMediaPlayer.isPlaying())? R.drawable.ic_pause : R.drawable.ic_play);

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mMusicSeekBar.setMax(mMediaPlayer.getDuration());
                    singleMusic.setTimeDuration(mMediaPlayer.getDuration());
                    timeDuration.setText(createTimerLabel(singleMusic.getTimeDuration()));
                    mMediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.ic_pause);
                }
            });

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    int p = position;
                    p = (p < files.size() - 1) ? p + 1 : 0;
                    singleMusic.setPosition(p);
                    initMusicPlayer(p);
                }
            });

            mMusicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b) {
                        mMediaPlayer.seekTo(i);
                        mMusicSeekBar.setProgress(i);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mMediaPlayer != null) {
                        if (mMediaPlayer.isPlaying()) {
                            try {
                                Message message = new Message();
                                message.what = mMediaPlayer.getCurrentPosition();
                                handler.sendMessage(message);
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            singleMusic.setTimeNow(createTimerLabel( msg.what));
            timeNow.setText(singleMusic.getTimeNow());
            mMusicSeekBar.setProgress(msg.what);
        }
    };

    private String createTimerLabel (int duration) {
        String timerLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        timerLabel += min+":";
        if(sec<10)
            timerLabel+="0";
        timerLabel += sec;
        return timerLabel;
    }

    private void play(){
        if(mMediaPlayer!=null && mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            btnPlay.setImageResource(R.drawable.ic_play);
        } else if (mMediaPlayer != null){ //if(singleMusic.isStart()){
            mMediaPlayer.start();
            btnPlay.setImageResource(R.drawable.ic_pause);
        } else {
            try {
                NowPlay play = NowPlay.getInstance();
                SingleMusic music = SingleMusic.getInstance();
                music.setStart(true);
                play.pos = 0;
                play.initMusicPlayer(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}


