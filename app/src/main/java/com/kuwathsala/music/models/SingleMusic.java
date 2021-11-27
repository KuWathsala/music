package com.kuwathsala.music.models;

public class SingleMusic {
    private static SingleMusic instace;
    private int position = 0 ;
    private int timeDuration;
    private String timeNow;
    private boolean isStart = false;

    private SingleMusic(){}

    public static SingleMusic getInstance(){
        if(instace==null)
            instace = new SingleMusic();
        return instace;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setTimeDuration(int timeDuration) {
        this.timeDuration = timeDuration;
    }

    public void setTimeNow(String timeNow) {
        this.timeNow = timeNow;
    }

    public int getPosition() {
        return position;
    }

    public int getTimeDuration() {
        return timeDuration;
    }

    public String getTimeNow() {
        return timeNow;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isStart() {
        return isStart;
    }
}


