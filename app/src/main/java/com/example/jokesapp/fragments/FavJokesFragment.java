package com.example.jokesapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jokesapp.R;
import com.example.jokesapp.controller.FavJokeListAdapter;
import com.example.jokesapp.model.Joke;
import com.example.jokesapp.model.JokeManger;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class FavJokesFragment extends Fragment {

    RecyclerView mRecyclerView;
    FavJokeListAdapter mFavJokeListAdapter;
    JokeManger mJokeManger;
    private List<Joke> mJokeList = new ArrayList<>();
    private Joke deletedJoke;

    public FavJokesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FavJokesFragment newInstance() {
        FavJokesFragment fragment = new FavJokesFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        mJokeManger = new JokeManger(context);
        mJokeList.clear();
        if(mJokeManger.retiriveJokes().size() > 0){
            for(Joke joke : mJokeManger.retiriveJokes()){
                mJokeList.add(joke);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_fav_jokes, container, false);

       if(view != null) {
           mRecyclerView = view.findViewById(R.id.rv);
           mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
           mFavJokeListAdapter = new FavJokeListAdapter(mJokeList,getContext());
           mRecyclerView.setAdapter(mFavJokeListAdapter);

           ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mSimpleCallBack);
           itemTouchHelper.attachToRecyclerView(mRecyclerView);
       }
       return view;
    }

    private ItemTouchHelper.SimpleCallback mSimpleCallBack = new ItemTouchHelper.SimpleCallback(0 , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position  = viewHolder.getAdapterPosition();
            switch(direction) {
                case ItemTouchHelper.LEFT:
                case ItemTouchHelper.RIGHT:

                    deletedJoke = mJokeList.get(position);
                    mJokeManger.deleteJoke(mJokeList.get(position));
                    mJokeList.remove(position);
                    mFavJokeListAdapter.notifyItemRemoved(position);
                    mFavJokeListAdapter.notifyDataSetChanged();

                    Snackbar.make(mRecyclerView, "Joke is \"Removed\"", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mJokeList.add(position, deletedJoke);
                                    mJokeManger.saveJoke(deletedJoke);
                                    mFavJokeListAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;
            }
        }
    };
}