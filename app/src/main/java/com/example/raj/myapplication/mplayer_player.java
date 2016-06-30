package com.example.raj.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class mplayer_player extends AppCompatActivity implements View.OnClickListener, AudioManager.OnAudioFocusChangeListener {
    ImageButton btnplay,songprev,songnext;
    int pos,tottime,totmin,totsec;
    AudioManager am= null;
    ImageView imageView;
    ArrayList<String> songarray,songpath;
    MediaPlayer mediaPlayer = new MediaPlayer();
    TextView songname,totaltime,curtime,artist;
    Bitmap img;
    SeekBar s;
//    NewMessageNotification notification;
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange){
            case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                //lower the volume
                mediaPlayer.setVolume(0.2f,0.2f);
                break;
            case (AudioManager.AUDIOFOCUS_LOSS):
                mediaPlayer.pause();
                break;
            case (AudioManager.AUDIOFOCUS_GAIN):
                mediaPlayer.setVolume(1f,1f);
                mediaPlayer.start();
                break;
            default:break;

        }
    }


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
    mythread mth = new mythread(0);



    MediaMetadataRetriever mr = new MediaMetadataRetriever();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mplayer_player);
        Intent in = getIntent();
        String position = in.getStringExtra("position");
        pos = Integer.parseInt(position);
        songarray = in.getStringArrayListExtra("songarray");
        songpath = in.getStringArrayListExtra("songpath");
        artist = (TextView)findViewById(R.id.artist);
        songname = (TextView)findViewById(R.id.songname);
        imageView = (ImageView)findViewById(R.id.imageView);
        totaltime = (TextView)findViewById(R.id.totaltime);
        btnplay = (ImageButton)findViewById(R.id.btnplay);
        songprev = (ImageButton)findViewById(R.id.songprev);
        songnext = (ImageButton)findViewById(R.id.songnext);
        s = (SeekBar)findViewById(R.id.seekBar);
        curtime = (TextView)findViewById(R.id.curtime);
        btnplay.setOnClickListener(this);

        songprev.setOnClickListener(this);

        songnext.setOnClickListener(this);
//

        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        // Request audio focus for playback
        am.requestAudioFocus(this,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);
        // notification
//        notification = new NewMessageNotification();
//        notification.notify(this,mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),1);

        resetView();
        mth.start();
        SeekBar.OnSeekBarChangeListener seekch = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(seekBar.getProgress() == seekBar.getMax())
                {
                    try {
                        nextsong();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
//            Toast.makeText(this,"Next",Toast.LENGTH_SHORT).show();
//            pos = pos+1;
//            Intent in = new Intent(mplayer_player.this,mplayer_player.class);
//            Bundle b = new Bundle();
//            b.putString("position",pos+"");
//            b.putStringArrayList("songarray",songarray);
//            b.putStringArrayList("songpath",songpath);
//            in.putExtras(b);
//            startActivity(in);
//            finish();
            try {
                nextsong();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if( i == R.id.songprev){


            prevsong();

        }

    }
    public void nextsong() throws IOException {
        mediaPlayer.reset();
        pos = pos+1;
       resetView();
    }
    public void prevsong() {
        mediaPlayer.reset();
        pos = pos-1;
        resetView();
    }

    public void resetView(){

        mr.setDataSource(songpath.get(pos));
//        notification.cancel(this);
//        notification.notify(this,mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),1);
        if(mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)!=null)
            songname.setText(mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        else
            songname.setText(songarray.get(pos));
        if(mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)!=null)
            artist.setText(mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
//        byte[] data = mr.getEmbeddedPicture();
//        if(data!=null)
//        {
//            img = BitmapFactory.decodeByteArray(data,0,data.length);
//            imageView.setImageBitmap(img);
//            imageView.setAdjustViewBounds(true);
//            imageView.setLayoutParams(new LinearLayout.LayoutParams(500,500));
//        }

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




        tottime = mediaPlayer.getDuration();
        totmin = tottime/60000;
        totsec = (tottime%60000)/1000;
        totaltime.setText(totmin+":"+totsec);

        mth.customset(0);
        s.setProgress(0);
        mediaPlayer.start();



        s.setMax(tottime);




    }
    @Override
    protected void onPause(){
        super.onPause();
//        mediaPlayer.pause();

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
//        mediaPlayer.pause();
    }
}
