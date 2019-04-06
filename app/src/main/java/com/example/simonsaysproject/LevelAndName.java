package com.example.simonsaysproject;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.shashank.sony.fancytoastlib.FancyToast;

public class LevelAndName extends AppCompatActivity {


    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private LinearLayout easy, medium, hard;
    private EditText userName;
    private  Animation animShakeEasy, animShakeMedium, animShakeHard;
    private ImageButton info;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_and_name);

    settings = getSharedPreferences("PREFERENCES",MODE_PRIVATE);
    editor = settings.edit();
    initView();

    }


      //Initialize all UI elements
    private void initView()
    {

        userName = findViewById(R.id.name_et);
        easy = findViewById(R.id.easy_bnt);
        medium = findViewById(R.id.medium_bnt);
        hard = findViewById(R.id.hard_bnt);
        info = findViewById(R.id.leve_and_name_info);

        userName.setText(readSetting("user"));

        animShakeEasy = AnimationUtils.loadAnimation(this, R.anim.shake);
        animShakeMedium = AnimationUtils.loadAnimation(this, R.anim.shake_m);
        animShakeHard = AnimationUtils.loadAnimation(this, R.anim.shake_h);
        easy.startAnimation(animShakeEasy);
        medium.startAnimation(animShakeMedium);
        hard.startAnimation(animShakeHard);

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editSetting("user",s.toString());
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                com.geniusforapp.fancydialog.FancyAlertDialog.Builder alert = new com.geniusforapp.fancydialog.FancyAlertDialog.Builder(LevelAndName.this)
                        .setimageResource(R.drawable.level_info)
                        .setTextTitle(getString(R.string.level_select))

                        .setBody(getString(R.string.levels_info))
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


    public void EasyLevel(View view) {

        if(!isNameEmpty())
        {
            Intent intent = new Intent(LevelAndName.this , Game.class);
            intent.putExtra("level",0);
            startActivity(intent);
            finish();
        }

    }

    public void MediumLevel(View view) {

        if(!isNameEmpty()) {
            Intent intent = new Intent(LevelAndName.this, Game.class);
            intent.putExtra("level", 1);
            startActivity(intent);
            finish();
        }
    }

    public void HardLevel(View view) {
        if(!isNameEmpty()) {
            Intent intent = new Intent(LevelAndName.this, Game.class);
            intent.putExtra("level", 2);
            startActivity(intent);
            finish();
        }
    }

    //Read setting
    private String readSetting(String key)
    {

        String value;
        value = settings.getString(key, "");
        return value;
    }

    //Edit setting
    private void editSetting(String key, String value)
    {
        editor.putString(key,value);
        editor.commit();
    }

    private boolean isNameEmpty(){ // Check if the user enter empty name

        if (readSetting("user").equals("") |readSetting("user").equals(" ")) {
            FancyToast.makeText(LevelAndName.this, getString(R.string.name_toast), FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();

            return true;
        }
        return false;

    }

    @Override
    protected void onPause() {
        super.onPause();

        BackgroundMusicService.pause++;
        Intent serviceIntent = new Intent(LevelAndName.this, BackgroundMusicService.class);
        startService(serviceIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        BackgroundMusicService.pause--;
        Intent serviceIntent = new Intent(LevelAndName.this, BackgroundMusicService.class);
        startService(serviceIntent);
    }


}
