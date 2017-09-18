package com.example.juk_va.recyclerviewplayer;

import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    MediaStore.Audio.Media mMediaStoreAudio;
    final String track_id = MediaStore.Audio.Media._ID;
    final String track_no = MediaStore.Audio.Media.TRACK;
    final String track_name = MediaStore.Audio.Media.TITLE;
    final String artist = MediaStore.Audio.Media.ARTIST;
    final String duration = MediaStore.Audio.Media.DURATION;
    final String album = MediaStore.Audio.Media.ALBUM;
    final String composer = MediaStore.Audio.Media.COMPOSER;
    final String year = MediaStore.Audio.Media.YEAR;
    final String path = MediaStore.Audio.Media.DATA;
    final String date_added = MediaStore.Audio.Media.DATE_ADDED;
    Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    ArrayList audio=new ArrayList();

    AudioManager am;
    private RecyclerView rv;
    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        mMediaStoreAudio = new MediaStore.Audio.Media();
        Uri uri = MediaStore.Audio.Media.getContentUri("external");
        String[] proj = { MediaStore.Audio.Media.DATA };
        int size = proj.length;
        String song = proj[0];
        rv = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
    }


    public void onClickBack(View view) {

    }

    public void onClickPlay(View view) {
    }

    public void onClickForward(View view) {
    }
}