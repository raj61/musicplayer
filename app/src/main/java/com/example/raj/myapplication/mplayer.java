package com.example.raj.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class mplayer extends AppCompatActivity{
    private ArrayList<String> songarray = new ArrayList<String>();
    private ArrayList<String> songpath = new ArrayList<String>();
    final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath()+"/";
    private ArrayList<HashMap<String,String>> songList = new ArrayList<HashMap<String, String>>();
    private String mp3Pattern = ".mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mplayer);


        if(MEDIA_PATH != null){
            File home = new File(MEDIA_PATH);
            File[] listFiles = home.listFiles();
            if(listFiles != null && listFiles.length > 0){
                for(File file : listFiles){
                    if(file.isDirectory()){
                        scanDirectory(file);
                    }
                    else {
                        addSongToList(file);
                    }
                }
            }
        }
        ListView li = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> me = new ArrayAdapter<String>(this,R.layout.activity_listview,songarray);
        li.setAdapter(me);
        AdapterView.OnItemClickListener liad = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent in = new Intent(mplayer.this,mplayer_player.class);
                Bundle b = new Bundle();
                b.putString("position",position+"");
                b.putStringArrayList("songarray",songarray);
                b.putStringArrayList("songpath",songpath);
                in.putExtras(b);
                startActivity(in);
            }
        };
        li.setOnItemClickListener(liad);
    }



    private void scanDirectory(File directory){
        if(directory != null){
            File[] listFiles = directory.listFiles();
            if(listFiles != null && listFiles.length > 0){
                for(File file : listFiles){
                    if(file.isDirectory()){
                        scanDirectory(file);
                    }
                    else
                    {
                        addSongToList(file);
                    }
                }
            }
        }
    }
    private void addSongToList(File song){
        if(song.getName().endsWith(mp3Pattern)){
            HashMap<String,String> songMap = new HashMap<String,String>();
            songMap.put("songTitle",song.getName().substring(0,(song.getName().length()-4)));
            songMap.put("songPath",song.getPath());

            songList.add(songMap);
            songarray.add(song.getName().substring(0,(song.getName().length()-4)));
            songpath.add(song.getPath());

        }
    }
}
