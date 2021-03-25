package com.example.homework3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MusicService extends Service {

    MusicPlayer musicPlayer;
    private final IBinder iBinder = new MyBinder();

    public static final String COMPLETE_INTENT = "complete intent";
    public static final String MUSICNAME = "music name";
    public static final String PIC_ID = "picture id";
    public static final String BUTTON_TEXT = "button text";

    @Override
    public void onCreate()
    {
        super.onCreate();
        musicPlayer = new MusicPlayer(this);
    }

    @Override
    public void onDestroy()
    {
        musicPlayer.stopPlayer();
    }

    public void startMusic(String songTitle, String s1, String s2, String s3, int p1, int p2, int p3)
    {
        musicPlayer.playMusic(songTitle, s1, s2, s3, p1, p2, p3);
    }

    public void pauseMusic()
    {
        musicPlayer.pauseMusic();
    }

    public void resumeMusic()
    {
        musicPlayer.resumeMusic();
    }

    public void restartMusic()
    {
        musicPlayer.restartMusic();
    }

    public int getPlayingStatus()
    {
        return musicPlayer.getMusicStatus();
    }

    public int getPicToDisplay() {
        return musicPlayer.getPictureToDisplay();
    }

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

//    public void onUpdateMusicName(String musicName) {
//        Intent intent = new Intent(COMPLETE_INTENT);
//        intent.putExtra(MUSICNAME, musicName);
//        sendBroadcast(intent);
//    }

    public void onUpdatePicture(int picID) {
        Intent intent = new Intent(COMPLETE_INTENT);
        intent.putExtra(PIC_ID, picID);
        sendBroadcast(intent);
    }

    public void onUpdatePlayButton(String text) {
        Intent intent = new Intent(COMPLETE_INTENT);
        intent.putExtra(BUTTON_TEXT, text);
        sendBroadcast(intent);
    }

    public class MyBinder extends Binder
    {
        MusicService getService()
        {
            return MusicService.this;
        }
    }
}