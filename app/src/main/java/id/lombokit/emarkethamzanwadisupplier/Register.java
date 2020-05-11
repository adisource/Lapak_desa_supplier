package id.lombokit.emarkethamzanwadisupplier;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.lombokit.emarkethamzanwadisupplier.Models.ModelDesa;
import id.lombokit.emarkethamzanwadisupplier.Root_url.Url_root;

public class Register extends AppCompatActivity {
    Url_root url_root = new Url_root();
    String url_get_nama_desa = url_root.Url+"get_nama_desa.php";
    String url_register_suplayer = url_root.Url+"register_suplayer.php";
    TextView textViewDaftar,textViewLogin,textViewJudul;
    Spinner spinnerNama_desa,spinnerPilihPenyedia;
    List<ModelDesa> modelDesas = new ArrayList<>();
    List<String>listnama_desa = new ArrayList<>();
    private String[] penyedia = {"Pilih Jenis Penyuplai","BUMDES/BUMKEL", "TOKO"};

    int id_desa;
    String s_id_desa;
    int bumdes;

    EditText editTextUsername,
            editTextNama,
            editTextKontak,
            editTextemail,
            editTextAlamat,
            editTextPassword;

    String s_username,s_nama,s_kontak,s_email,s_alamat,s_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();



        getDataDesa();
        get_id();
        eventSppiner();



    }



    private void init() {
        editTextUsername = findViewById(R.id.username);
        editTextNama = findViewById(R.id.nama_toko);
        editTextKontak = findViewById(R.id.kontak);
        editTextemail = findViewById(R.id.alamat_email);
        editTextAlamat = findViewById(R.id.alamat);
        editTextPassword = findViewById(R.id.pass);
        textViewDaftar = findViewById(R.id.daftar);
        textViewJudul = findViewById(R.id.title);
        spinnerNama_desa = findViewById(R.id.nama_desa);
        spinnerPilihPenyedia = findViewById(R.id.pilih_penyedia);
        textViewJudul.setText("Register pengguna baru");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spiner_item, penyedia);
        spinnerPilihPenyedia.setAdapter(adapter);



    }
    private void getDataDesa() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_get_nama_desa, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listnama_desa.add(0,"Pilih Desa");
                try {
                    JSONArray jsonArray =new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        int id = data.getInt("id_desa");
                        String nama_desa = data.getString("nama_desa");
                        ModelDesa modelDesa = new ModelDesa(
                                id,
                                nama_desa
                        );
                        modelDesas.add(modelDesa);
                    }
                    for (int i = 0; i < modelDesas.size(); i++) {
                        listnama_desa.add(modelDesas.get(i).getNama_desa());
                    }
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.spiner_item, listnama_desa);
                    spinnerNama_desa.setAdapter(spinnerAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),""+error,Toast.LENGTH_LONG).show();

            }
        });
        Volley.newRequestQueue(this).add(stringRequest);

        spinnerNama_desa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!spinnerNama_desa.getSelectedItem().toString().equals("Pilih Desa")) {
                    id_desa = modelDesas.get(position - 1).getId_desa();

                }else{

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void get_id() {
        textViewDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bumdes = spinnerPilihPenyedia.getSelectedItemPosition();
                if (bumdes==2){
                    bumdes=0;
                }
                s_username = editTextUsername.getText().toString();
                s_nama =editTextNama.getText().toString();
                s_kontak = editTextKontak.getText().toString();
                s_email =editTextemail.getText().toString();
                s_alamat = editTextAlamat.getText().toString();
                s_password =editTextPassword.getText().toString();

                if (s_username.isEmpty()){
                    editTextUsername.setError("Username Kosong ");
                    editTextUsername.requestFocus();
                }else if(s_nama.isEmpty()){
                    editTextNama.setError("Nama Kosong");
                    editTextNama.requestFocus();
                }else if (s_kontak.isEmpty()){
                    editTextKontak.setError("Kontak kosong");
                    editTextKontak.requestFocus();
                }else if(s_email.isEmpty()){
                    editTextemail.setError("Email Kosong");
                    editTextemail.requestFocus();
                }else  if (s_alamat.isEmpty()){
                    editTextAlamat.setError("Alamat kosong");
                    editTextAlamat.requestFocus();
                }else if (s_password.isEmpty()){
                    editTextPassword.setError("Password Kosong");
                    editTextPassword.requestFocus();
                }else if(spinnerPilihPenyedia.getSelectedItem().toString()=="Pilih Jenis Penyuplai"){
                    Toast.makeText(getApplicationContext(),"Anda belum memilih jenis penyuplai",Toast.LENGTH_LONG).show();
                }else if(spinnerNama_desa.getSelectedItem().toString()=="Pilih Desa"){
                    Toast.makeText(getApplicationContext(),"Anda belum memilih desa",Toast.LENGTH_LONG).show();
                }else {
                    prosesRegister();
                }


            }
        });

    }

    private void prosesRegister() {
        s_id_desa = String.valueOf(id_desa);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_register_suplayer, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String cek = jsonObject.getString("cek");
                    if (cek.equals("exsits")){
                        Toast.makeText(getApplicationContext(),"Anda sudah mendaftar sebelum silakan login",Toast.LENGTH_LONG).show();
                    }else if(cek.equals("success")){
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Berhasil terdaftar",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"Gagal Mendaftar",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("username",s_username);
                map.put("nama",s_nama);
                map.put("nomer_hp",s_kontak);
                map.put("email",s_email);
                map.put("id_desa",s_id_desa);
                map.put("alamat",s_alamat);
                map.put("bumdes", String.valueOf(bumdes));
                map.put("password",s_password);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
    private void eventSppiner() {

    }

}
