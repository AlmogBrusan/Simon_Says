package com.example.simonsaysproject;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Game extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences settings;
    private List<Integer> sequence;
    private boolean isPlayerTurn;
    private int currentKey = 0;
    private int level;
    private int round;
    private int score;

    private Button btn_tl;
    private Button btn_tr;
    private Button btn_bl;
    private Button btn_br;

    private TextView game_status;
    private TextView game_score;
    private TextView game_level;
    private TextView game_round;
    private ImageView pic;
    private android.view.animation.Animation animShakeRound;

    private MediaPlayer sound1;
    private MediaPlayer sound2;
    private MediaPlayer sound3;
    private MediaPlayer sound4;
    private MediaPlayer failure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initView();
        startGame();
    }

    //Initialize all UI elements
    private void initView()
    {
        score = 0;
        round = 0;
        sequence = new ArrayList<>();
        settings = getSharedPreferences("PREFERENCES",MODE_PRIVATE);
        level = getIntent().getIntExtra("level",0); // get the level

        game_status = findViewById(R.id.game_status);
        game_score = findViewById(R.id.game_score);
        game_level = findViewById(R.id.game_level);
        game_level.setText(String.format(levelToSend()));
        game_round = findViewById(R.id.game_round);
        game_score.setText(String.format("%03d",score));

        btn_tl = findViewById(R.id.btn_tl);
        btn_tr = findViewById(R.id.btn_tr);
        btn_bl = findViewById(R.id.btn_bl);
        btn_br = findViewById(R.id.btn_br);

        pic = findViewById(R.id.game_picture);

        btn_tl.setOnClickListener(this);
        btn_tr.setOnClickListener(this);
        btn_bl.setOnClickListener(this);
        btn_br.setOnClickListener(this);

        sound1 = MediaPlayer.create(this, R.raw.sound1);
        sound2 = MediaPlayer.create(this, R.raw.sound2);
        sound3 = MediaPlayer.create(this, R.raw.sound3);
        sound4 = MediaPlayer.create(this, R.raw.sound4);
        failure = MediaPlayer.create(this, R.raw.failure);


    }

    @Override
    public void onClick(View v) // get click from user
    {
        if(isPlayerTurn)
        {
            switch (v.getId())
            {
                case R.id.btn_tl:
                    flashButton(btn_tl, getColor(R.color.tl_dark), getColor(R.color.tl_light));
                    compareSequence(0);
                    break;
                case R.id.btn_tr:
                    flashButton(btn_tr, getColor(R.color.tr_dark), getColor(R.color.tr_light));
                    compareSequence(1);
                    break;
                case R.id.btn_bl:
                    flashButton(btn_bl, getColor(R.color.bl_dark), getColor(R.color.bl_light));
                    compareSequence(2);
                    break;
                case R.id.btn_br:
                    flashButton(btn_br, getColor(R.color.br_dark), getColor(R.color.br_light));
                    compareSequence(3);
                    break;

                default:
                    break;
            }
        }
    }


    private void startGame()
    {
        game_status.setText(R.string.cpu_turn);
        pic.setImageResource(R.drawable.see);
        isPlayerTurn=false;

        //Introduce a delay before starting each round.
        Handler gameStarter = new Handler();
        gameStarter.postDelayed(new Runnable() {
            @Override
            public void run() {
                extendSequence();
            }
        },DelayBetweenRounds());
    }

    // End the game if the player is disqualified and asks him if he want keep the score
    private void endGame(){


        if(score != 0) {
            HighScoreManager hm = new HighScoreManager();
            hm.pushUserScore(readSetting("user"), score, levelToSend()); // enter the name, user score and level to FireBase Database
        }
        new FancyAlertDialog.Builder(Game.this)

                .setBackgroundColor(Color.parseColor("#D0A29898"))
                .setMessage(getString(R.string.save_score_ask1) + " " + score + " " + getString(R.string.save_score_ask3) )
                .setNegativeBtnText(getString(R.string.save_score_ask3_N))
                .setPositiveBtnBackground(Color.parseColor("#FA9308"))
                .setPositiveBtnText(getString(R.string.save_score_ask3_Y))
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))
                .setAnimation(Animation.POP)
                .isCancellable(false)
                .setIcon(R.drawable.score_save, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {

                        Intent intent = new Intent(Game.this, HighScore.class);
                        intent.putExtra("isInTable", score);
                        intent.putExtra("isPlayed", true);
                        startActivity(intent);
                        finish();
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Intent intent = new Intent(Game.this, LevelAndName.class);
                        startActivity(intent);
                        finish();


                    }
                })
                .build();



    }
    // sent the level String to FireBase Database

    private String levelToSend(){
        if (level == 0) {
            return  getString(R.string.easy);
        } else if(level == 1) {
            return  getString(R.string.medium);
        }
            return  getString(R.string.hard);
    }


    //Turns on and off the button's light
    private void flashButton(final Button btn, final int dark, int light)
    {
        switch (btn.getId()) // turn on the light
        {
            case R.id.btn_tl:
                btn_tl.setBackgroundResource(R.drawable.rounded_button1p);
                break;
            case R.id.btn_tr:
                btn_tr.setBackgroundResource(R.drawable.rounded_button2p);
                break;
            case R.id.btn_bl:
                btn_bl.setBackgroundResource(R.drawable.rounded_button3p);
                break;
            case R.id.btn_br:
                btn_br.setBackgroundResource(R.drawable.rounded_button4p);
                break;
        }

        if(readSetting("sound").equals("true")) // Check if the player turned on the sound
        {
            switch (btn.getId()) // Play the sound according to the selected button
            {
                case R.id.btn_tl:
                    playSound(R.raw.sound1);
                    break;
                case R.id.btn_tr:
                    playSound(R.raw.sound2);
                    break;
                case R.id.btn_bl:
                    playSound(R.raw.sound3);
                    break;
                case R.id.btn_br:
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
                    case R.id.btn_tl:
                        btn_tl.setBackgroundResource(R.drawable.rounded_button1);
                        break;
                    case R.id.btn_tr:
                        btn_tr.setBackgroundResource(R.drawable.rounded_button2);
                        break;
                    case R.id.btn_bl:
                        btn_bl.setBackgroundResource(R.drawable.rounded_button3);
                        break;
                    case R.id.btn_br:
                        btn_br.setBackgroundResource(R.drawable.rounded_button4);
                        break;
                }
            }
        }, FlashButtonDelay());
    }

    //Activate button based on key from CPU
    private void triggerButton(int i)
    {
        switch (i)
        {
            case 0:
                flashButton(btn_tl, getColor(R.color.tl_dark), getColor(R.color.tl_light));
                break;
            case 1:
                flashButton(btn_tr, getColor(R.color.tr_dark), getColor(R.color.tr_light));
                break;
            case 2:
                flashButton(btn_bl, getColor(R.color.bl_dark), getColor(R.color.bl_light));
                break;
            case 3:
                flashButton(btn_br, getColor(R.color.br_dark), getColor(R.color.br_light));
                break;

            default:
                break;
        }
    }

    //Adds one additional key to the sequence.
    private void extendSequence()
    {
        round++;
        game_round.setText(String.valueOf(round));
        animShakeRound = AnimationUtils.loadAnimation(this, R.anim.shake_round);
        game_round.startAnimation(animShakeRound);
        Random r = new Random();
        int key = r.nextInt(4);
        sequence.add(key);
        demoSequence(0);
    }

    //The sequence is played for the player
    private void demoSequence(final int i)
    {
        if(i<sequence.size())
        {
            triggerButton(sequence.get(i));

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    demoSequence(i+1);
                }
            }, DelayBetweenClicks());
        }

        else
        {
            game_status.setText(R.string.player_turn);
            pic.setImageResource(R.drawable.play);
            isPlayerTurn=true;
            currentKey=0;
        }
    }

    //Checks if the player clicks are according to the previous rhythm
    private void compareSequence(int i)
    {
        if(i==sequence.get(currentKey))
        {
            currentKey++;

        }

        else
        {
            if(readSetting("sound").equals("true"))
                failure.start();
            endGame();
        }

        if(currentKey>=sequence.size())
        {
            score();
            game_score.setText(String.format("%03d",score));
            startGame();
        }
    }

    // Determine the score according to the level
    private void score(){

        if (level == 0) {
            score += currentKey * currentKey * 100;
        } else if(level == 1) {
            score += currentKey * currentKey * 120;
        } else if (level == 2){
            score += currentKey * currentKey * 140;

        }
    }

    // Play the Sound
    private void playSound(int idOfSound) {
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

    //Read settings from Shared preference.
    private String readSetting(String key)
    {
        String value;
        value = settings.getString(key, "");
        return value;
    }

    private int DelayBetweenClicks(){
        if(level==0)
            return 800;
        else if(level==1)
            return 600;
        else return 400;
     } // Delay between clicks by level

    private int DelayBetweenRounds(){
        if(level==0)
            return 3000;
        else if(level==1)
            return 2200;
        else return 1500;
    } // Delay between rounds by level

    private int FlashButtonDelay(){
        if(level==0)
            return 400;
        else if(level==1)
            return 200;
        else return 100;
    } // Flash button delay by level

    @Override
    public void onBackPressed() {

        new FancyAlertDialog.Builder(Game.this)

                .setBackgroundColor(Color.parseColor("#D0A29898"))
                .setMessage(getString(R.string.out_game))
                .setNegativeBtnText(getString(R.string.no))
                .setPositiveBtnBackground(Color.parseColor("#FFA9A7A8"))
                .setPositiveBtnText(getString(R.string.yes))
                .setNegativeBtnBackground(Color.parseColor("#FA9308"))
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.stop, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Intent intent = new Intent(Game.this, LevelAndName.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {



                    }
                })
                .build();
    } // Activating a dialog if the player trying to get out in the middle of a played game

    @Override
    protected void onPause() {
        super.onPause();

        BackgroundMusicService.pause++;
        Intent serviceIntent = new Intent(Game.this, BackgroundMusicService.class);
        startService(serviceIntent);

    }

    @Override
    protected void onResume() {
        super.onResume();

        BackgroundMusicService.pause--;
        Intent serviceIntent = new Intent(Game.this, BackgroundMusicService.class);
        startService(serviceIntent);
    }

}
