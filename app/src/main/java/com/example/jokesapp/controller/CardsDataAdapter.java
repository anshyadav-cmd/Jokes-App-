package com.example.jokesapp.controller;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jokesapp.R;
import com.example.jokesapp.model.Joke;
import com.example.jokesapp.model.JokeManger;

import java.util.List;

public class CardsDataAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private boolean isLiked = false;
    private JokeLikeListener mJokeLikeListener;
    private Joke mJoke;
    private JokeManger mJokeManger;

    @Override
    public void add(@Nullable String object) {
        super.add(object);
    }

    public CardsDataAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mContext = context;
        mJokeLikeListener = (JokeLikeListener) context;
        mJokeManger = new JokeManger(context);
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){
        //supply the layout for your card
        TextView v = (TextView)(contentView.findViewById(R.id.content));
        v.setText(getItem(position));

        // Like Button coding
        ImageButton likeButton = contentView.findViewById(R.id.likeButton);

        alreadyLiked(likeButton, position);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "Like is clicked", Toast.LENGTH_SHORT).show();
                if(!isLiked){
                    likeButton.setImageResource(R.drawable.liked_filled);
                    isLiked = true;

                    mJoke = new Joke(getItem(position), true);
                    mJokeLikeListener.jokeIsLiked(mJoke);
                }else {
                    likeButton.setImageResource(R.drawable.like_empty);
                    isLiked = false;
                    mJoke = new Joke(getItem(position), false);
                    mJokeLikeListener.jokeIsLiked(mJoke);
                }
            }
        });

        // Share button coding
        ImageButton shareButton = contentView.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                String shareBody = v.getText().toString();
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Mama Joke!");
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                v.getContext().startActivity(Intent.createChooser(intent,"Share via"));
            }
        });

        return contentView;
    }

    public void alreadyLiked(ImageButton imageButton, int position){
        List<Joke> jokes = mJokeManger.retiriveJokes();
        for(Joke joke : jokes) {
            if(getItem(position).equals(joke.getJokeText())) {
                if (joke.isJokeLiked()) {
                    imageButton.setImageResource(R.drawable.liked_filled);
                } else {
                    imageButton.setImageResource((R.drawable.like_empty));
                }
            }
        }
    }
}
