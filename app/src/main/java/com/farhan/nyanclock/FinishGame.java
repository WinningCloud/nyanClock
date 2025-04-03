package com.farhan.nyanclock;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class FinishGame extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finishgame);

        ImageView finishgif = findViewById(R.id.finishgif);

        Glide.with(this)
                .asGif()
                .load(R.drawable.goodgame)
                .override(1100, 1100) // Resize for better performance
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                //.transition(DrawableTransitionOptions.withCrossFade()) // Smooth transition
                .into(finishgif);

        int stars_num = getIntent().getIntExtra("stars",0);
        String score_text = String.format("Your score: %d⭐",stars_num);
        TextView score = findViewById(R.id.score);
        score.setText(score_text);
}}