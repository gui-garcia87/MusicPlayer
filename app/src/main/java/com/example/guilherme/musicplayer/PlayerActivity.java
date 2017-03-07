package com.example.guilherme.musicplayer;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.example.guilherme.musicplayer.R.raw.song;

/**
 * Created by Guilherme on 03/03/2017.
 */

public class PlayerActivity extends AppCompatActivity{

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        mediaPlayer = MediaPlayer.create(this, song);

        Button playButton = (Button) findViewById(R.id.play_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Toast.makeText(PlayerActivity.this, "I'm done", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });


        Button pauseButton = (Button) findViewById(R.id.pause_button);

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();

            }
        });

        Button resetButton = (Button) findViewById(R.id.reset_button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mediaPlayer.reset();
                    AssetFileDescriptor afd = null;
                    afd = getResources().openRawResourceFd(R.raw.song);
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mediaPlayer.prepareAsync();
                    //mediaPlayer.setDataSource("C:/Users/Guilherme/AndroidStudioProjects/MusicPlayer/app/src/main/res/raw/song.mp3");
                    mediaPlayer.prepare();
                    mediaPlayer.stop();
                    releaseMediaPlayer();
                } catch(Exception e){
                        e.printStackTrace();
                }
            }
        });
    }

    private void releaseMediaPlayer(){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
