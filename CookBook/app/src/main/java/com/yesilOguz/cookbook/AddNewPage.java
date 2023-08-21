package com.yesilOguz.cookbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AddNewPage extends AppCompatActivity {

    // dont change these
    final String imagebbApiUrl = "https://api.imgbb.com/1/upload";

    final String pantryUpdateUrl = "https://getpantry.cloud/apiv1/pantry/" + MainActivity.pantryId + "/basket/recipes";

    // dont change these

    Button selectImgButt;
    Button saveButt;

    int SELECT_PICTURE = 200;

    Bitmap selectedImg;

    String titleCook;
    String descCook;
    String includesCook;
    String howToCook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //saveButt.setClickable(true);

        // For fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_add_new_page);

        ImageView backButt = findViewById(R.id.backButtonImage);
        selectImgButt = findViewById(R.id.selectImgButt);
        saveButt = findViewById(R.id.saveButt);

        EditText titleInp = findViewById(R.id.titleInp);
        EditText shortDecInp = findViewById(R.id.shortDescInp);
        EditText incInp = findViewById(R.id.includesInp);
        EditText howToMakeInp = findViewById(R.id.howToMakeInp);

        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddNewPage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        selectImgButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        saveButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleCook = titleInp.getText().toString();
                descCook = shortDecInp.getText().toString();
                includesCook = incInp.getText().toString();
                howToCook = howToMakeInp.getText().toString();

                if (selectedImg == null || titleCook.isEmpty() ||
                        includesCook.isEmpty() || howToCook.isEmpty()) {

                    Toast.makeText(AddNewPage.this, "You have to fill in all the blanks and choose a picture!",
                            Toast.LENGTH_LONG).show();

                    return;
                }

                saveIt();
            }
        });
    }

    void saveIt() {

        saveButt.setClickable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // for upload the image
                    URL uploadPhotoUrl = new URL(imagebbApiUrl + "?key=" + MainActivity.imgbbKey);
                    HttpURLConnection uploadPhotoConn = (HttpURLConnection) uploadPhotoUrl.openConnection();

                    // for upload the json datas
                    URL updateRecipes = new URL(pantryUpdateUrl);
                    HttpURLConnection recipesConn = (HttpURLConnection)updateRecipes.openConnection();

                    uploadPhotoConn.setRequestMethod("POST");
                    uploadPhotoConn.setDoOutput(true);

                    recipesConn.setRequestMethod("POST");
                    recipesConn.setRequestProperty("Content-Type", "application/json");
                    recipesConn.setDoOutput(true);

                    String postData = "image=" + Uri.encode(Base64.encodeToString(bitmapToByteArray(selectedImg), Base64.DEFAULT));
                    byte[] postByteData = postData.getBytes(StandardCharsets.UTF_8);

                    try (DataOutputStream outputStream = new DataOutputStream(uploadPhotoConn.getOutputStream())) {
                        outputStream.write(postByteData);
                    }

                    int responseCode = uploadPhotoConn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStreamReader inputStreamReader = new InputStreamReader(uploadPhotoConn.getInputStream());
                        StringBuilder response = new StringBuilder();
                        int data = inputStreamReader.read();
                        while (data != -1) {
                            response.append((char) data);
                            data = inputStreamReader.read();
                        }

                        inputStreamReader.close();

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        JSONObject responseData = jsonResponse.getJSONObject("data");

                        String uploadedUrl = responseData.getString("url");

                        String[] enters = howToCook.split("\\n");

                        String recipe = "";

                        for (String enter: enters) {

                            recipe += enter + "\\n";

                        }

                        String willAddJson = "{\n\t\"Title\": \"" + titleCook + "\"," +
                                "\n\t\"ShortDec\": \"" + descCook + "\", " +
                                "\n\t\"ImageUrl\": \"" + uploadedUrl + "\", " +
                                "\n\t\"Includes\": \"" + includesCook + "\", " +
                                "\n\t\"How To Make\": \"" + recipe + "\"\n}";

                        int receivedLength = MainActivity.receivedJson.length();

                        String preItems = MainActivity.receivedJson.substring(0, receivedLength-2);
                        String lastItems = MainActivity.receivedJson.substring(receivedLength-2, receivedLength);

                        String replaceIt = preItems + "," + willAddJson + lastItems;

                        try (OutputStream os = recipesConn.getOutputStream()) {
                            byte[] input = replaceIt.getBytes(StandardCharsets.UTF_8);
                            os.write(input, 0, input.length);
                        }

                        responseCode = recipesConn.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            Toast.makeText(AddNewPage.this, "Successfully uploaded", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddNewPage.this, "Error while data uploading!!", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(AddNewPage.this, "Error while photo uploading!!", Toast.LENGTH_LONG).show();
                    }

                    uploadPhotoConn.disconnect();
                    recipesConn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(AddNewPage.this, MainActivity.class);
                startActivity(i);
            }
        }).start();

    }

    void imageChooser() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PICTURE);
    }

    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                selectedImg = selectedImage;

                Toast.makeText(AddNewPage.this, "successfully selected", Toast.LENGTH_LONG).show();

                selectImgButt.setText("Image selected");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(AddNewPage.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(AddNewPage.this, "You haven't picked an image", Toast.LENGTH_LONG).show();
        }
    }
}
