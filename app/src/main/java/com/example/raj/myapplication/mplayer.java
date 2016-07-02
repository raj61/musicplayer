package com.example.raj.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class mplayer extends AppCompatActivity{
    private ArrayList<String> songarray = new ArrayList<String>();
    private ArrayList<String> songpath = new ArrayList<String>();
    final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath()+"/";
    private ArrayList<HashMap<String,String>> songList = new ArrayList<HashMap<String, String>>();
    private String mp3Pattern = ".mp3";
    Spinner s;
    MediaMetadataRetriever mr = new MediaMetadataRetriever();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mplayer);
        File filei = getBaseContext().getFileStreamPath("playlist");
        s = (Spinner)findViewById(R.id.spinner);
        s.setVisibility(View.VISIBLE);
        if(filei.exists())
        {
            Toast.makeText(this,"Playlist exists ",Toast.LENGTH_SHORT).show();
            try {
                FileInputStream infile = openFileInput("playlist");
                ObjectInputStream is = new ObjectInputStream(infile);
                songList = (ArrayList<HashMap<String,String>>) is.readObject();
                for( HashMap<String,String> song:songList){
                    songarray.add(song.get("songTitle"));
                    songpath.add(song.get("songPath"));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            s.setVisibility(View.GONE);
        }
        else {

            if (MEDIA_PATH != null) {
                File home = new File(MEDIA_PATH);
                File[] listFiles = home.listFiles();
                if (listFiles != null && listFiles.length > 0) {
                    for (File file : listFiles) {
                        if (file.isDirectory()) {
                            scanDirectory(file);
                        } else {
                            addSongToList(file);
                        }
                    }
                }
            }
            try {
                FileOutputStream outfile = openFileOutput("playlist",MODE_PRIVATE);
                ObjectOutput ois = new ObjectOutputStream(outfile);
                ois.writeObject(songList);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            s.setVisibility(View.GONE);

        }
        int size = songpath.size();

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
            String title = "";
            try{
                mr.setDataSource(song.getPath());
                title = mr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

            }
            catch (Exception e)
            {

            }
            String name =song.getName().substring(0,(song.getName().length()-4));
            if(title == null || title.length()<=0)
            {
                songMap.put("songTitle",name);
                songarray.add(name);
            }
            else
            {
                songMap.put("songTitle",title);
                songarray.add(title);
            }

            songMap.put("songPath",song.getPath());

            songList.add(songMap);

            songpath.add(song.getPath());

        }
    }
}
