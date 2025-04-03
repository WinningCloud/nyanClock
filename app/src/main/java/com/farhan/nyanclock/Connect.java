package com.farhan.nyanclock;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Connect extends Activity {

    public LinearLayout instabtn;
    public LinearLayout githubbtn;

    public void onClickInsta(View view){
        String insta_link = "https://www.instagram.com/farhann_6389/";
        Intent insta_opener = new Intent(Intent.ACTION_VIEW, Uri.parse(insta_link));
        insta_opener.setPackage("com.instagram.android");

        if (insta_opener.resolveActivity(getPackageManager()) != null) {
            startActivity(insta_opener);
        } else {
            // Open in browser if Instagram is not installed
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(insta_link)));
        }
    }

    public void onClickGit(View view){
        String github_link = "https://github.com/WinningCloud";
        Intent github_opener = new Intent(Intent.ACTION_VIEW,Uri.parse(github_link));
        startActivity(Intent.createChooser(github_opener, "Open with"));

    }

    public void onClickLin(View view){
        String linkedin_link = "https://www.linkedin.com/in/farhan6389";
        Intent linkedin = new Intent(Intent.ACTION_VIEW,Uri.parse(linkedin_link));
        linkedin.setPackage("com.linkedin.android");
        if (linkedin.resolveActivity(getPackageManager()) != null) {
            startActivity(linkedin);
        } else {
            // Open in browser if Instagram is not installed
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(linkedin_link)));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);


    }
}
