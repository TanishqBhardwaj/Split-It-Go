package com.example.retrofitexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;
    private String API_KEY = "b8f745c2d43033fd65ce3af63180c3c3";
    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://eae4e227.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getPostMovies();
        createPost();
    }

    public void getPostMovies() {
//        Call<PostMovie> call = jsonPlaceHolderApi.getPostMovies(3,"movie",
//                "popular", API_KEY);
//        call.enqueue(new Callback<PostMovie>() {
//            @Override
//            public void onResponse(Call<PostMovie> call, Response<PostMovie> response) {
//                if(!response.isSuccessful()) {
//                    textViewResult.setText("Code: " + response.code());
//                    return;
//                }
//
//                PostMovie posts = response.body();
//                List<PostMovie.PostMovies> postMoviesList = posts.getResults();
//                for(PostMovie.PostMovies post: postMoviesList) {
//                    String content = "";
//
//                    content += "Popularity: " + post.getPopularity() + "\n";
//                    content += "Title: " + post.getTitle() + "\n";
//                    content += "Release-Date: " + post.getDate() + "\n\n";
//
//                    textViewResult.append(content);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PostMovie> call, Throwable t) {
//                textViewResult.setText(t.getMessage());
//            }
//        });


        Call<PostMovie> call = jsonPlaceHolderApi.getPostMovies();
        call.enqueue(new Callback<PostMovie>() {
            @Override
            public void onResponse(Call<PostMovie> call, Response<PostMovie> response) {
                if(!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                PostMovie posts = response.body();
                List<PostMovie.PostMovies> postMoviesList = posts.getResults();
                for(PostMovie.PostMovies post: postMoviesList) {
                    String content = "";

                    content += "Email: " + post.getEmail() + "\n";
                    content += "Username: " + post.getUsername() + "\n\n";
//                    content += "Release-Date: " + post.getDate() + "\n\n";

                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<PostMovie> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    public void createPost() {
//        PostMovie.PostMovies postMovies = new PostMovie.PostMovies(100.0, "BlackOps", "15-10-19");
        PostMovie.PostMovies postMovies = new PostMovie.PostMovies("Tanishq", "tanishqbhardwaj.tb@gmail.com");
        Call<PostMovie.PostMovies> call = jsonPlaceHolderApi.createPost(postMovies);
        call.enqueue(new Callback<PostMovie.PostMovies>() {
            @Override
            public void onResponse(Call<PostMovie.PostMovies> call, Response<PostMovie.PostMovies> response) {
                if(!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                PostMovie.PostMovies posts = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "UserName: " + posts.getUsername() + "\n";
                content += "Email: " + posts.getEmail() + "\n\n";
//                content += "Release-Date: " + posts.getDate() + "\n\n";

                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<PostMovie.PostMovies> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

}