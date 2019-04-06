package com.example.simonsaysproject;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.github.jinatonic.confetti.CommonConfetti;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.view.View.VISIBLE;

public class OneOnOne extends AppCompatActivity {

    private int index = 0;
    ArrayList<Integer> buttonArray;
    private  ProgressBar progressBarPlayer1 ;
    private  ProgressBar progressBarPlayer2 ;
    private final HashMap<String, String> hashMap = new HashMap<>();
    private SharedPreferences sound;
    private TextView red_button , yellow_button ,blue_button, green_button ;
    private LinearLayout step;
    private ImageButton info;
    private RelativeLayout container;
    private android.view.animation.Animation animShakeStep;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_on_one);

        initView();
    }

    //Initialize all UI elements
    private void initView() {

        sound = getSharedPreferences("PREFERENCES",MODE_PRIVATE);


        Sprite wave = new Wave();
        progressBarPlayer1 = findViewById(R.id.my_progress_bar_1);
        progressBarPlayer1.setIndeterminateDrawable(wave);
        Sprite wave2 = new Wave();
        progressBarPlayer2 = findViewById(R.id.my_progress_bar_2);
        progressBarPlayer2.setIndeterminateDrawable(wave2);


        red_button = findViewById(R.id.red_button);
        yellow_button = findViewById(R.id.yellow_button);
        blue_button = findViewById(R.id.blue_button);
        green_button = findViewById(R.id.green_button);
        step = findViewById(R.id.add_step_l);

        container = findViewById(R.id.container);

        info = findViewById(R.id.one_one_one_info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.geniusforapp.fancydialog.FancyAlertDialog.Builder alert = new com.geniusforapp.fancydialog.FancyAlertDialog.Builder(OneOnOne.this)
                        .setimageResource(R.drawable.one_on_one_info)
                        .setTextTitle(getString(R.string.how_play))
                        .setBody(getString(R.string.one_on_one_info))
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


        if (buttonArray == null) {
            buttonArray = new ArrayList<>();
            progressBarPlayer1.setVisibility(VISIBLE);
            progressBarPlayer2.setVisibility(View.GONE);
        }
    }


    public void onClickButton(@NonNull View view) {
        try {
            doOnClickButton(view);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Turns on and off the button's light
    private void flashButton(final TextView btn, final int dark, int light) {
        switch (btn.getId()) {
            case R.id.red_button:
                red_button.setBackgroundResource(R.drawable.rounded_button1p);
                break;
            case R.id.blue_button:
                blue_button.setBackgroundResource(R.drawable.rounded_button2p);
                break;
            case R.id.yellow_button:
                yellow_button.setBackgroundResource(R.drawable.rounded_button3p);
                break;
            case R.id.green_button:
                green_button.setBackgroundResource(R.drawable.rounded_button4p);
                break;
        }

        if(readSetting("sound").equals("true")) // Check if the player turned on the sound
        {
            switch (btn.getId()) // Play the sound according to the selected button
            {
                case R.id.red_button:
                    playSound(R.raw.sound1);
                    break;
                case R.id.blue_button:
                    playSound(R.raw.sound2);
                    break;
                case R.id.yellow_button:
                    playSound(R.raw.sound3);
                    break;
                case R.id.green_button:
                    playSound(R.raw.sound4);
                    break;
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                switch (btn.getId()) // turn off the light after delay
                {
                    case R.id.red_button:
                        red_button.setBackgroundResource(R.drawable.rounded_button1);
                        break;
                    case R.id.blue_button:
                        blue_button.setBackgroundResource(R.drawable.rounded_button2);
                        break;
                    case R.id.yellow_button:
                        yellow_button.setBackgroundResource(R.drawable.rounded_button3);
                        break;
                    case R.id.green_button:
                        green_button.setBackgroundResource(R.drawable.rounded_button4);
                        break;
                }
            }
        }, 100);
    }


    private void doOnClickButton(View view) {


        String color = null;

        switch (view.getId()) { //Activate button based on key from users
            case R.id.green_button:
                flashButton(green_button, getColor(R.color.br_dark), getColor(R.color.br_light));
                color = "Green";
                break;
            case R.id.red_button:
                flashButton(red_button, getColor(R.color.tl_dark), getColor(R.color.tl_light));
                color = "Red";
                break;
            case R.id.yellow_button:
                flashButton(yellow_button, getColor(R.color.bl_dark), getColor(R.color.bl_light));
                color = "Yellow";
                break;
            case R.id.blue_button:
                flashButton(blue_button, getColor(R.color.tr_dark), getColor(R.color.tr_light));
                color = "Blue";
                break;

        }

        if (buttonArray.size() - index == 1) { // The player managed to repeat his friend's rhythm
            changeVisible();
        }

        if (buttonArray.size() == index) { // The player adds another step to the previous rhythm

            hashMap.put(String.valueOf(index + 1), color);

            changeVisible();
            index = 0;
            buttonArray.add(view.getId());

            changeStatusOfProgressBar();


        } else {

            if (buttonArray.get(index) != view.getId()) { // The player  failed and could not repeat his friend's rhythm

                makeAlertMessage(hashMap);

            } else {
                index++;

            }
        }
    }

    void changeVisible(){
            if (step.getVisibility() == VISIBLE) {
                step.setVisibility(View.INVISIBLE);
            } else {
                step.setVisibility(VISIBLE);
                animShakeStep = AnimationUtils.loadAnimation(this, R.anim.shake_round);
                step.startAnimation(animShakeStep);
            }
    }

    private void changeStatusOfProgressBar() {
        if (progressBarPlayer1.getVisibility() == VISIBLE) {
            progressBarPlayer1.setVisibility(View.GONE);
            progressBarPlayer2.setVisibility(VISIBLE);
        } else {
            progressBarPlayer1.setVisibility(VISIBLE);
            progressBarPlayer2.setVisibility(View.GONE);
        }
    }

    // Victory dialog showing the winning player and starts konffeti
    private void makeAlertMessage(final Map<String, String> hashMap) {

        CommonConfetti.rainingConfetti(container, new int[] { Color.YELLOW ,Color.RED ,Color.BLUE ,Color.GREEN  })
                .infinite();
        playSound(R.raw.winning);

        final String winner = (progressBarPlayer1.getVisibility() != VISIBLE) ?
                getString(R.string.player_1) : getString(R.string.player_2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new FancyAlertDialog.Builder(OneOnOne.this)

                        .setBackgroundColor(Color.parseColor("#3649d4"))
                        .setMessage(getString(R.string.is_winner) +winner + getString(R.string.save_score_ask3) )
                        .setNegativeBtnText(getString(R.string.finish))
                        .setPositiveBtnBackground(Color.parseColor("#FA9308"))
                        .setPositiveBtnText(getString(R.string.replay))
                        .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))
                        .setAnimation(Animation.POP)
                        .isCancellable(false)
                        .setIcon(R.drawable.win_icon, Icon.Visible)
                        .OnPositiveClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {

                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        })
                        .OnNegativeClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {
                                finish();


                            }
                        })
                        .build();
            }
        }, 2000);


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

    private String readSetting(String key){
        String value;
        value = sound.getString(key, "");
        return value;
    }

    @Override
    protected void onPause() {
        super.onPause();

        BackgroundMusicService.pause++;
        Intent serviceIntent = new Intent(OneOnOne.this, BackgroundMusicService.class);
        startService(serviceIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        BackgroundMusicService.pause--;
        Intent serviceIntent = new Intent(OneOnOne.this, BackgroundMusicService.class);
        startService(serviceIntent);
    }
}
