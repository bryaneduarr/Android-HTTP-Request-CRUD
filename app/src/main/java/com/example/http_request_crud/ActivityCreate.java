package com.example.http_request_crud;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.http_request_crud.Config.RestApiMethods;

public class ActivityCreate extends AppCompatActivity {
  private RequestQueue requestQueue;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    EdgeToEdge.enable(this);
    setContentView(R.layout.activity_create);
    ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
      Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
      return insets;
    });
  }

  private void SendData() {
    requestQueue = Volley.newRequestQueue(this);

    StringRequest stringRequest = new StringRequest(Request.Method.GET, RestApiMethods.EndPointGET, new Response.Listener<String>() {
      @Override
      public void onResponse(String response) {
        try {
          String message = response.toString();
        } catch (Exception error) {
          Log.d("Error Response", error.toString());
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {

      }
    });

    requestQueue.add(stringRequest);
  }
}