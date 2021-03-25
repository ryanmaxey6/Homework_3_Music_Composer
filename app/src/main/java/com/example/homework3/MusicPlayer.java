package com.example.homework3;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayer implements MediaPlayer.OnCompletionListener {

    MediaPlayer player;
    MediaPlayer player2;
    MediaPlayer player3;
    MediaPlayer player4;

    private int currPos = 0;
    private int currPos2 = 0;
    private int currPos3 = 0;
    private int currPos4 = 0;
    private int start1 = 0;
    private int start2 = 0;
    private int start3 = 0;
    private int currTime = 0;

    int musicIndex = 0;
    private int musicStatus = 0;
    private int sound1Status = 0;
    private int sound2Status = 0;
    private int sound3Status = 0;
    private MusicService musicService;

    private Timer timer;

    private int mainPicID = 0;
    private int picID1 = 0;
    private int picID2 = 0;
    private int picID3 = 0;

    private String title;
    private String sound1;
    private String sound2;
    private String sound3;
    private int prog1;
    private int prog2;
    private int prog3;

    static final int[] MUSICPATH = new int[] {
            R.raw.gotechgo,
            R.raw.entersandman,
            R.raw.gamedayrock
    };

    static final int[] SOUNDPATH = new int[] {
            R.raw.clapping,
            R.raw.cheering,
            R.raw.lestgohokies
    };

    static final String[] MUSICNAME = new String[] {
            "Go Tech Go!",
            "Enter Sandman",
            "Gameday Rock"
    };

    static final String[] SOUNDNAME = new String[] {
            "Clapping",
            "Cheering",
            "Go Hokies!"
    };

    public MusicPlayer(MusicService service)
    {
        this.musicService = service;
    }

    public int getMusicStatus()
    {
        return musicStatus;
    }

    public String getMusicName()
    {
        return MUSICNAME[musicIndex];
    }

    private int getSoundPosition(String sound)
    {
        for (int i = 0; i < SOUNDNAME.length; i++)
        {
            if (SOUNDNAME[i].equals(sound))
            {
                return i;
            }
        }
        return -1;
    }

    private int getSoundPictureID(String soundName)
    {
        if (soundName.equals(SOUNDNAME[0]))
        {
            return R.drawable.clapping_pic;
        }
        else if (soundName.equals(SOUNDNAME[1]))
        {
            return R.drawable.cheering_pic;
        }
        else if (soundName.equals(SOUNDNAME[2]))
        {
            return R.drawable.lets_go_hokies;
        }
        else
        {
            return R.drawable.song_pic;
        }
    }

    public int getPictureToDisplay()
    {
        if (sound1Status != 0)
        {
            return getSoundPictureID(SOUNDNAME[0]);
        }
        else if (sound2Status != 0)
        {
            return getSoundPictureID(SOUNDNAME[1]);
        }
        else if (sound3Status != 0)
        {
            return getSoundPictureID(SOUNDNAME[2]);
        }
        else
        {
            return getSoundPictureID(getMusicName());
        }
    }

    public void playMusic(String title, String sound1, String sound2, String sound3, int progress1,
                          int progress2, int progress3)
    {
        for (int i = 0; i < MUSICNAME.length; i++)
        {
            if (MUSICNAME[i].equals(title))
            {
                musicIndex = i;
            }
        }

        this.title = title;
        this.sound1 = sound1;
        this.sound2 = sound2;
        this.sound3 = sound3;
        this.prog1 = progress1;
        this.prog2 = progress2;
        this.prog3 = progress3;

        mainPicID = getSoundPictureID(title);
        picID1 = getSoundPictureID(sound1);
        picID2 = getSoundPictureID(sound2);
        picID3 = getSoundPictureID(sound3);

        player = MediaPlayer.create(this.musicService, MUSICPATH[musicIndex]);
        player2 = MediaPlayer.create(this.musicService, SOUNDPATH[getSoundPosition(sound1)]);
        player3 = MediaPlayer.create(this.musicService, SOUNDPATH[getSoundPosition(sound2)]);
        player4 = MediaPlayer.create(this.musicService, SOUNDPATH[getSoundPosition(sound3)]);

        Log.i("MusicPlayer", "progress1: " + progress1);
        Log.i("MusicPlayer", "progress2: " + progress2);
        Log.i("MusicPlayer", "progress3: " + progress3);
        Log.i("MusicPlayer", "duration: " + player.getDuration());

        start1 = (progress1 * player.getDuration()) / 100;
        start2 = (progress2 * player.getDuration()) / 100;
        start3 = (progress3 * player.getDuration()) / 100;

        Log.i("MusicPlayer", "start1: " + start1);
        Log.i("MusicPlayer", "start2: " + start2);
        Log.i("MusicPlayer", "start3: " + start3);

        player.start();
        player.setOnCompletionListener(this);

//        musicService.onUpdateMusicName(getMusicName());
        musicStatus = 1;

        timer = new Timer();
        TimerTask task = new Helper();
        timer.scheduleAtFixedRate(task, 0, 1);
    }

    private class Helper extends TimerTask
    {
        public void run() {
            if (musicStatus != 0)
            {
                if (currTime == start1)
                {
                    Log.i("MusicPlayer", "starting player2");
                    player2.start();
                    player2.setOnCompletionListener(MusicPlayer.this);
                    musicService.onUpdatePicture(picID1);
                    sound1Status = 1;
                }
                if (currTime == start2)
                {
                    Log.i("MusicPlayer", "start2 inside if: " + start2);
                    Log.i("MusicPlayer", "starting player3");
                    player3.start();
                    player3.setOnCompletionListener(MusicPlayer.this);
                    musicService.onUpdatePicture(picID2);
                    sound2Status = 1;
                }
                if (currTime == start3)
                {
                    Log.i("MusicPlayer", "starting player4");
                    player4.start();
                    player4.setOnCompletionListener(MusicPlayer.this);
                    musicService.onUpdatePicture(picID3);
                    sound3Status = 1;
                }
                if (musicStatus == 1)
                {
                    currTime++;
                }
            }
            else
            {
                timer.cancel();
                timer.purge();
                currTime = 0;
                Log.i("MusicPlayer", "timer canceled");
            }
        }
    }

    public void pauseMusic()
    {
        if (player != null && player.isPlaying())
        {
            player.pause();
            currPos = player.getCurrentPosition();
            musicStatus = 2;
        }
        if (player2 != null && player2.isPlaying())
        {
            player2.pause();
            currPos2 = player2.getCurrentPosition();
            sound1Status = 2;
        }
        if (player3 != null && player3.isPlaying())
        {
            player3.pause();
            currPos3 = player3.getCurrentPosition();
            sound2Status = 2;
        }
        if (player4 != null && player4.isPlaying())
        {
            player4.pause();
            currPos4 = player4.getCurrentPosition();
            sound3Status = 2;
        }
    }

    public void resumeMusic()
    {
        if (player != null)
        {
            player.seekTo(currPos);
            player.start();
            musicStatus = 1;
        }
        if (player2 != null && sound1Status == 2)
        {
            player2.seekTo(currPos2);
            player2.start();
            sound1Status = 1;
        }
        if (player3 != null && sound2Status == 2)
        {
            player3.seekTo(currPos3);
            player3.start();
            sound2Status = 1;
        }
        if (player4 != null && sound3Status == 2)
        {
            player4.seekTo(currPos4);
            player4.start();
            sound3Status = 1;
        }
    }

    public void restartMusic()
    {
        if (musicStatus == 0)
        {
            return;
        }
        if (player != null)
        {
            player.stop();
        }
        currPos = 0;
        musicStatus = 0;
        if (player2 != null)
        {
            player2.stop();
        }
        currPos2 = 0;
        sound1Status = 0;
        if (player3 != null)
        {
            player3.stop();
        }
        currPos3 = 0;
        sound2Status = 0;
        if (player4 != null)
        {
            player4.stop();
        }
        currPos4 = 0;
        sound3Status = 0;
        musicService.onUpdatePicture(mainPicID);
        playMusic(title, sound1, sound2, sound3, prog1, prog2, prog3);
    }

    public void stopPlayer()
    {
        if (player != null)
        {
            player.release();
            player = null;
            musicStatus = 0;
            Log.i("MusicPlayer", "released player");
        }
        if (player2 != null)
        {
            player2.release();
            player2 = null;
            sound1Status = 0;
            Log.i("MusicPlayer", "released player2");
        }
        if (player3 != null)
        {
            player3.release();
            player3 = null;
            sound2Status = 0;
            Log.i("MusicPlayer", "released player3");
        }
        if (player4 != null)
        {
            player4.release();
            player4 = null;
            sound3Status = 0;
            Log.i("MusicPlayer", "released player4");
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mp.equals(player))
        {
            player.release();
            player = null;
            musicStatus = 0;
            Log.i("MusicPlayer", "released player");
            musicService.onUpdatePlayButton("Play");
        }
        if (mp.equals(player2))
        {
            player2.release();
            player2 = null;
            sound1Status = 0;
            Log.i("MusicPlayer", "released player2");
            musicService.onUpdatePicture(mainPicID);
        }
        if (mp.equals(player3))
        {
            player3.release();
            player3 = null;
            sound2Status = 0;
            Log.i("MusicPlayer", "released player3");
            musicService.onUpdatePicture(mainPicID);
        }
        if (mp.equals(player4))
        {
            player4.release();
            player4 = null;
            sound3Status = 0;
            Log.i("MusicPlayer", "released player4");
            musicService.onUpdatePicture(mainPicID);
        }
    }
}
