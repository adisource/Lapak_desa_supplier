package id.lombokit.emarkethamzanwadisupplier;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.lombokit.emarkethamzanwadisupplier.Root_url.Url_root;
import id.lombokit.emarkethamzanwadisupplier.SessionManager.SessionAlert;
import id.lombokit.emarkethamzanwadisupplier.SessionManager.SessionManager;

public class LoginActivity extends AppCompatActivity {
    Url_root root = new Url_root();
    String slug_login = "/login.php";
    String url_login = root.Url+slug_login;

    SessionManager sessionManager;
    SessionAlert sessionAlert;

    ImageView selebihnya;
    TextView judul;
    TextView textViewUsername,textViewPass;
    TextView login,daftar;

    String username,password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);
        sessionAlert = new SessionAlert(this);
        init();
        eventClik();




    }



    private void init() {

        judul = findViewById(R.id.title);
        selebihnya = findViewById(R.id.more);
        login = findViewById(R.id.login);
        textViewUsername = findViewById(R.id.username);
        textViewPass = findViewById(R.id.pass);
        username = textViewUsername.getText().toString();
        password = textViewPass.getText().toString();
        daftar = findViewById(R.id.daftar);

    }
    private void eventClik() {
        selebihnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
            }
        });

        judul.setText((CharSequence) "Login Pengguna");
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesLogin();
            }
        });

    }

    private void prosesLogin() {
        StringRequest  stringRequest = new StringRequest(Request.Method.POST, url_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")){
                        String id_sup = jsonObject.getString("id_sup");
                        String desa = jsonObject.getString("desa");
                        sessionManager.saveBoolean(SessionManager.SP_LOGINED,true);
                        sessionManager.saveString(SessionManager.SP_IDUSER,id_sup);
                        sessionManager.saveString(SessionManager.SP_NAMADESA,desa);
                        if (id_sup.equals(sessionManager.getSpIduser())){
                            sessionAlert.saveBoolean(SessionAlert.SP_TRUE,true);
                            Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                            startActivity(intent);
                        }else{
                            sessionAlert.saveBoolean(SessionAlert.SP_TRUE,false);
                            Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                            startActivity(intent);
                        }

                    }else if(status.equals("filed")){
                        Toast.makeText(getApplicationContext(),"Data tidak di temukan",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Server not found"+error,Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("username",textViewUsername.getText().toString());
                map.put("password",textViewPass.getText().toString());
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (sessionManager.getSpLogined()==true){
            Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
            startActivity(intent);

        }
    }
}
