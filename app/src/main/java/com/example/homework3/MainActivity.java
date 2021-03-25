package com.example.homework3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner songSpinner;
    Spinner sound1Spinner;
    SeekBar sound1Seek;
    Spinner sound2Spinner;
    SeekBar sound2Seek;
    Spinner sound3Spinner;
    SeekBar sound3Seek;

    private String title;
    private String sound1;
    private String sound2;
    private String sound3;

    private static final String LOG_NAME = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songSpinner = (Spinner) findViewById(R.id.spinnerSong);
        songSpinner.setOnItemSelectedListener(this);

        sound1Spinner = (Spinner) findViewById(R.id.spinnerSound1);
        sound1Spinner.setOnItemSelectedListener(this);
        sound1Seek = (SeekBar) findViewById(R.id.seekBarSound1);

        sound2Spinner = (Spinner) findViewById(R.id.spinnerSound2);
        sound2Spinner.setOnItemSelectedListener(this);
        sound2Seek = (SeekBar) findViewById(R.id.seekBarSound2);

        sound3Spinner = (Spinner) findViewById(R.id.spinnerSound3);
        sound3Spinner.setOnItemSelectedListener(this);
        sound3Seek = (SeekBar) findViewById(R.id.seekBarSound3);

        if (savedInstanceState != null)
        {
            songSpinner.setSelection(savedInstanceState.getInt("song selection"));
            sound1Spinner.setSelection(savedInstanceState.getInt("sound 1 selection"));
            sound2Spinner.setSelection(savedInstanceState.getInt("sound 2 selection"));
            sound3Spinner.setSelection(savedInstanceState.getInt("sound 3 selection"));
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("song selection", songSpinner.getSelectedItemPosition());
        outState.putInt("sound 1 selection", sound1Spinner.getSelectedItemPosition());
        outState.putInt("sound 2 selection", sound2Spinner.getSelectedItemPosition());
        outState.putInt("sound 3 selection", sound3Spinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    public void clickedPlay(View view)
    {
        Intent stopIntent = new Intent(this, MusicService.class);
        boolean wasStopped = stopService(stopIntent);
        Log.i(LOG_NAME, "Was stopped? " + wasStopped);

        Intent i1 = new Intent(this, PlayerActivity.class);
        i1.putExtra("music title", title);
        i1.putExtra("sound 1", sound1);
        i1.putExtra("sound 2", sound2);
        i1.putExtra("sound 3", sound3);
        i1.putExtra("start 1", sound1Seek.getProgress());
        i1.putExtra("start 2", sound2Seek.getProgress());
        i1.putExtra("start 3", sound3Seek.getProgress());
        startActivity(i1);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i(LOG_NAME, "onItemSelected called");
        switch (parent.getId())
        {
            case R.id.spinnerSong:
                title = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinnerSound1:
                sound1 = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinnerSound2:
                sound2 = parent.getItemAtPosition(position).toString();
                break;
            case R.id.spinnerSound3:
                sound3 = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}