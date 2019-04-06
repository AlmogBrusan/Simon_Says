package com.example.simonsaysproject;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.github.jinatonic.confetti.CommonConfetti;
import com.shashank.sony.fancytoastlib.FancyToast;
import java.util.List;

public class HighScore extends AppCompatActivity {

    private HighScoreManager hm = new HighScoreManager();
    private RecyclerView recyclerView;
    private HighScoreAdapter adapter;
    private ImageButton info;
    private SharedPreferences settings;
    private int lastScore;
    private boolean isPlayed;
    private RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        initView();
    }

    //Initialize all UI elements
    private void initView()
    {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        container = findViewById(R.id.container2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        settings = getSharedPreferences("PREFERENCES",MODE_PRIVATE);
        lastScore = getIntent().getIntExtra("isInTable", 0);
        isPlayed  = getIntent().getBooleanExtra("isPlayed",false);



        if(isNetworkAvailable()) {
            hm.getHighScore(new HighScoreManager.highScoreListener() { // ok, the client is connected to internet
                @Override
                public void onChange(List<UserScore> highScores) {
                    displayHighScores(highScores);
                }

                @Override
                public void onError() {
                    FancyToast.makeText(HighScore.this, getString(R.string.server_problem), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

                }
            });
        }
        else
            FancyToast.makeText(HighScore.this, getString(R.string.no_connection), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

        info = findViewById(R.id.highscore_info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                com.geniusforapp.fancydialog.FancyAlertDialog.Builder alert = new com.geniusforapp.fancydialog.FancyAlertDialog.Builder(HighScore.this)
                        .setimageResource(R.drawable.score)
                        .setTextTitle(getString(R.string.high_score))

                        .setBody(getString(R.string.high_score_info))
                        .setNegativeColor(R.color.colorRipple)



                        .setPositiveButtonText(getString(R.string.ok))
                        .setPositiveColor(R.color.colorPrimary)
                        .setOnPositiveClicked(new com.geniusforapp.fancydialog.FancyAlertDialog.OnPositiveClicked() {
                            @Override
                            public void OnClick(View view, Dialog dialog) {
                                dialog.dismiss();

                            }
                        }).setButtonsGravity(com.geniusforapp.fancydialog.FancyAlertDialog.PanelGravity.CENTER)
                        .setBodyGravity(com.geniusforapp.fancydialog.FancyAlertDialog.TextGravity.CENTER)
                        .setTitleGravity(com.geniusforapp.fancydialog.FancyAlertDialog.TextGravity.CENTER)

                        .setCancelable(true)
                        .build();
                alert.show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    //Display High Scores
    private void displayHighScores(List<UserScore> highScores)
    {
        if (isPlayed){
            isEnterTable(highScores);
        }
        adapter = new HighScoreAdapter(highScores);
        recyclerView.setAdapter(adapter);
    }

    public String readSetting(String key)
    {
        String value;
        value = settings.getString(key, "");
        return value;
    }

    private void isEnterTable(List<UserScore> highScores){ // Check after a game if the user entered to Top 15
        int temp;
        isPlayed = false;
        temp= highScores.get(14).getScore();

        if(lastScore > temp){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CommonConfetti.rainingConfetti(container, new int[] { Color.YELLOW ,Color.RED ,Color.BLUE ,Color.GREEN   }).stream(7000);
                        }
                    });
                }
            }, 100);

            playSound(R.raw.winning);
            FancyToast.makeText(HighScore.this, getString(R.string.in_table), FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false ).show();

        }else {
            FancyToast.makeText(HighScore.this, getString(R.string.not_in_table), FancyToast.LENGTH_LONG, FancyToast.INFO, false ).show();

        }
    }

    // Play the Sound
    private void playSound(int idOfSound) {

        if(readSetting("sound").equals("true")) {
            try {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), idOfSound);
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }

                assert mediaPlayer != null;
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(@NonNull MediaPlayer mp) {
                        if (mp.isPlaying()) {
                            mp.stop();
                        }

                        mp.reset();
                        mp.release();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        BackgroundMusicService.pause++;
        Intent serviceIntent = new Intent(HighScore.this, BackgroundMusicService.class);
        startService(serviceIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        BackgroundMusicService.pause--;
        Intent serviceIntent = new Intent(HighScore.this, BackgroundMusicService.class);
        startService(serviceIntent);
    }


}
