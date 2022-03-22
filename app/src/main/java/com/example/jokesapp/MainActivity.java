package com.example.jokesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arasthel.asyncjob.AsyncJob;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements JokeLikeListener {

    private CardStack mCardStack;
    private CardsDataAdapter mCardAdapter;
    private List<Joke> mAllJokes;
    private JokeManger mJokeManger;

    // shaking
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mJokeManger = new JokeManger(this);

        mAllJokes = new ArrayList<>();

        mCardStack = (CardStack)findViewById(R.id.container);
        mCardStack.setContentResource(R.layout.joke_view);
        mCardStack.setStackMargin(20);

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                handleShakeEvent();
            }
        });

        mCardAdapter = new CardsDataAdapter(this,0);

        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        // Do some background work
                        try {
                            JSONObject rootJSONObject = new JSONObject(loadJSONFromAssets());
                            JSONArray jsonJokes = rootJSONObject.getJSONArray("fat");

                            addJokesToArrayList(jsonJokes, mAllJokes);
                            jsonJokes = rootJSONObject.getJSONArray("stupid");

                            addJokesToArrayList(jsonJokes, mAllJokes);
                            jsonJokes = rootJSONObject.getJSONArray("ugly");

                            addJokesToArrayList(jsonJokes, mAllJokes);
                            jsonJokes = rootJSONObject.getJSONArray("nasty");

                            addJokesToArrayList(jsonJokes, mAllJokes);
                            jsonJokes = rootJSONObject.getJSONArray("hairy");

                            addJokesToArrayList(jsonJokes, mAllJokes);
                            jsonJokes = rootJSONObject.getJSONArray("bald");

                            addJokesToArrayList(jsonJokes, mAllJokes);
                            jsonJokes = rootJSONObject.getJSONArray("old");

                            addJokesToArrayList(jsonJokes, mAllJokes);
                            jsonJokes = rootJSONObject.getJSONArray("poor");

                            addJokesToArrayList(jsonJokes, mAllJokes);
                            jsonJokes = rootJSONObject.getJSONArray("short");

                            addJokesToArrayList(jsonJokes, mAllJokes);
                            jsonJokes = rootJSONObject.getJSONArray("skinny");

                            addJokesToArrayList(jsonJokes, mAllJokes);
                            jsonJokes = rootJSONObject.getJSONArray("tall");

                            addJokesToArrayList(jsonJokes, mAllJokes);
                            jsonJokes = rootJSONObject.getJSONArray("got");

                            addJokesToArrayList(jsonJokes, mAllJokes);
                            jsonJokes = rootJSONObject.getJSONArray("like");

                            addJokesToArrayList(jsonJokes, mAllJokes);
                            jsonJokes = rootJSONObject.getJSONArray("misc");

                            addJokesToArrayList(jsonJokes, mAllJokes);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {

                        for(Joke joke : mAllJokes){
                            mCardAdapter.add(joke.getJokeText());
                        }
                        mCardStack.setAdapter(mCardAdapter);
                    }
                }).create().start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSensorManager.unregisterListener(mShakeDetector);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(MainActivity.this, FavJokesActivity.class));
        return super.onOptionsItemSelected(item);

    }
    private void handleShakeEvent() {
        new AsyncJob.AsyncJobBuilder<Boolean>()
                .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {
                        Collections.shuffle(mAllJokes);
                        return true;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        mCardAdapter.clear();
                        mCardAdapter = new CardsDataAdapter(MainActivity.this, 0);
                        for(Joke joke: mAllJokes) {
                            mCardAdapter.add(joke.getJokeText());
                        }
                        mCardStack.setAdapter(mCardAdapter);
                    }
                }).create().start();

    }
}