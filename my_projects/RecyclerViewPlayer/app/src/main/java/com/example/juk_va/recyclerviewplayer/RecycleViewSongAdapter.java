package com.example.juk_va.recyclerviewplayer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 18.09.2017.
 */

public class RecycleViewSongAdapter extends RecyclerView.Adapter<RecycleViewSongAdapter.SongViewHolder> {


    public static class SongViewHolder extends RecyclerView.ViewHolder {

        TextView songTitle;
        TextView songArtist;

        SongViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_title);
            songArtist = itemView.findViewById(R.id.song_artist);
        }
    }

    ArrayList<Song> songs;

    RecycleViewSongAdapter(ArrayList<Song> songs){
        this.songs = songs;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song, parent, false);
        SongViewHolder pvh = new SongViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(SongViewHolder songViewHolder, int position) {
        songViewHolder.songTitle.setText(songs.get(position).getTitle());
        songViewHolder.songArtist.setText(songs.get(position).getArtist());
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}
