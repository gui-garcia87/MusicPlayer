package com.example.guilherme.musicplayer;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import static com.example.guilherme.musicplayer.R.layout.activity_player;
import static com.example.guilherme.musicplayer.R.raw.song;

/**
 * Created by Guilherme on 03/03/2017.
 */

public class PlayerActivity extends AppCompatActivity{

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;

    private double startTime = 0;
    private double finalTime = 0;

    private Handler handler = new Handler();
    private int forwardTime = 5000;
    private int backwardTime = 5000;

    public static int oneTimeOnly = 0;

    private TextView tx1,tx2,tx3;
    private Button playButton;
    private Button pauseButton;
    private Button resetButton;
    private Button backButton;
    private Button forwardButton;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(activity_player);

        mediaPlayer = MediaPlayer.create(this, song);

        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        playButton = (Button) findViewById(R.id.play_button);
        resetButton = (Button) findViewById(R.id.reset_button);
        pauseButton = (Button) findViewById(R.id.pause_button);
        backButton = (Button) findViewById(R.id.buttonBack);
        forwardButton = (Button) findViewById(R.id.buttonForward);



        seekBar.setClickable(false);
        pauseButton.setEnabled(false);


        tx1 = (TextView) findViewById(R.id.textView1);
        tx1.setText(String.format("%d min, %d sec", 0, 0));
        tx2 = (TextView) findViewById(R.id.textView2);
        tx2.setText("Song.mp3");
        tx3 = (TextView) findViewById(R.id.textView3);
        tx3.setText(String.format("%d min, %d sec", 0, 0));


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayerActivity.this,"Playing", Toast.LENGTH_SHORT).show();
                mediaPlayer.start();

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if(oneTimeOnly == 0){
                    seekBar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }

                tx3.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        finalTime))));

                tx1.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        startTime))));

                seekBar.setProgress((int) startTime);
                handler.postDelayed(UpdateSongTime,100);
                playButton.setEnabled(false);
                pauseButton.setEnabled(true);


                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        Toast.makeText(PlayerActivity.this, "I'm done", Toast.LENGTH_SHORT).show();
                        playButton.setEnabled(true);
                        resetButton.callOnClick();

                    }
                });
            }
        });



        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayerActivity.this,"Pausing", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                playButton.setEnabled(true);

            }
        });


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayerActivity.this,"Reset", Toast.LENGTH_SHORT).show();
                try {
                    mediaPlayer.reset();
                    AssetFileDescriptor afd = null;
                    afd = getResources().openRawResourceFd(R.raw.song);
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mediaPlayer.prepareAsync();
                    mediaPlayer.prepare();
                    mediaPlayer.stop();
                    releaseMediaPlayer();
                } catch(Exception e){
                        e.printStackTrace();
                }
            }
        });


        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int)startTime;

                if((temp+forwardTime)<=finalTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"Jump forward 5",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"No Jump",Toast.LENGTH_SHORT).show();
                }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int)startTime;

                if((temp-backwardTime)>0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getApplicationContext(),"Jump back 5",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"No Jump",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            tx1.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekBar.setProgress((int)startTime);
            handler.postDelayed(this, 100);
        }
    };

    private void releaseMediaPlayer(){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
