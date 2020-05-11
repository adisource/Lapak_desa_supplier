package id.lombokit.emarkethamzanwadisupplier;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.lombokit.emarkethamzanwadisupplier.Root_url.Url_root;
import id.lombokit.emarkethamzanwadisupplier.SessionManager.SessionManager;

public class ProfileActivity extends AppCompatActivity {

    SessionManager sessionManager;
    Url_root root = new Url_root();
    String slug_profil = "view_profil.php";
    String slug_update_username_password ="update_username_password.php";
    String slug_update_alamat = "update_alamat_suplayer.php";
    String slug_update_kontak = "update_kontak_suplayer.php";
    String url_profil = root.Url + slug_profil;
    String url_username_password = root.Url+slug_update_username_password;
    String url_update_alamat = root.Url+slug_update_alamat;
    String url_update_kontak = root.Url+slug_update_kontak;

    String pass;
    String username;
    String email;
    String kontak;
    String alamat;
    String kabupaten;
    String kecamatan;
    String nama_desa;

    ImageView kembali, pemberitahuan;
    TextView judul,
            textViewUsername,
            textViewKontak,
            textViewEmail,
            textViewNamaToko,
            textViewKab,
            textViewKec,
            textViewDesa,
            textViewAlamat,
            textViewPass,
            textViewEdit_username_pass,textVieweditAlamat,textViewEditKontak;
    EditText editTextusername, editTextpassword,editTextAlamat,editTextKontak_NoHp,editTextEmail;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sessionManager = new SessionManager(this);
        init();

        getProfil();
        eventClick();
        eventEdit();
    }


    private void init() {
        kembali = findViewById(R.id.back);
        judul = findViewById(R.id.title);
        pemberitahuan = findViewById(R.id.notif);
        textViewUsername = findViewById(R.id.username);
        textViewKontak = findViewById(R.id.no_hp);
        textViewEmail = findViewById(R.id.email);
        textViewKab = findViewById(R.id.kab);
        textViewKec = findViewById(R.id.kec);
        textViewDesa = findViewById(R.id.desa);
        textViewAlamat = findViewById(R.id.alamat);
        textViewPass = findViewById(R.id.password);
        textViewEdit_username_pass = findViewById(R.id.edit_username_pass);
        textViewEditKontak = findViewById(R.id.editkontak);
        textVieweditAlamat = findViewById(R.id.editalamat);

    }

    private void getProfil() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_profil, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        pass = data.getString("password");
                        username = data.getString("username");
                        email = data.getString("email");
                        kontak = data.getString("kontak");
                        alamat = data.getString("alamat");
                        kabupaten = data.getString("kabupaten");
                        kecamatan = data.getString("kecamatan");
                        nama_desa = data.getString("nama_desa");
                        textViewUsername.setText(username);
                        textViewKontak.setText(kontak);
                        textViewEmail.setText(email);
                        textViewKab.setText(kabupaten);
                        textViewKec.setText(kecamatan);
                        textViewDesa.setText(nama_desa);
                        textViewAlamat.setText(alamat);
                        textViewPass.setText(pass);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id_suplayer", sessionManager.getSpIduser());
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void eventClick() {
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            }
        });

        pemberitahuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
            }
        });
        judul.setText((CharSequence) "Profil");


    }

    private void eventEdit() {
        textViewEdit_username_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(ProfileActivity.this);
                inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.form_edit_username_pass, null);
                dialog.setView(dialogView);
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("Edit username dan password");
                editTextusername = dialogView.findViewById(R.id.username);
                editTextpassword = dialogView.findViewById(R.id.password);
                editTextusername.setText(username);
                editTextpassword.setText(pass);
                dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                  StringRequest stringRequest = new StringRequest(Request.Method.POST, url_username_password, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("success")) {
                                    Toast.makeText(getApplicationContext(), "Berhasil di update", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Gagal update", Toast.LENGTH_LONG).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<>();
                                map.put("id_sup", sessionManager.getSpIduser());
                                map.put("username", editTextusername.getText().toString());
                                map.put("password",editTextpassword.getText().toString());
                                return map;
                            }
                        };
                        Volley.newRequestQueue(ProfileActivity.this).add(stringRequest);

                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }

    });
        textVieweditAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(ProfileActivity.this);
                inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.form_edit_alamat, null);
                dialog.setView(dialogView);
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("Edit alamat lengkap");
                editTextAlamat = dialogView.findViewById(R.id.alamat);
                editTextAlamat.setText(alamat);

                dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                 StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update_alamat, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("success")) {
                                    Toast.makeText(getApplicationContext(), "Berhasil di update", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Gagal update", Toast.LENGTH_LONG).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<>();
                                map.put("id_sup", sessionManager.getSpIduser());
                                map.put("alamat", editTextAlamat.getText().toString());
                                return map;
                            }
                        };
                        Volley.newRequestQueue(ProfileActivity.this).add(stringRequest);

                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        textViewEditKontak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(ProfileActivity.this);
                inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.form_edit_kontak, null);
                dialog.setView(dialogView);
                dialog.setIcon(R.mipmap.ic_launcher);
                dialog.setTitle("Edit Kontak");
                editTextKontak_NoHp= dialogView.findViewById(R.id.nomer_hp);
                editTextEmail = dialogView.findViewById(R.id.email);
                editTextKontak_NoHp.setText(kontak);
                editTextEmail.setText(email);
                dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
               StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update_kontak, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("success")) {
                                    Toast.makeText(getApplicationContext(), "Berhasil di update", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Gagal update", Toast.LENGTH_LONG).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<>();
                                map.put("id_sup", sessionManager.getSpIduser());
                                map.put("kontak", editTextKontak_NoHp.getText().toString());
                                map.put("email",editTextEmail.getText().toString());
                                return map;
                            }
                        };
                        Volley.newRequestQueue(ProfileActivity.this).add(stringRequest);

                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
}
}
