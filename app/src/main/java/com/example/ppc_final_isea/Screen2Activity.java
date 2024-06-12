package com.example.ppc_final_isea;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class Screen2Activity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextView textView2;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);

        textView2 = (TextView) findViewById(R.id.textView2);
        iniciarMenuInf();

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);
        // Use 10.0.2.2 to refer to localhost from the emulator
        String url = "http://10.0.2.2:1337/api/informacions/1";

        if (checkOnlineState()) {
            // Request a JSON response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Navigate through the nested JSON to get the "info" field
                                JSONObject data = response.getJSONObject("data");
                                JSONObject attributes = data.getJSONObject("attributes");
                                String info = attributes.getString("info");

                                textView2.setText(info);
                            } catch (JSONException e) {
                                textView2.setText("La respuesta JSON no se pudo procesar");
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    textView2.setText("Error en respuesta: " + url + " -->" + error.getMessage());
                }
            });

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);
        } else {
            textView2.setText("No hay conexión a internet.");
        }
    }



    private void iniciarMenuInf() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.page_inicio);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.page_inicio) {
                goMain();
            } else if (itemId == R.id.page_riesgo) {
                // Do nothing, already on "riesgo" page
            } else if (itemId == R.id.page_info) {
                goScreen3();
            } else if (itemId == R.id.page_carga) {
                goScreen4();
            } else {
                // Handle unexpected icon
                Toast.makeText(getApplicationContext(),
                        "Icono no encontrado", Toast.LENGTH_LONG).show();
            }
            return true;
        });
    }

    //Metodo para que al volver se seleccione el icono correspondiente en el menu
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        bottomNavigationView.setSelectedItemId(R.id.page_riesgo);
    }

    public void goScreen4Calculate(View view) {
        Intent act4 = new Intent(this, Screen4Activity.class);
        startActivity(act4);
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

    public void goMain() {
        Intent act1 = new Intent(this, MainActivity.class);
        startActivity(act1);
    }

    public void goScreen2() {
        Intent act2 = new Intent(this, Screen2Activity.class);
        startActivity(act2);
    }

    public void goScreen3() {
        Intent act3 = new Intent(this, Screen3Activity.class);
        startActivity(act3);
    }

    public void goScreen4() {
        Intent act4 = new Intent(this, Screen4Activity.class);
        startActivity(act4);
    }

    public void goScreen5() {
        Intent act5 = new Intent(this, Screen5Activity.class);
        startActivity(act5);
    }
}