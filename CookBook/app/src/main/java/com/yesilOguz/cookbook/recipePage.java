package com.yesilOguz.cookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;

public class recipePage extends AppCompatActivity {

    public static recipeDatas datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_recipe_page);

        String incStr = datas.includes;
        String[] incArr = incStr.split(",");

        String includes = "";

        for (String inc : incArr) {
            includes += "• " + inc + "\n";
        }

        ImageView backButt = findViewById(R.id.backButtonImage);
        TextView mealText = findViewById(R.id.mealName);
        ImageView mealPic = findViewById(R.id.mealPic);
        TextView shortDescText = findViewById(R.id.shortDescText);
        TextView includestText = findViewById(R.id.incText);
        TextView howToMakeText = findViewById(R.id.howToMakeText);

        mealText.setText(datas.recipeTitle);

        mealPic.setImageBitmap(datas.mealBitmap);

        shortDescText.setText(datas.shortDesc);

        includestText.setText(includes);

        howToMakeText.setText(datas.howToMake);

        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(recipePage.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}