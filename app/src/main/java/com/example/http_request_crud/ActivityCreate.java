package com.example.http_request_crud;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.http_request_crud.Models.Personas;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityCreate extends AppCompatActivity {
  private RequestQueue requestQueue;

  Button buttonFoto, buttonGuardar;

  ImageView imageView;

  EditText nombres, apellidos, direccion, telefono;

  static final int REQUEST_IMAGE = 101;

  static final int ACCESS_CAMERA = 201;

  String currentPhotoPath;

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
    imageView = (ImageView) findViewById(R.id.imageView);
    nombres = (EditText) findViewById(R.id.nombreEditText);
    apellidos = (EditText) findViewById(R.id.apellidoEditText);
    telefono = (EditText) findViewById(R.id.telefonoEditText);
    direccion = (EditText) findViewById(R.id.direccionEditText);
    // Buttons
    buttonFoto = (Button) findViewById(R.id.imgaeViewButton);
    buttonGuardar = (Button) findViewById(R.id.buttonGuardar);

    buttonFoto.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ObtenerFoto();
      }
    });

    buttonGuardar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          String nombreText = nombres.getText().toString();
          String apellidoText = apellidos.getText().toString();
          String telefonoText = telefono.getText().toString();
          String direccionText = direccion.getText().toString();
          String fotoBase64 = ConvertImageBase64(currentPhotoPath);

          JSONObject jsonObject = new JSONObject();
          jsonObject.put("nombres", nombreText);
          jsonObject.put("apellidos", apellidoText);
          jsonObject.put("telefono", telefonoText);
          jsonObject.put("direccion", direccionText);
          jsonObject.put("foto", fotoBase64);

          Log.d("Datos", jsonObject.toString());

          SendDataCreate(nombreText, apellidoText, telefonoText, direccionText, fotoBase64);

        } catch (Exception error) {
          Log.e("Error", "Error al crear el JSON: " + error.toString());
        }
      }
    });
  }

  private void ObtenerFoto() {
    // Metodo para obtener los permisos requeridos de la aplicacion
    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, ACCESS_CAMERA);
    } else {
      dispatchTakePictureIntent();

    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == ACCESS_CAMERA) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        dispatchTakePictureIntent();
      } else {
        Toast.makeText(getApplicationContext(), "se necesita el permiso de la camara", Toast.LENGTH_LONG).show();
      }
    }
  }

  private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    File image = File.createTempFile(imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */);

    // Save a file: path for use with ACTION_VIEW intents
    currentPhotoPath = image.getAbsolutePath();
    return image;
  }

  private void dispatchTakePictureIntent() {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    // Ensure that there's a camera activity to handle the intent
    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
      // Create the File where the photo should go
      File photoFile = null;
      try {
        photoFile = createImageFile();
      } catch (IOException ex) {
        ex.toString();
      }
      // Continue only if the File was successfully created
      if (photoFile != null) {
        Uri photoURI = FileProvider.getUriForFile(this, "com.example.http_request_crud.fileprovider", photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE);
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_IMAGE) {
      try {
        File foto = new File(currentPhotoPath);
        imageView.setImageURI(Uri.fromFile(foto));
      } catch (Exception ex) {
        ex.toString();
      }
    }
  }

  private void SendDataCreate(String nombreText, String apellidoText, String telefonoText, String direccionText, String fotoBase64) {
    requestQueue = Volley.newRequestQueue(this);
    // Personas person = new Personas();

    /*
    person.setNombres(nombres.getText().toString());
    person.setNombres(apellidos.getText().toString());
    person.setNombres(telefono.getText().toString());
    person.setNombres(direccion.getText().toString());
    person.setFoto(ConvertImageBase64(currentPhotoPath));
     */

    JSONObject jsonObject = new JSONObject();

    try {
      jsonObject.put("nombres", nombreText);
      jsonObject.put("apellidos", apellidoText);
      jsonObject.put("telefono", telefonoText);
      jsonObject.put("direccion", direccionText);
      jsonObject.put("foto", fotoBase64);
    } catch (Exception error) {
      Log.d("Error Response", error.toString());
    }

    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, "http://10.0.2.2/programacion-movil-1-php-crud-parcial-2/peticiones-http/CreatePerson.php", jsonObject, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        try {
          String message = response.toString();
          Log.d("Response", message);
        } catch (Exception error) {
          Log.d("Error Response", error.toString());
        }
      }
    }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        Log.d("Error Response", error.toString());
      }
    });

    requestQueue.add(stringRequest);
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