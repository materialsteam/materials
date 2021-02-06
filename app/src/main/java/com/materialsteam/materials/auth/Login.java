package com.materialsteam.materials.auth;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.materialsteam.materials.BottomNavigation;
import com.materialsteam.materials.R;
import com.materialsteam.materials.config.Connections;
import com.materialsteam.materials.config.Functions;
import com.materialsteam.materials.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private TextView textView33;
    private EditText et_email, et_password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // cek session is login
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(Login.this, BottomNavigation.class));
            return;
        }

        et_email = findViewById(R.id.editText);
        et_password = findViewById(R.id.editText2);
        Button btn_login = findViewById(R.id.button);
        TextView link_regis = findViewById(R.id.textView34);

        link_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    private void login() {
        final String email = this.et_email.getText().toString().trim();
        final String password = this.et_password.getText().toString().trim();

        // validasi input
        if (TextUtils.isEmpty(email)) {
            Functions.setValidate(et_email, "Email wajib diisi!");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Functions.setValidate(et_email,"Email tidak valid!");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Functions.setValidate(et_password, "Password wajib diisi!");
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Connections.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String pesan = jsonObject.getString("message");
                            String field = jsonObject.getString("field");

                            switch (status) {
                                case "1":
                                    JSONObject userJson = jsonObject.getJSONObject("data");
                                    User user = new User(
                                            userJson.getString("kd_user").trim(),
                                            userJson.getString("nama").trim(),
                                            userJson.getString("email").trim(),
                                            userJson.getString("no_hp").trim(),
                                            userJson.getString("alamat").trim()
                                    );
                                    SharedPrefManager.getInstance(Login.this).setUser(user);
                                    finish();
                                    startActivity(new Intent(Login.this, BottomNavigation.class));
                                    break;
                                case "0":

                                    switch (field) {
                                        case "email":
                                            Functions.setValidate(et_email, pesan);
                                            break;
                                        case "password":
                                            Functions.setValidate(et_password, pesan);
                                            break;
                                        default:
                                            Toast.makeText(Login.this, pesan, Toast.LENGTH_LONG).show();
                                    }
                                    break;

                                default:
                                    Toast.makeText(Login.this, "Login Gagal", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            // Toast.makeText(Login.this, "Error: "+e.toString(), Toast.LENGTH_LONG)
                            //  .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        // Toast.makeText(getApplicationContext(), "Error: "+error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Mohon Tunggu..");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}