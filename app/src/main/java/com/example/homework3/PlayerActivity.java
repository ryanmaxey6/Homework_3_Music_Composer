package com.example.homework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    TextView songName;
    Button play;
    Button restart;
    ImageView image;
    private int imageID = R.drawable.song_pic;

    MusicService musicService;
    MusicCompletionReceiver musicCompletionReceiver;
    Intent startMusicServiceIntent;
    boolean isBound = false;
    boolean isInitialized = false;

    private String title;
    private String sound1;
    private String sound2;
    private String sound3;
    private int start1;
    private int start2;
    private int start3;

    public static final String INITIALIZE_STATUS = "initialization status";
    public static final String MUSIC_PLAYING = "music playing";
    public static final String IMAGE_ID = "image id";
    private static final String LOG_NAME = PlayerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        songName = (TextView) findViewById(R.id.tvSongName);
        play = (Button) findViewById(R.id.playButton);
        play.setOnClickListener(this);
        restart = (Button) findViewById(R.id.restartButton);
        restart.setOnClickListener(this);
        image = (ImageView) findViewById(R.id.ivHokie);

        if (savedInstanceState != null)
        {
            isInitialized = savedInstanceState.getBoolean(INITIALIZE_STATUS);
            songName.setText(savedInstanceState.getString(MUSIC_PLAYING));
            imageID = savedInstanceState.getInt(IMAGE_ID);
            image.setImageResource(imageID);
            Log.i(LOG_NAME, "saved instance state restored");
        }

        startMusicServiceIntent = new Intent(this, MusicService.class);

        if (!isInitialized)
        {
            startService(startMusicServiceIntent);
            isInitialized = true;
            Log.i(LOG_NAME, "service started");
        }

        musicCompletionReceiver = new MusicCompletionReceiver(this);

        Intent i = getIntent();
        title = i.getStringExtra("music title");
        songName.setText(title);
        sound1 = i.getStringExtra("sound 1");
        sound2 = i.getStringExtra("sound 2");
        sound3 = i.getStringExtra("sound 3");
        start1 = i.getIntExtra("start 1", 0);
        start2 = i.getIntExtra("start 2", 0);
        start3 = i.getIntExtra("start 3", 0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.i(LOG_NAME, "onCreate completed");
    }

    @Override
    public void onClick(View v) {
        Log.i(LOG_NAME, "onClick called");
        if (isBound)
        {
            Log.i(LOG_NAME, "is bounded");
            if (v.getId() == R.id.playButton)
            {
                switch (musicService.getPlayingStatus())
                {
                    case 0:
                        musicService.startMusic(title, sound1, sound2, sound3, start1, start2, start3);
                        play.setText("Pause");
                        break;
                    case 1:
                        musicService.pauseMusic();
                        play.setText("Play");
                        break;
                    case 2:
                        musicService.resumeMusic();
                        play.setText("Pause");
                        break;
                }
            }
            else if (v.getId() == R.id.restartButton)
            {
                musicService.restartMusic();
                play.setText("Pause");
            }
        }
    }

//    public void updateName(String musicName) {
//        songName.setText(musicName);
//    }

    public void updatePicture(int pictureID) {
        image.setImageResource(pictureID);
        imageID = pictureID;
        Log.i(LOG_NAME, "updatePicture completed");
    }

    public void updatePlayButton(String text) {
        play.setText(text);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (isInitialized && !isBound) {
            bindService(startMusicServiceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
            Log.i(LOG_NAME, "bindService completed");
        }

        registerReceiver(musicCompletionReceiver, new IntentFilter(MusicService.COMPLETE_INTENT));
        Log.i(LOG_NAME, "onResume completed");
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        if (isBound) {
            unbindService(musicServiceConnection);
            isBound = false;
            Log.i(LOG_NAME, "unbindService completed");
        }

        unregisterReceiver(musicCompletionReceiver);
        Log.i(LOG_NAME, "onPause completed");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(INITIALIZE_STATUS, isInitialized);
        outState.putString(MUSIC_PLAYING, songName.getText().toString());
        outState.putInt(IMAGE_ID, imageID);
        super.onSaveInstanceState(outState);
        Log.i(LOG_NAME, "onSaveInstanceState completed");
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }

    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
            musicService = binder.getService();
            isBound = true;

            updatePicture(musicService.getPicToDisplay());

            switch (musicService.getPlayingStatus()) {
                case 0:
                    play.setText("Play");
                    break;
                case 1:
                    play.setText("Pause");
                    break;
                case 2:
                    play.setText("Play");
                    break;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            isBound = false;
        }
    };
}