package com.example.movie_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connecttoServer();
    }


    private void connecttoServer(){
       RequestQueue req =Volley.newRequestQueue(this);
       StringRequest request = new StringRequest(Request.Method.GET,"https://api.androidhive.info/json/movies.json", new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               Log.d("Success", "Test Succesfull");
               try {
                   JSONArray aobject = new JSONArray(response);
                   for (int i=0; i<aobject.length();i++)
                   {
                       JSONObject ab = aobject.getJSONObject(i);
                       MovieInfo av = new MovieInfo();
                       av.setTitle(ab.getString("title"));
                       av.setImage(ab.getString("image"));
                       av.setRating(ab.getDouble("rating"));
                       av.setReleaseYear(ab.getInt("releaseYear"));
                       Log.d("Test Response", "onResponse: "+ av.getTitle()+" "+av.getImage()+" "+av.getRating()+" "+av.getReleaseYear());
                       JSONArray st = ab.getJSONArray("genre");
                       ArrayList<String> Ary = new ArrayList<String>();
                       for(int j=0;j<(st.length());j++)
                       {
                           String street = st.getString(j);
                            Ary.add(street);
                       }
                       av.setGenre(Ary);
                       for (String str : Ary)
                       {
                           Log.d("Genre", " "+str);
                       }
                   }



               } catch (JSONException e) {
                   e.printStackTrace();
                   Log.d("JSON", "onResponse: exception in json ");
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Log.d("Error", "onErrorResponse: Failed");

           }
       });
        req.add(request);
    }

}