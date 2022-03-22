package com.example.jokesapp.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jokesapp.R;

public class CardsDataAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private boolean isLiked = false;

    @Override
    public void add(@Nullable String object) {
        super.add(object);
    }

    public CardsDataAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mContext = context;
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){
        //supply the layout for your card
        TextView v = (TextView)(contentView.findViewById(R.id.content));
        v.setText(getItem(position));
        ImageButton likeButton = contentView.findViewById(R.id.likeButton);
        ImageButton shareButton = contentView.findViewById(R.id.shareButton);


        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "Like is clicked", Toast.LENGTH_SHORT).show();
                if(!isLiked){
                    likeButton.setImageResource(R.drawable.liked_filled);
                    isLiked = true;
                }else {
                    likeButton.setImageResource(R.drawable.like_empty);
                    isLiked = false;
                }
            }
        });

        return contentView;
    }
}
