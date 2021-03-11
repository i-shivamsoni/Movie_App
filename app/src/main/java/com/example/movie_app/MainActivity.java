package com.example.movie_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<MovieInfo> movieinfo;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connecttoServer();
        movieinfo = new ArrayList<MovieInfo>();
        adapter = new MyAdapter();
        ListView listv = findViewById(R.id.lstv);
        listv.setAdapter(adapter);
        Log.d("yay", "onCreate: popo");
    }


    private void connecttoServer() {

        RequestQueue req = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, "https://api.androidhive.info/json/movies.json", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Log.d("Success", "Test Succesfull"); Internet Connected Successfully
                try {

                    JSONArray aobject = new JSONArray(response);

                    for (int i = 0; i < aobject.length(); i++)
                    {
                        JSONObject ab = aobject.getJSONObject(i);

                        MovieInfo mi = new MovieInfo();
                        mi.setTitle(ab.getString("title"));
                        mi.setImage(ab.getString("image"));
                        mi.setRating(ab.getDouble("rating"));
                        mi.setReleaseYear(ab.getInt("releaseYear"));

                        Log.d("Test Response", "onResponse: " + mi.getTitle() + " " + mi.getImage() + " " + mi.getRating() + " " + mi.getReleaseYear());

                        //Logic to get genre
                        JSONArray st = ab.getJSONArray("genre");
                        ArrayList<String> Ary = new ArrayList<String>();
                        for (int j = 0; j < (st.length()); j++) {
                            String street = st.getString(j);
                            Ary.add(street);
                        }
                        mi.setGenre(Ary);
//                        for (String str : Ary) {
//                            Log.d("Genre", " " + str);
//                        }
                        movieinfo.add(mi);
                    }
                    Log.d("TAG", "onResponse: finish");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSON", "onResponse: exception in json ");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Internet Connection Failed
                Log.d("Error", "onErrorResponse: Failed");

            }
        });
        req.add(request); // It fires the request
    }

    class MyAdapter extends ArrayAdapter {

        public MyAdapter() {
            super(MainActivity.this, R.layout.my_layout, movieinfo);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = getLayoutInflater().inflate(R.layout.my_layout, null);
            TextView mname = v.findViewById(R.id.MovieName_my);
            ImageView avatar = v.findViewById(R.id.img_my);
            TextView rating = v.findViewById(R.id.rating_my);

            MovieInfo obj = (MovieInfo) movieinfo.get(position);
            mname.setText(obj.getTitle());
            rating.setText(obj.getRating() + "");
            Picasso.with(MainActivity.this).load(obj.getImage()).into(avatar);
            return v;
        }
    }
}