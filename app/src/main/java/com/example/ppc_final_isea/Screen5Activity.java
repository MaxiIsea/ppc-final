package com.example.ppc_final_isea;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

public class Screen5Activity extends AppCompatActivity {

    ImageView image_covid;
    Button button_send;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen5);

        //Para que aparezca el boton atras en la actionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        image_covid = findViewById(R.id.imageCovid);
        button_send = findViewById(R.id.buttonSend);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImage5(image_covid);
            }
        });
        queue = Volley.newRequestQueue(this);

        double riesgoRec = getIntent().getExtras().getDouble("riesgoRec");
        double riesgoProg = getIntent().getExtras().getDouble("riesgoProg");
        boolean esquema = getIntent().getExtras().getBoolean("esquema");

        cargarImagen(riesgoRec, riesgoProg, esquema);

    }

    private void cargarImagen(double riesgoRecurrente, double riesgoProgreso, boolean esquema) {
        //Obtener la url de la imagen
        //String url = "https://ppc2021.edit.com.ar/service/api/imagen/";
        String url = "http://10.0.2.2:1337/api/images/";
        String uri = url + riesgoRecurrente + "/" + riesgoProgreso + "/" + esquema;
        Log.e("Uri para obtener url imagen: ", uri);

        if (checkOnlineState()) {
            // Request a JSON response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, uri, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String urlImagen = response.getString("imagen");
                                Log.i("Url imagen: ", urlImagen);
                                setearImagen(urlImagen);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        image_covid.setImageResource(R.drawable.error);
                        Toast.makeText(Screen5Activity.this, "Error en respuesta: " + uri + " -->" + error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
            });

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);

        } else {
            Toast.makeText(Screen5Activity.this, "No hay conexión a internet", Toast.LENGTH_LONG).show();
        }

    }

    private void setearImagen(String urlImagen) {
        //Obtener y setear la imagen.
        ImageRequest request = new ImageRequest(urlImagen,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        image_covid.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        image_covid.setImageResource(R.drawable.error);
                        Toast.makeText(Screen5Activity.this, "Error en respuesta: " + urlImagen + " -->" + error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                });
        queue.add(request);
    }

    protected void sendImage5(ImageView image) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        File f = new File(getExternalCacheDir() + "/" + getResources().getString(R.string.app_name) + ".png");
        Intent shareintent;

        try {
            FileOutputStream outputStream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            outputStream.flush();
            outputStream.close();

            shareintent = new Intent(Intent.ACTION_SEND);
            shareintent.setType("image/*");
            shareintent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
            shareintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        } catch (Exception e) {
            throw new RuntimeException();
        }
        startActivity(Intent.createChooser(shareintent, "Share Image"));
    }

    public boolean checkOnlineState() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            } else {
                // For devices with versions below Android M
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnectedOrConnecting();
            }
        }

        return false;
    }

    //Para agregar funcionalidad de ir atras en el boton del action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}