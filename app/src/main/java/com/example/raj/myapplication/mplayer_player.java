package com.example.raj.myapplication;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class mplayer_player extends AppCompatActivity implements View.OnClickListener{
    ImageButton btnplay,songprev,songnext;
    int pos,tottime,totmin,totsec;
    ArrayList<String> songarray,songpath;
    MediaPlayer mediaPlayer = new MediaPlayer();
    TextView songname,totaltime,curtime;
    SeekBar s;
    class mythread extends Thread{
        int i;
        int pau=0;
        mythread(int i){
            this.i = i;
        }
        public void run(){

                for (; i < tottime; i++) {
                    while(pau == 1) {
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    s.setProgress(i);
                    try {
                        sleep(300);
                        i = i + 300;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

        }
        public void customset(int i){
            this.i = i;
        }
        public void custompause(int p){
            pau=p;
        }
    };
    mythread mth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mplayer_player);
        Intent in = getIntent();
        String position = in.getStringExtra("position");
        pos = Integer.parseInt(position);
        songarray = in.getStringArrayListExtra("songarray");
        songpath = in.getStringArrayListExtra("songpath");
        songname = (TextView)findViewById(R.id.songname);
        songname.setText(songarray.get(pos));


        try {
            mediaPlayer.setDataSource(songpath.get(pos));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        curtime = (TextView)findViewById(R.id.curtime);

        MediaPlayer.OnBufferingUpdateListener buf = new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                curtime.setText(percent+"");

            }
        };
        mediaPlayer.setOnBufferingUpdateListener(buf);
        totaltime = (TextView)findViewById(R.id.totaltime);
         tottime = mediaPlayer.getDuration();
        totmin = tottime/60000;
         totsec = (tottime%60000)/1000;
        totaltime.setText(totmin+":"+totsec);

        btnplay = (ImageButton)findViewById(R.id.btnplay);
        btnplay.setOnClickListener(this);
        songprev = (ImageButton)findViewById(R.id.songprev);
        songprev.setOnClickListener(this);
        songnext = (ImageButton)findViewById(R.id.songnext);
        songnext.setOnClickListener(this);
        mediaPlayer.start();
        mth = new mythread(0);
        mth.start();
        s = (SeekBar)findViewById(R.id.seekBar);
        s.setMax(tottime);



        SeekBar.OnSeekBarChangeListener seekch = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(seekBar.getProgress() == seekBar.getMax())
                {
                    pos = pos+1;
                    Intent in = new Intent(mplayer_player.this,mplayer_player.class);
                    Bundle b = new Bundle();
                    b.putString("position",pos+"");
                    b.putStringArrayList("songarray",songarray);
                    b.putStringArrayList("songpath",songpath);
                    in.putExtras(b);
                    startActivity(in);
                    finish();
                }
                int curmin = progress/60000;
                int cursec = (progress%60000)/1000;
                curtime.setText(curmin+":"+cursec);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());
                mth.customset(seekBar.getProgress());

            }
        };
        s.setOnSeekBarChangeListener(seekch);




    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if(i == R.id.btnplay){
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnplay.setImageResource(android.R.drawable.ic_media_play);
                mth.custompause(1);


            }
            else
            {
                mediaPlayer.start();
                btnplay.setImageResource(android.R.drawable.ic_media_pause);
                mth.custompause(0);

            }

        }
        if( i == R.id.songnext){
            Toast.makeText(this,"Next",Toast.LENGTH_SHORT).show();
            pos = pos+1;
            Intent in = new Intent(mplayer_player.this,mplayer_player.class);
            Bundle b = new Bundle();
            b.putString("position",pos+"");
            b.putStringArrayList("songarray",songarray);
            b.putStringArrayList("songpath",songpath);
            in.putExtras(b);
            startActivity(in);
            finish();

        }
        if( i == R.id.songprev){
            Toast.makeText(this,"Prev",Toast.LENGTH_SHORT).show();
            pos = pos-1;
            Intent in = new Intent(mplayer_player.this,mplayer_player.class);
            Bundle b = new Bundle();
            b.putString("position",pos+"");
            b.putStringArrayList("songarray",songarray);
            b.putStringArrayList("songpath",songpath);
            in.putExtras(b);
            startActivity(in);
            finish();

        }

    }
    @Override
    protected void onPause(){
        super.onPause();
        mediaPlayer.pause();

    }
}
