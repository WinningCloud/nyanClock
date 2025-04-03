package com.farhan.nyanclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class LostGame extends Activity {
    private ImageView endgame_gif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_game);


        endgame_gif = findViewById(R.id.endgame_gif_view);
        Glide.with(this)
                .asGif()
                .load(R.drawable.gameover)
                .override(1100, 1100) // Resize for better performance
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                //.transition(DrawableTransitionOptions.withCrossFade()) // Smooth transition
                .into(endgame_gif);

    }

    public void onClickRestart(View view){
        Intent restart = new Intent(this,MainActivity.class);
        startActivity(restart);

    }
}