package com.example.simonsaysproject;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;


public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    private Button btnStartGame;
    private Button btnHighScore;
    private Button btnSettings;
    private Button btn1o1;
    public SharedPreferences settings ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initView();

    }

    private String readSetting(String key) {
        String value;
        value = settings.getString(key, "");
        return value;
    }


    //Initialize all UI elements
    private void initView() {
        settings = getSharedPreferences("PREFERENCES", MODE_PRIVATE);

        btnStartGame = findViewById(R.id.btn_startgame);
        btnHighScore = findViewById(R.id.btn_highscore);
        btnSettings = findViewById(R.id.btn_settings);
        btn1o1 = findViewById(R.id.btn_1o1);

        // Set animation for buttons
        StartSmartAnimation.startAnimation( findViewById(R.id.btn_startgame) , AnimationType.RotateIn, 2200 , 0 , true );
        StartSmartAnimation.startAnimation( findViewById(R.id.btn_highscore) , AnimationType.Tada, 2200 , 0 , true );
        StartSmartAnimation.startAnimation( findViewById(R.id.btn_settings) , AnimationType.Wave, 2200 , 0 , true );
        StartSmartAnimation.startAnimation( findViewById(R.id.btn_1o1) , AnimationType.Wobble, 2200 , 0 , true );
        StartSmartAnimation.startAnimation( findViewById(R.id.main_info) , AnimationType.SlideInUp, 2200 , 0 , true );


        btnStartGame.setOnClickListener(this);
        btn1o1.setOnClickListener(this);
        btnHighScore.setOnClickListener(this);
        btnSettings.setOnClickListener(this);

        // Checks whether the last time the user played background music
        if (readSetting("music").equals("true")) {
            Intent serviceIntent = new Intent(this, BackgroundMusicService.class);
            startService(serviceIntent);
        }

    }

    //Handle onClick events
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_startgame:
                startActivity(new Intent(this, LevelAndName.class));
                break;
            case R.id.btn_highscore:
                startActivity(new Intent(this, HighScore.class));

                break;
            case R.id.btn_settings:
                startActivity(new Intent(this, Settings.class));

                break;
            case R.id.btn_1o1:
                startActivity(new Intent(this, OneOnOne.class));

                break;
            default:
                break;
        }
    }

    // Set information button
    public void InfoClick(View view) {

        com.geniusforapp.fancydialog.FancyAlertDialog.Builder alert = new com.geniusforapp.fancydialog.FancyAlertDialog.Builder(MainMenu.this)
                .setimageResource(R.drawable.app_icon1)
                .setTextTitle(getString(R.string.app_name))

                .setBody(getString(R.string.info))
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



    @Override
    protected void onPause() {
        super.onPause();

        BackgroundMusicService.pause++;
        Intent serviceIntent = new Intent(MainMenu.this, BackgroundMusicService.class);
        startService(serviceIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        BackgroundMusicService.pause--;
        Intent serviceIntent = new Intent(MainMenu.this, BackgroundMusicService.class);
        startService(serviceIntent);
    }


}