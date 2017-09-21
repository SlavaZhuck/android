package com.example.juk_va.recyclerviewplayer;

import android.content.ContentUris;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;

import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Song> mSongList;
    private RecyclerView mRV;
    private TextView mSong;
    private final long delayForButtons = 400;
    private TextView mArtist;
    private int mSongNumber = 0;
    private boolean mButtonClick = false;
    public static long mTimeStamp = 0;
    MediaPlayer mediaPlayer;
    private Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRV = (RecyclerView) findViewById(R.id.recycleView);
        mSong = (TextView) findViewById(R.id.song);
        mArtist = (TextView) findViewById(R.id.artist);

        mSongList = new ArrayList<>();
        getmSongList();

        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRV.setLayoutManager(llm);
        mRV.setHasFixedSize(true);
        initializeAdapter();
        mediaPlayer = new MediaPlayer();

        mRV.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getBaseContext(), mRV, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        setDataText(position);
                        mSongNumber = position;
                        playSong();
                    }
                })
        );

        t = new Thread(new Runnable() {
            public void run() {
                while(true) {
                    if (mButtonClick == true) {
                        if (System.currentTimeMillis() - mTimeStamp == delayForButtons) {
                            playSong();
                            mButtonClick = false;
                        }
                    }
                }
            }
        });
        t.start();
    }

    public void getmSongList() {
        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                mSongList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
        musicCursor.close();
    }

    private void initializeAdapter() {
        RecycleViewSongAdapter adapter = new RecycleViewSongAdapter(mSongList);
        mRV.setAdapter(adapter);
    }

    private void setDataText(int pos) {
        mSong.setText(mSongList.get(pos).getTitle());
        mArtist.setText(mSongList.get(pos).getArtist());
    }

    public void onClickBack(View view) {
        mButtonClick = true;
        mTimeStamp = System.currentTimeMillis();
        if (mSongNumber == 0)
            mSongNumber = mSongList.size() - 1;
        else
            mSongNumber--;
        setDataText(mSongNumber);
    }

    public void onClickPlay(View view) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public void onClickForward(View view) {
        mButtonClick = true;
        mTimeStamp = System.currentTimeMillis();
        if (mSongNumber == mSongList.size() - 1)
            mSongNumber = 0;
        else
            mSongNumber++;
        setDataText(mSongNumber);
    }

    public void playSong() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        int currentAudio = (int) mSongList.get(mSongNumber).getID();
        Uri trackUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentAudio);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), trackUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}