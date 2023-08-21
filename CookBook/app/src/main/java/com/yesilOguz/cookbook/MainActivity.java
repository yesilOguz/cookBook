package com.yesilOguz.cookbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    // you need to change this

    // You can get this key from
    // https://getpantry.cloud
    // signup and it will be give to you your api key
    public static final String pantryId = "YOUR-API-KEY";

    // You can get this key from
    // https://imgbb.com/signup
    // signup and it will be give to you your api key
    public static final String imgbbKey = "YOUR-API-KEY";
    // you need to change this

    public static String receivedJson;
    private String baseUrl = "https://getpantry.cloud/apiv1/pantry/" + pantryId + "/basket/recipes";
    String jsonStr;
    recipeDatas[] datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        ImageView addImButt = findViewById(R.id.addCook);
        LinearLayout content = findViewById(R.id.mainContent);
        addImButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNewPage.class);
                startActivity(intent);
            }
        });

        Context cntxt = getApplicationContext();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    jsonStr = getResponseText(baseUrl);
                    receivedJson = jsonStr;
                }
                catch (Exception e){}

                JSONArray recipes = null;

                try {
                    recipes = new JSONObject(jsonStr).getJSONArray("Recipes");

                    datas = new recipeDatas[recipes.length()];

                    for (int i=0; i < recipes.length(); i++) {
                        JSONObject recipe = new JSONObject(recipes.get(i).toString());

                        recipeDatas data = new recipeDatas();

                        data.recipeTitle = recipe.getString("Title");
                        data.shortDesc = recipe.getString("ShortDec");
                        data.mealBitmap = getBitmap(recipe.getString("ImageUrl"));
                        data.includes = recipe.getString("Includes");
                        data.howToMake = recipe.getString("How To Make");

                        datas[i] = data;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (recipeDatas data: datas) {
                            card recipeCard = new card(data.recipeTitle,
                                    data.shortDesc, data.mealBitmap, content, cntxt);

                            setListenerOnCardView(recipeCard.card, data.recipeTitle,
                                    data.shortDesc, data.mealBitmap, data.includes, data.howToMake);
                        }
                    }
                });
            }
        }).start();
    }

    void setListenerOnCardView(CardView card, String title, String desc, Bitmap img, String incs, String howToMake){

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeDatas data = new recipeDatas();
                data.recipeTitle = title;
                data.mealBitmap = img;
                data.shortDesc = desc;
                data.includes = incs;
                data.howToMake = howToMake;

                recipePage.datas = data;

                Intent intent = new Intent(MainActivity.this, recipePage.class);
                startActivity(intent);
            }
        });

    }

    private String getResponseText(String stringUrl) throws IOException
    {
        StringBuilder response  = new StringBuilder();

        URL url = new URL(stringUrl);
        HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
        if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()),8192);
            String strLine = null;
            while ((strLine = input.readLine()) != null)
            {
                response.append(strLine);
            }
            input.close();
        }
        return response.toString();
    }

    Bitmap getBitmap(String siteUrl) {
        try {
            URL url = new URL(siteUrl);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}