package com.guguluk.fuckexam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.guguluk.fuckexam.util.ObscuredSharedPreferences;

import java.util.Random;


public class main extends ActionBarActivity {

    private TextView fuck;
    private Animation fadeOut;
    private boolean stared = false;
    private long lastFuck;
    private int count = 0;
    private long diffMs = 250;
    private Toast lastToast = null;
    private MediaPlayer lastPlayer = null;
    private char[] songList = {'a','b','c','d','e','f','g','h','i','j'};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load();
        //
        fuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fuck.startAnimation(fadeOut);
                if(!stared) { stared = !stared; lastFuck = System.currentTimeMillis(); count++; playSong(); return; }
                else {
                    if(System.currentTimeMillis() - lastFuck >= diffMs) {
                        if(lastToast != null) { lastToast.cancel(); }
                        lastToast = Toast.makeText(getApplicationContext(), "Total Fuck : " + Integer.toString(count), Toast.LENGTH_LONG);
                        fuckScore(count);
                        lastToast.show();
                        count = 0;
                        stared = !stared;
                    } else {
                        lastFuck = System.currentTimeMillis();
                        count++;
                        playSong();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fuck, menu);
        //
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //
        if(id == R.id.best_score) {
            AlertDialog alertMessage = new AlertDialog.Builder(this).create();
            alertMessage.setTitle("Best Score");
            alertMessage.setMessage(Integer.toString(getScore()));
            alertMessage.show();
        }
        //
        return super.onOptionsItemSelected(item);
    }

    private void load() {
        fuck = (TextView) findViewById(R.id.fuck);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_out);
    }

    private int getScore() {
        SharedPreferences prefs = new ObscuredSharedPreferences(this, getSharedPreferences("data", Context.MODE_PRIVATE));
        return prefs.getInt("score", 0);
    }

    private void setScore(int score) {
        SharedPreferences prefs = new ObscuredSharedPreferences(this, getSharedPreferences("data", Context.MODE_PRIVATE));
        prefs.edit().putInt("score", score).commit();
    }

    private void fuckScore(int score) {
        if(score > getScore()) { setScore(score); }
    }

    private void playSong() {
        if(lastPlayer==null || !lastPlayer.isPlaying()) {
            Random random = new Random();
            int id = getResources().getIdentifier("raw/" + songList[random.nextInt(songList.length - 1)], null, this.getPackageName());
            lastPlayer = MediaPlayer.create(this, id);
            lastPlayer.start();
        }
    }
}
