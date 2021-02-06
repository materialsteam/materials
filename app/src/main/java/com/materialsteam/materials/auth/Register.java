package com.materialsteam.materials.auth;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.materialsteam.materials.BottomNavigation;
import com.materialsteam.materials.R;
import com.materialsteam.materials.auth.Aktivasi_akun;
import com.materialsteam.materials.auth.Login;
import com.materialsteam.materials.auth.SharedPrefManager;
import com.materialsteam.materials.config.Connections;
import com.materialsteam.materials.config.Functions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {

    private EditText et_nama, et_email, et_password, et_no_hp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // cek session is login
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(Register.this, BottomNavigation.class));
            return;
        }

        et_nama = findViewById(R.id.editText5);
        et_email = findViewById(R.id.editText);
        et_password = findViewById(R.id.editText2);
        et_no_hp = findViewById(R.id.editText7);
        Button btn_regis = findViewById(R.id.button);
        TextView link_login = findViewById(R.id.textView34);

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

    }
    @TargetApi(21)
    private void signUp() {
        final String nama = this.et_nama.getText().toString().trim();
        final String email = this.et_email.getText().toString().trim();
        final String password = this.et_password.getText().toString().trim();
        final String no_telpon = this.et_no_hp.getText().toString().trim();

        final int random  = ThreadLocalRandom.current().nextInt();
        final String rand = String.valueOf(random);
        final String subject = "Aktivasi Akun Materials";
        final String message = "Untuk registrasi, masukkan kode verifikasi "+rand+". JANGAN BERIKAN kode kepada siapapun termasuk tim Materials";

        // Custom Regular Expression
        String angka = "^[0-9]*$";
        String huruf = "^[a-zA-Z ]+$";

        if (TextUtils.isEmpty(nama)) {
            Functions.setValidate(et_nama,"Nama lengkap wajib diisi!");
            return;
        }

        if (!nama.matches(huruf)) {
            Functions.setValidate(et_nama, "Nama Lengkap tidak boleh berisi angka atau simbol khusus!");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Functions.setValidate(et_email,"Email wajib diisi!");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Functions.setValidate(et_email,"Email tidak valid!");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Functions.setValidate(et_password,"Password wajib diisi!");
            return;
        }

        if (TextUtils.isEmpty(no_telpon)) {
            Functions.setValidate(et_no_hp,"Nomor Telepon wajib diisi!");
            return;
        }

        if((no_telpon.length() != 12 || !no_telpon.matches(angka)) && no_telpon.length() != 11) {
            Functions.setValidate(et_no_hp,"Nomor telepon harus sesuai format dan tidak boleh huruf atau simbol khusus!");
            return;
        }

        // request volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Connections.URL_REGIS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String pesan = jsonObject.getString("message");
                            String field = jsonObject.getString("field");

                            Log.i("status", status);

                            switch (status) {
                                case "1":
                                    Toast.makeText(Register.this, "Registrasi Berhasil", Toast.LENGTH_LONG).show();
                                    finish();
                                        // send email
                                        SendMail sm = new SendMail(Register.this, email, subject, message);
                                        sm.execute();

                                        Bundle bundle = new Bundle();
                                        bundle.putString("email", email);
                                        Intent intent = new Intent(Register.this, Aktivasi_akun.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    break;

                                case "0":

                                    switch (field) {
                                        case "email":
                                            Functions.setValidate(et_email, pesan);
                                            break;
                                        case "no_hp":
                                            Functions.setValidate(et_no_hp, pesan);
                                            break;
                                        default:
                                            Toast.makeText(Register.this, "Registrasi Gagal", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                default:
                                    Toast.makeText(Register.this, "Registrasi Gagal", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("random", rand);
                params.put("password", password);
                params.put("no_telpon", no_telpon);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(stringRequest);

        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setMessage("Mohon Tunggu..");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}
