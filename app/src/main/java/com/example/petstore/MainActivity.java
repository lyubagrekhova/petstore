package com.example.petstore;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private TextView mTextView2;
    private EditText editText;

    private ImageView image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);
        mTextView2 = (TextView) findViewById(R.id.textView2);
        editText = (EditText) findViewById(R.id.editText);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        image = (ImageView) findViewById(R.id.imageView2);
        mProgressBar.setVisibility(View.INVISIBLE);


    }
    public void onClick(View view) {
        mProgressBar.setVisibility(View.VISIBLE);


        PetService petService = PetService.retrofit.create(PetService.class);
        final Call<Pet> call =
                petService.getId(Integer.parseInt(editText.getText().toString()));


        call.enqueue(new Callback<Pet>() {
            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {
                // response.isSuccessfull() is true if the response code is 2xx
                if (response.isSuccessful()) {
                    Pet user = response.body();

                    // Получаем json из github-сервера и конвертируем его в удобный вид
                    mTextView2.setText("ID: " + user.getId() +
                            "\nИмя: "+user.getName()+"\nСтатус: " + user.getStatus());
                    mProgressBar.setVisibility(View.INVISIBLE);

                    Picasso.get().load(user.getPhotoUrls().get(0).toString()).into(image);


                } else {
                    int statusCode = response.code();

                    // handle request errors yourself
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mTextView.setText(errorBody.string());
                        mProgressBar.setVisibility(View.INVISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }






            @Override
            public void onFailure(Call<Pet> call, Throwable throwable) {
                mTextView.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });



    }

}