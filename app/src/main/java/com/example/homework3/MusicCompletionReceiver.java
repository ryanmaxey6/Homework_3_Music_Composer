package com.example.homework3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MusicCompletionReceiver extends BroadcastReceiver {

    PlayerActivity playerActivity;

    public MusicCompletionReceiver() {
        // empty constructor
    }

    public MusicCompletionReceiver(PlayerActivity playerActivity) {
        this.playerActivity = playerActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        String musicName = intent.getStringExtra(MusicService.MUSICNAME);
//        playerActivity.updateName(musicName);
        int picID = intent.getIntExtra(MusicService.PIC_ID, 0);
        if (picID != 0)
        {
            playerActivity.updatePicture(picID);
        }
        String buttonText = intent.getStringExtra(MusicService.BUTTON_TEXT);
        if (buttonText != null)
        {
            playerActivity.updatePlayButton(buttonText);
        }
    }
}
