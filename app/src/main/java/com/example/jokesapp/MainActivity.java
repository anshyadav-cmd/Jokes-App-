package com.example.jokesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.jokesapp.controller.CardsDataAdapter;
import com.example.jokesapp.controller.JokeLikeListener;
import com.example.jokesapp.model.Joke;
import com.example.jokesapp.model.JokeManger;
import com.wenchao.cardstack.CardStack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements JokeLikeListener {

    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;
    private List<Joke> mAllJokes;
    private JokeManger mJokeManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mJokeManger = new JokeManger(this);

        mAllJokes = new ArrayList<>();

        mCardStack = (CardStack)findViewById(R.id.container);
        mCardStack.setContentResource(R.layout.joke_view);
        mCardStack.setStackMargin(20);

        mCardAdapter = new CardsDataAdapter(this,0);

        try {
            JSONObject rootJSONObject = new JSONObject(loadJSONFromAssets());
            JSONArray fatJokes = rootJSONObject.getJSONArray("fat");
            addJokesToArrayList(fatJokes, mAllJokes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(Joke joke : mAllJokes){
            mCardAdapter.add(joke.getJokeText());
        }
        mCardStack.setAdapter(mCardAdapter);
    }

    private String loadJSONFromAssets() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("jokes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private void addJokesToArrayList(JSONArray jsonArray , List<Joke> arrayList){
        try{
            if(jsonArray != null) {
                for(int i = 0; i < jsonArray.length(); i++) {
                    arrayList.add(new Joke(jsonArray.getString(i), false));
                }
            }
        }catch(JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void jokeIsLiked(Joke joke) {
        if(joke.isJokeLiked()){
            mJokeManger.saveJoke(joke);
        }else {
            mJokeManger.deleteJoke(joke);
        }
    }
}