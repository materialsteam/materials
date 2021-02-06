package com.materialsteam.materials.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.materialsteam.materials.R;
import com.materialsteam.materials.config.Connections;
import com.materialsteam.materials.config.Functions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Aktivasi_akun extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private EditText et_kode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktivasi_akun);

        et_kode = findViewById(R.id.editText2);
        Button btn_verifikasi = findViewById(R.id.button);

        btn_verifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(Verifikasi_Akun.this, Login.class));
                //finish();
                verifikasi();
            }
        });
    }

    private void verifikasi() {
        Bundle bundle = getIntent().getExtras();
        final String kode = this.et_kode.getText().toString().trim();
        final String email = bundle.getString("email");

        if (TextUtils.isEmpty(kode)) {
            Functions.setValidate(et_kode,"masukkan kode verifikasi!");
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Connections.URL_VERIFIKASI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            switch (status){
                                case "1":
                                    finish();
                                    startActivity(new Intent(Aktivasi_akun.this, Login.class));
                                    break;
                                case "0":
                                    Functions.setValidate(et_kode,"kode verifikasi salah!");
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(Aktivasi_akun.this, "Error: "+e.toString(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Aktivasi_akun.this, "Error: "+error.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("code", kode);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);

        progressDialog = new ProgressDialog(Aktivasi_akun.this);
        progressDialog.setMessage("Mengecek..");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}
