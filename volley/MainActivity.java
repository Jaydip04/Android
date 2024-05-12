package com.jaydip.volley;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String api = "https://jsonplaceholder.typicode.com/photos";
    ArrayList<userModel> allusersList;
    RecyclerView rcvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        allusersList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);
        rcvMain = findViewById(R.id.rcvMain);
        rcvMain.setLayoutManager(new LinearLayoutManager(this));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, api, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONArray array = new JSONArray(s);
                    for(int i = 0;i<array.length();i++){
                        JSONObject singleObject = array.getJSONObject(i);
                        userModel singleModel = new userModel(
                                singleObject.getInt("albumId"),
                                singleObject.getInt("id"),
                                singleObject.getString("title"),
                                singleObject.getString("url"),
                                singleObject.getString("thumbnailUrl")
                        );
                        allusersList.add(singleModel);
                    }
                    rcvMain.setAdapter(new userAdapter(MainActivity.this,allusersList));
                    Log.e("api","onResponse: "+allusersList.size());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("api","onErrorResponse: "+volleyError.getLocalizedMessage());
            }
        });
        queue.add(stringRequest);
    }
}