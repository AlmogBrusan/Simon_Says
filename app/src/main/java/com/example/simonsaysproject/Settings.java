package com.example.simonsaysproject;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.suke.widget.SwitchButton;


public class Settings extends AppCompatActivity {

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private com.suke.widget.SwitchButton setting_sfx , setting_music;
    private ImageView info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = getSharedPreferences("PREFERENCES",MODE_PRIVATE);
        editor = settings.edit();
        initView();
    }

    //Initialize all UI elements
    public void initView()
    {

        setting_sfx = findViewById(R.id.setting_sfx);
        setting_music = findViewById(R.id.setting_music);


        // Read and set sound last choice from Shared Preferences
        if(readSetting("sound").equals("true"))
        {
            setting_sfx.setChecked(true);
        }
        else
        {
            setting_sfx.setChecked(false);
        }


        // Read and set background music last choice from Shared Preferences
        if(readSetting("music").equals("true"))
        {
            setting_music.setChecked(true);
        }
        else
        {
            setting_music.setChecked(false);
        }


        // Listener that turn on and off  background music using Service
         setting_music.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {

                if(isChecked)
                {
                    editSetting("music", "true");
                    Intent serviceIntent = new Intent(Settings.this, BackgroundMusicService.class);
                    startService(serviceIntent);
                }
                else
                {
                    editSetting("music", "false");
                    Intent serviceIntent = new Intent(Settings.this, BackgroundMusicService.class);
                    stopService(serviceIntent);
                }
            }
        });


        // Listener that turn on and off  sound using Service
        setting_sfx.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked)
                {
                    editSetting("sound", "true");
                }
                else
                {
                    editSetting("sound", "false");
                }
            }

        });

        info = findViewById(R.id.setting_info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                com.geniusforapp.fancydialog.FancyAlertDialog.Builder alert = new com.geniusforapp.fancydialog.FancyAlertDialog.Builder(Settings.this)
                        .setimageResource(R.drawable.sound_info)
                        .setTextTitle(getString(R.string.settings))
                        .setBody(getString(R.string.settings_info))
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

    //Read setting
    public String readSetting(String key)
    {
        String value;
        value = settings.getString(key, "");
        return value;
    }

    //Edit setting
    public void editSetting(String key, String value)
    {
        editor.putString(key,value);
        editor.commit();
    }


    @Override
    protected void onPause() {
        super.onPause();

        BackgroundMusicService.pause++;
        Intent serviceIntent = new Intent(Settings.this, BackgroundMusicService.class);
        startService(serviceIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        BackgroundMusicService.pause--;
       Intent serviceIntent = new Intent(Settings.this, BackgroundMusicService.class);
        startService(serviceIntent);
    }


}



