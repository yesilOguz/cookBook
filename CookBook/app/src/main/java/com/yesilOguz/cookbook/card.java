package com.yesilOguz.cookbook;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

public class card {


    public CardView card;

    String cardTitle;
    String cardDesc;
    Bitmap cardBitmap;
    LinearLayout lnLayout;
    Context context;

    public card(String _cardTitle, String _cardDesc, Bitmap _cardBitmap, LinearLayout _lnLayout, Context _context){

        cardTitle = _cardTitle;
        cardDesc = _cardDesc;
        cardBitmap = _cardBitmap;
        lnLayout = _lnLayout;
        context = _context;

        createCardInLayout();
    }

    void createCardInLayout() {
        card = new CardView(context);
        LinearLayout layout = new LinearLayout(context);
        TextView titleText = new TextView(context);
        TextView descText = new TextView(context);
        ImageView cardImage = new ImageView(context);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(80, 0, 80, 0);

        card.setLayoutParams(layoutParams);
        card.setCardBackgroundColor(Color.WHITE);
        card.setCardElevation(30f);
        card.setMaxCardElevation(36f);
        card.setRadius(60f);
        card.setPreventCornerOverlap(true);
        card.setUseCompatPadding(true);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);

        layout.setPadding(0, 25, 0, 100);

        cardImage.setLayoutParams(new LinearLayout.LayoutParams(
                600,
                600
        ));
        cardImage.setImageBitmap(cardBitmap);

        SpannableString boldTitle = new SpannableString(cardTitle);
        boldTitle.setSpan(new StyleSpan(Typeface.BOLD), 0, cardTitle.length(), 0);

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        titleText.setLayoutParams(textParams);
        titleText.setText(boldTitle);
        titleText.setTextColor(Color.parseColor("#040404"));
        titleText.setTextSize(20f);

        descText.setText(cardDesc);
        descText.setPadding(50, 0, 0, 0);
        descText.setTextColor(Color.parseColor("#040404"));
        descText.setTextSize(20f);

        layout.addView(cardImage);
        layout.addView(titleText);
        layout.addView(descText);

        card.addView(layout);
        lnLayout.addView(card);
    }

}
