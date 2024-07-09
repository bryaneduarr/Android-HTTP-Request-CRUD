package com.example.http_request_crud;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

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
import com.example.http_request_crud.Models.Personas;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class ActivityCreate extends AppCompatActivity {
  private RequestQueue requestQueue;
  Button buttonFoto, buttonGuardar;

  EditText nombres, apellidos, direccion, telefono;

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
    nombres = (EditText) findViewById(R.id.nombreEditText);
    apellidos = (EditText) findViewById(R.id.apellidoEditText);
    telefono = (EditText) findViewById(R.id.telefonoEditText);
    direccion = (EditText) findViewById(R.id.direccionEditText);
  }

  private void SendDataCreate() {
    requestQueue = Volley.newRequestQueue(this);
    Personas person = new Personas();

    person.setNombres(nombres.getText().toString());
    person.setNombres(apellidos.getText().toString());
    person.setNombres(telefono.getText().toString());
    person.setNombres(direccion.getText().toString());

    JSONObject jsonObject = new JSONObject();

    try {
      jsonObject.put("nombres", person.getNombres());
      jsonObject.put("apellidos", person.getApellido());
      jsonObject.put("telefono", person.getTelefono());
      jsonObject.put("direccion", person.getDireccion());
      jsonObject.put("foto", person.getFoto());
    } catch (Exception error) {
      Log.d("Error Response", error.toString());
    }

    StringRequest stringRequest = new StringRequest(Request.Method.POST, RestApiMethods.EndPointGET, new Response.Listener<String>() {
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
  }

  /*private void SendData() {
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
  } */

  private String ConvertImageBase64(String path) {
    Bitmap bitmap = BitmapFactory.decodeFile(path);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
    byte[] imagearray = byteArrayOutputStream.toByteArray();
    return Base64.encodeToString(imagearray, Base64.DEFAULT);

  }
}