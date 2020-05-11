package id.lombokit.emarkethamzanwadisupplier.Pesanan;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.lombokit.emarkethamzanwadisupplier.Models.List_user;
import id.lombokit.emarkethamzanwadisupplier.Models.Users;
import id.lombokit.emarkethamzanwadisupplier.R;
import id.lombokit.emarkethamzanwadisupplier.Root_url.Url_root;
import id.lombokit.emarkethamzanwadisupplier.SessionManager.SessionAlert;
import id.lombokit.emarkethamzanwadisupplier.SessionManager.SessionManager;

public class UserPemesan extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    Url_root root = new Url_root();
    String slug_userB = "/view_userBelanja.php";
    String save_jadwal_pesan = root.Url + "/save_jadwal_pesan.php";
    String url_view_jadwal_pesan = root.Url + "/view_jadwal_pesanan.php";
    String url_update_jadwal_pesan = root.Url+"/update_jadwal_pesanan.php";
    String url_userB = root.Url + slug_userB;


    SessionManager sessionManager;
    SessionAlert sessionAlert;

    private List_user list_user;
    private RecyclerView recyclerView;
    private Recycle_adapter adapter;

    RelativeLayout layout_belanja_user;

    TextView textViewJudul;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton fab;

    TimePicker jam;

    TextView textViewJadwal_pesan, textViewJadwal_pengepulan, textViewJadwal_pengiriman;
    Button set_jadwal_pesan, set_jadwal_pengepulan, set_jadwal_pengiriman;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    Calendar calendar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pesanan);
        init();
        eventSwipe();
        eventClik();
        object();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        sessionManager = new SessionManager(this);
        sessionAlert = new SessionAlert(this);
        textViewJudul = findViewById(R.id.title);
        textViewJudul.setText("Daftar Pesanan");
        recyclerView = findViewById(R.id.list_user);
        swipeRefreshLayout = findViewById(R.id.swipe);
        layout_belanja_user = findViewById(R.id.layout_user_belanja);
        fab = findViewById(R.id.jadwal_pesanan);

        //calendar = Calendar.getInstance();


    }
    private void eventClik() {
        fab.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Rect displayRectangle = new Rect();
                Window window = getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
                dialog = new AlertDialog.Builder(UserPemesan.this);
                inflater = getLayoutInflater();
                dialogView = inflater.inflate(R.layout.setting_jadwal_pesanan, null);
                dialogView.setMinimumWidth((int) (displayRectangle.width() * 0.9f));
                dialogView.setMinimumHeight((int) (displayRectangle.height() * 0.9f));
                jam = dialogView.findViewById(R.id.jam);
                textViewJadwal_pesan = dialogView.findViewById(R.id.jadwal_pesanan);
                textViewJadwal_pengepulan = dialogView.findViewById(R.id.jadwal_pengepulan);
                textViewJadwal_pengiriman = dialogView.findViewById(R.id.jadwal_pengiriman);
                set_jadwal_pesan = dialogView.findViewById(R.id.set_jadwal_pesanan);
                set_jadwal_pengepulan = dialogView.findViewById(R.id.set_jadwal_pengepulan);
                set_jadwal_pengiriman = dialogView.findViewById(R.id.set_jadwal_pengiriman);
                dialog.setView(dialogView);
                dialog.setCancelable(true);
                final int[] get_hour_from_picker = new int[1];
                final int[] get_minute_from_picker = new int[1];


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_view_jadwal_pesan, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                String jam_pesanan = data.getString("jam_pesan");
                                String jam_pengepulan = data.getString("jam_pengepulan");
                                String jam_pengiriman = data.getString("jam_pengiriman");
                                textViewJadwal_pesan.setText(jam_pesanan);
                                textViewJadwal_pengepulan.setText(jam_pengepulan);
                                textViewJadwal_pengiriman.setText(jam_pengiriman);

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
                        map.put("id_sup", sessionManager.getSpIduser());
                        return map;
                    }
                };
                Volley.newRequestQueue(UserPemesan.this).add(stringRequest);

                jam.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        get_hour_from_picker[0] = hourOfDay;
                        get_minute_from_picker[0] = minute;

                    }
                });

                set_jadwal_pesan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textViewJadwal_pesan.setText(new StringBuilder().append(get_hour_from_picker[0]).append(":").append(get_minute_from_picker[0]));


                    }
                });
                set_jadwal_pengepulan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textViewJadwal_pengepulan.setText(new StringBuilder().append(get_hour_from_picker[0]).append(":").append(get_minute_from_picker[0]));
                    }
                });
                set_jadwal_pengiriman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textViewJadwal_pengiriman.setText(new StringBuilder().append(get_hour_from_picker[0]).append(":").append(get_minute_from_picker[0]));
                    }
                });

                dialog.setPositiveButton("Simpan", null);



                dialog.setNeutralButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update_jadwal_pesan, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("success")) {
                                    Toast.makeText(getApplicationContext(), "Berhasil diupdate", Toast.LENGTH_LONG).show();
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
                                map.put("jam_pesanan", textViewJadwal_pesan.getText().toString());
                                map.put("jam_pengepulan", textViewJadwal_pengepulan.getText().toString());
                                map.put("jam_pengiriman", textViewJadwal_pengiriman.getText().toString());
                                map.put("id_sup", sessionManager.getSpIduser());
                                return map;
                            }
                        };
                        Volley.newRequestQueue(UserPemesan.this).add(stringRequest);

                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                final AlertDialog alertDialog = dialog.create();
                alertDialog.show();

            if (sessionAlert.getSpLogined()==true){
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setVisibility(View.GONE);
            }else {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, save_jadwal_pesan, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.contains("success")) {
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition(0, 0);
                                    sessionAlert.saveBoolean(SessionAlert.SP_TRUE,true);
                                    Toast.makeText(getApplicationContext(), "Berhasil ditambah", Toast.LENGTH_LONG).show();
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
                                map.put("jam_pesanan", textViewJadwal_pesan.getText().toString());
                                map.put("jam_pengepulan", textViewJadwal_pengepulan.getText().toString());
                                map.put("jam_pengiriman", textViewJadwal_pengiriman.getText().toString());
                                map.put("id_sup", sessionManager.getSpIduser());
                                return map;
                            }
                        };
                        Volley.newRequestQueue(UserPemesan.this).add(stringRequest);

                    }


                });
            }



            }
        });
    }
    @SuppressLint("ResourceAsColor")
    private void eventSwipe() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(true);
                }
                loadData();

            }
        });
    }

    private void object() {
        list_user = new List_user();
        list_user.usersArrayList = new ArrayList<>();
        adapter = new Recycle_adapter(getApplicationContext(), list_user);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        if (list_user.usersArrayList != null) {
            list_user.usersArrayList.clear();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_userB, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("kosong")) {
                        LayoutInflater inflater = getLayoutInflater();
                        View view = inflater.inflate(R.layout.massage, null);
                        layout_belanja_user.addView(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                        recyclerView.setVisibility(View.GONE);
                    }
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            String id = data.getString("id");
                            String nama = data.getString("nama");
                            String telpon = data.getString("telpon");
                            int total_belanja = data.getInt("total");
                            int jumlah_belanja = data.getInt("jml_barang");

                            Users users = new Users(
                                    id,
                                    nama,
                                    telpon,
                                    total_belanja,
                                    jumlah_belanja

                            );
                            list_user.usersArrayList.add(users);
                        }
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    swipeRefreshLayout.setRefreshing(false);

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

                    return map;
                }
            };
            Volley.newRequestQueue(this).add(stringRequest);

        }

    }
    public class Recycle_adapter extends RecyclerView.Adapter<Recycle_adapter.My_viewHolder> {
        private Context context;
        private List_user list_user;

        public Recycle_adapter(Context context, List_user list_user) {
            this.context = context;
            this.list_user = list_user;
        }

        @NonNull
        @Override
        public My_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_user, parent, false);
            return new Recycle_adapter.My_viewHolder(viewItem);
        }

        public class My_viewHolder extends RecyclerView.ViewHolder {

            TextView textViewNama, textViewTlp, textViewAlamat, textViewCount, textViewSum;


            public My_viewHolder(@NonNull View itemView) {
                super(itemView);

                textViewNama = itemView.findViewById(R.id.nama_user);
                textViewTlp = itemView.findViewById(R.id.no_hp);
                textViewCount = itemView.findViewById(R.id.v_count);
                textViewSum = itemView.findViewById(R.id.sum);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final My_viewHolder holder, final int position) {
            holder.textViewNama.setText(list_user.getUsersArrayList().get(position).getNama());
            holder.textViewTlp.setText(list_user.getUsersArrayList().get(position).getTelpon());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ListBarangStatusBerbeda.class);
                    intent.putExtra("id_user", list_user.getUsersArrayList().get(position).getId());
                    startActivity(intent);
                }
            });

            int convert_total = list_user.usersArrayList.get(position).getTotal_belanja();
            Locale localeID = new Locale("in", "ID");
            NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(localeID);
            holder.textViewSum.setText(rupiahFormat.format((double) convert_total));
            holder.textViewCount.setText(""+list_user.getUsersArrayList().get(position).getJumlah());


        }


        @Override
        public int getItemCount() {
            return list_user.usersArrayList.size();
        }
    }







}
