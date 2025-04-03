package com.farhan.nyanclock;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawableTransformation;

import java.util.Random;

public class MainActivity extends Activity {
    ImageButton switch_button;

    Runnable timerRunnable;
    ImageView nyan_gif;
    MediaPlayer audio_player;
    String timeFormatted;
    TextView heading;
    String heading_text;


    private final Handler timerHandler = new Handler();
    private int seconds = 0;
    Boolean isRunning;
    TextView time;
    private final Handler volumeHandler = new Handler();

    private float star_opacity = 1.0f;

    private Runnable volumeRunnable;
    private AudioManager audioManager;

    private ImageButton star_button;
    private final Random random = new Random();

    private FrameLayout gameContainer;
    private TextView starscount;
    private String stars_count;

    private Handler star_remover_handler = new Handler();

    private int num_stars=0;
    private ImageButton helper;


    private ImageButton connector;
    private ImageView heart1;
    private ImageView heart2;
    private ImageView heart3;

    private int hearts_count;

    private ImageView ufo;
    private int startX, endX;

    private Handler ufoHandler = new Handler();

    private int ufo_speed;

    public void onClickConnect(View view){
        Intent connect = new Intent(this,Connect.class);
        startActivity(connect);
    }

    private void moveUFO() {
        if (ufo == null) {
            ufo = findViewById(R.id.ufo_view); // Initialize if not set
        }

        // Random Y position to move across the screen
        Random random = new Random();
        int screenHeight = gameContainer.getHeight(); // Parent layout height
        int randomY = random.nextInt(screenHeight - ufo.getHeight());

        // Place UFO at the start (left side)
        int X = random.nextBoolean()?1:-1;
        if(X==1){
//            ufo.setX(-200);
            startX = -200;
            endX = gameContainer.getWidth()+200;
        }
        else{
            startX = gameContainer.getWidth()+200;
            endX = -200;
        }
        ufo.setX(startX);
         // Start off-screen
        ufo.setY(randomY);
        ufo.setVisibility(View.VISIBLE);

        ufo_speed = random.nextInt(401)+600;

        // Animate UFO from left to right
        ObjectAnimator animator = ObjectAnimator.ofFloat(ufo, "translationX", endX);
        animator.setDuration(ufo_speed); // UFO moves across in 1 second
        animator.setInterpolator(new LinearInterpolator()); // Keeps speed constant
        animator.start();

        // Hide the UFO after animation completes
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ufo.setVisibility(View.INVISIBLE);
            }
        });
    }


    private void spawnUFO() {
        if(!isRunning){return;}
        Random random = new Random();
        int delay = random.nextInt(5000) + 3000; // Spawn every 3-8 seconds

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isRunning){
                moveUFO();
                spawnUFO(); // Schedule next UFO spawn
                }
            }
        }, delay);
        }



    private void onClickUfo(View view){
        if(isRunning){
        ufo.animate().cancel();
        changeTimerColourGreen();
        seconds+=250;
        ufo.setVisibility(View.INVISIBLE);
        }
    }



    public void changeTimerColour(){
        Handler colourHandler = new Handler();
        colourHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                time.setTextColor(Color.RED);
            }
        },100);

      }

    public void changeTimerColourGreen(){
        Handler colourHandlerGreen = new Handler();
        colourHandlerGreen.postDelayed(new Runnable() {
            @Override
            public void run() {
                time.setTextColor(Color.GREEN);
            }
        },100);

    }





private void startVolumeMonitor() {
    audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

    volumeRunnable = new Runnable() {
        @Override
        public void run() {

            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume < 4) {
                // Boost volume if it goes below threshold
                int a2 = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                final int safeVolume = Math.min(a2, 10);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, safeVolume, AudioManager.FLAG_SHOW_UI);
                Toast.makeText(MainActivity.this, "Too smart?", Toast.LENGTH_SHORT).show();
            }

            // Check again after 2 seconds
            volumeHandler.postDelayed(this, 2000);
        }

    };

    // Start monitoring
    volumeHandler.post(volumeRunnable);
}






    private void spawnNewStar() {
        if (!isRunning) return; // Don't spawn if game is over

        star_opacity = 1.0f;
        star_button.setAlpha(1.0f);

        int x = random.nextInt(gameContainer.getWidth() - 150);
        int y = random.nextInt(gameContainer.getHeight() - 150);

        star_button.setX(x);
        star_button.setY(y);
        star_button.bringToFront();
        star_button.setVisibility(View.VISIBLE);

        star_remover(); // Start fading again
    }

    // Function to Remove Star
    public void star_remover() {
        star_remover_handler.removeCallbacksAndMessages(null); // Clear previous tasks

        star_remover_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isRunning){
                if (star_opacity > 0.1f) {
                    star_opacity -= 0.1f;
                    star_button.setAlpha(star_opacity);
                    star_remover_handler.postDelayed(this, 200);
                } else {
                    changeTimerColour();
                    star_button.setVisibility(View.INVISIBLE);
                    if(hearts_count==3){
                        heart3.setVisibility(View.INVISIBLE);
                        hearts_count--;
                        spawnNewStar();
                    }
                    else if(hearts_count==2){
                        heart2.setVisibility(View.INVISIBLE);
                        hearts_count--;
                        spawnNewStar();
                    }
                    else{
                        heart1.setVisibility(View.INVISIBLE);
                        Intent game_ender = new Intent(MainActivity.this, LostGame.class);
                        audio_player.pause();
                        startActivity(game_ender);
                        game_ender.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                    }

                }
            }
            }
        }, 200);
    }


public void onClickHelp(View view){
    Intent help_screen = new Intent(this,help.class);
    startActivity(help_screen);

}


    public void onClickStar(View view){
        if(isRunning){spawnNewStar();}
        num_stars++;
        stars_count  = String.format("⭐ %d",num_stars);
        starscount.setText(stars_count);
    }

    public void onClickButton(View view){
        //check_volume();
        if(!isRunning){

            connector.setVisibility(View.INVISIBLE);
            helper.setVisibility(View.INVISIBLE);
            star_button.setVisibility(View.VISIBLE);
            starscount.setVisibility(View.VISIBLE);
            heading.setVisibility(View.INVISIBLE);
            time.setTextColor(Color.WHITE);
            audio_player.start();
            audio_player.setLooping(true);
            isRunning = true;
            switch_button.setImageResource(R.drawable.stop);
            timerHandler.post(timerRunnable);
            spawnUFO();
            if (nyan_gif.getDrawable() instanceof GifDrawable) {
                ((GifDrawable) nyan_gif.getDrawable()).start();
            }
        }
        else if(isRunning){

            helper.setVisibility(View.VISIBLE);
            connector.setVisibility(View.VISIBLE);
            starscount.setVisibility(View.INVISIBLE);
            star_button.setVisibility(View.INVISIBLE);
            heading_text = String.format("%d SECONDS REMAINING!!",(6389-seconds));
            heading.setText(heading_text);
            heading.setVisibility(View.VISIBLE);
            audio_player.pause();
            isRunning = false;
            switch_button.setImageResource(R.drawable.start);
            if (nyan_gif.getDrawable() instanceof GifDrawable) {
                ((GifDrawable) nyan_gif.getDrawable()).stop();
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        startVolumeMonitor();


        setContentView(R.layout.activity_main);
        ufo = findViewById(R.id.ufo_view);
//        spawnUFO();


        helper = findViewById(R.id.help_button);
        gameContainer = findViewById(R.id.container);
        star_button = findViewById(R.id.star_button);
        starscount = findViewById(R.id.stars_count);
        star_button.bringToFront();
        audio_player = MediaPlayer.create(this,R.raw.original);
        switch_button = findViewById(R.id.button);
        nyan_gif = findViewById(R.id.nyanCat);
        time = findViewById(R.id.timetext);
        heading = findViewById(R.id.heading);
        connector = findViewById(R.id.connect_button);

        heart1 = findViewById(R.id.heart1);
        heart2 = findViewById(R.id.heart2);
        heart3 = findViewById(R.id.heart3);
        hearts_count = 3;

        ufo.setOnClickListener(this::onClickUfo);


        Glide.with(this)
                .asGif()
                .load(R.drawable.tenor2)
                .override(1100, 1100) // Resize for better performance
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                //.transition(DrawableTransitionOptions.withCrossFade()) // Smooth transition
                .into(nyan_gif);
        isRunning = false;

        timerRunnable = new Runnable(){
            @Override
            public void run(){
                if(isRunning){
                    seconds+=1;
                    time.setTextColor(Color.WHITE);
                    if(seconds>=6389){
                        isRunning = false;
                        game_finisher();
                        seconds = 0;
                        audio_player.pause();
                    }
                    if(seconds<10){
                        timeFormatted = String.format("%d",seconds);
                    }

                    else{
                        timeFormatted = String.format("%02d",seconds);
                    }
                    time.setText(timeFormatted);
                    time.postDelayed(this, 1000);
                }

            }
        };
    }

    public void game_finisher(){
        Intent finishgame = new Intent(this,FinishGame.class);
        finishgame.putExtra("stars",num_stars);
        startActivity(finishgame);
        finishgame.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
    }



    public void onWrongClick(View view){
        if(isRunning){

            changeTimerColour();
            seconds = seconds/2;
            if(seconds>0){
                seconds -= 1;
            }

        }

    }



}