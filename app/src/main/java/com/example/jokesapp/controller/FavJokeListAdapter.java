package com.example.jokesapp.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jokesapp.R;
import com.example.jokesapp.model.Joke;
import com.example.jokesapp.view.FavJokeViewHolder;

import java.util.List;

public class FavJokeListAdapter extends RecyclerView.Adapter<FavJokeViewHolder> {

    private List<Joke> mJokeList;
    private Context mContext;

    public FavJokeListAdapter(List<Joke> jokeList, Context context) {
        mJokeList = jokeList;
        mContext = context;
    }

    @NonNull
    @Override
    public FavJokeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_joke_item, parent, false);
        return new FavJokeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavJokeViewHolder holder, int position) {
        String jokeText = mJokeList.get(position).getJokeText();
        holder.getTxtFavJoke().setText(jokeText);
    }

    @Override
    public int getItemCount() {
        return mJokeList.size();
    }
}
