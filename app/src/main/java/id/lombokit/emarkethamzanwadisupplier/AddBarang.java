package id.lombokit.emarkethamzanwadisupplier;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.lombokit.emarkethamzanwadisupplier.Adapters.Adapter;
import id.lombokit.emarkethamzanwadisupplier.Models.Categories;
import id.lombokit.emarkethamzanwadisupplier.Models.Products;
import id.lombokit.emarkethamzanwadisupplier.Pesanan.Detail_pesanan;
import id.lombokit.emarkethamzanwadisupplier.Root_url.EndPoints;
import id.lombokit.emarkethamzanwadisupplier.Root_url.Url_root;
import id.lombokit.emarkethamzanwadisupplier.SessionManager.SessionManager;

public class AddBarang extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    Url_root url_root = new Url_root();
    String url_add_barang = url_root.Url + "add_barang.php";
    String url_view_barang_input = url_root.Url + "view_barang_input.php";
    String url_delete_barang = url_root.Url + "delete_barang.php";


    private int PICK_IMAGE_REQUEST = 1;
    Recycle_adapterViewBarangInput adapter;
    private Categories categories;
    RecyclerView recyclerView;
    EditText editTextNama, editTextharga, editTextStok, editTextKet, editTextStatus;
    TextView textViewJudul;
    Button cari_gambar;
    String s_nama_barang, s_harga, s_stok, s_ket, s_status;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton add_barang;
    private Bitmap bitmap;
    ImageView gambar,back;
    SessionManager sessionManager;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barang);
        init();
        event();
        eventSwipe();
        object();


    }

    private void init() {
        add_barang = findViewById(R.id.add);
        recyclerView = findViewById(R.id.list_barang);
        swipeRefreshLayout = findViewById(R.id.swipe);
        sessionManager = new SessionManager(this);
        textViewJudul = findViewById(R.id.title);
        textViewJudul.setText("Tambah barang");
        back = findViewById(R.id.back);
    }

    private void event() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            }
        });
        add_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogForm("", "", "", "", "", "Simpan");
            }
        });

    }

    private void DialogForm(String nama_barang, String harga, String stok, String ket, String status, String simpan) {
        dialog = new AlertDialog.Builder(AddBarang.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.form_add_barang, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setTitle("Form input barang ");

        editTextNama = dialogView.findViewById(R.id.nama_barang);
        editTextharga = dialogView.findViewById(R.id.harga);
        editTextKet = dialogView.findViewById(R.id.ket);
        editTextStok = dialogView.findViewById(R.id.stok);
        editTextStatus = dialogView.findViewById(R.id.status);
        gambar = dialogView.findViewById(R.id.gambar);
        cari_gambar = dialogView.findViewById(R.id.cari);
        cari_gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        dialog.setPositiveButton(simpan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                simpan_data();

            }
        });
        dialog.show();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                gambar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void simpan_data() {
        swipeRefreshLayout.setRefreshing(true);
     /*  final String bitmapcode = getStringImage(bitmap);
        s_nama_barang = editTextNama.getText().toString();
        s_harga = editTextharga.getText().toString();
        s_ket = editTextKet.getText().toString();
        s_stok = editTextStok.getText().toString();
        s_status = editTextStatus.getText().toString();
        if (s_nama_barang.isEmpty()) {
            editTextNama.setError("kosong");
        } else if (s_harga.isEmpty()) {
            editTextharga.setError("kosong");
        } else if (s_ket.isEmpty()) {
            editTextKet.setError("kosong");
        } else if (s_stok.isEmpty()) {
            editTextStok.setError("kosong");
        } else if (s_status.isEmpty()) {
            editTextStatus.setError("kosong");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_add_barang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String cek = jsonObject.getString("massage");
                    if (cek.equals("success")) {
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        Toast.makeText(getApplicationContext(), "Berhasil ditambah", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Gagal ditambah", Toast.LENGTH_LONG).show();
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
                map.put("photo",bitmapcode);
                map.put("nama_barang", s_nama_barang);
                map.put("harga", s_harga);
                map.put("ket", s_ket);
                map.put("status", s_status);
                map.put("stok", s_stok);
                return map;
            }
        };
        Volley.newRequestQueue(AddBarang.this).add(st*//*ringRequest);*/


    }

    private void uploadBitmap(final Bitmap bitmap) {
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.UPLOAD_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", "Barang Tes");
                params.put("ket", "Ini Percobaan");
                params.put("harga", "1500");
                params.put("stok", "5");
                params.put("status", "Tersedia");
                params.put("slug", "/barang_tes");
                params.put("id_kat", "1");
                params.put("id_sup", "2");
                return params;
            }


        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
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
                loadDataBarang();


            }
        });

    }

    private void object() {
        categories = new Categories();
        categories.productsArrayList = new ArrayList<>();

        adapter = new Recycle_adapterViewBarangInput(getApplicationContext(), categories);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onRefresh() {
        loadDataBarang();
    }

    private void loadDataBarang() {
        swipeRefreshLayout.setRefreshing(true);
        if (categories.productsArrayList != null) {
            categories.productsArrayList.clear();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_view_barang_input, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            int id_barang = data.getInt("id_barang");
                            String nama_barang = data.getString("nama_barang");
                            String gambar = data.getString("gambar");
                            String harga = data.getString("harga");
                            Products products = new Products(
                                    id_barang,
                                    nama_barang,
                                    gambar,
                                    harga
                            );
                            categories.productsArrayList.add(products);
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
                    swipeRefreshLayout.setRefreshing(false);

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
    public class Recycle_adapterViewBarangInput extends RecyclerView.Adapter<Recycle_adapterViewBarangInput.My_viewHolder> {
        private Context context;
        private Categories categories;

        public Recycle_adapterViewBarangInput(Context context, Categories categories) {
            this.context = context;
            this.categories = categories;
        }

        @NonNull
        @Override
        public Recycle_adapterViewBarangInput.My_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_product, parent, false);
            return new Recycle_adapterViewBarangInput.My_viewHolder(viewItem);
        }

        public class My_viewHolder extends RecyclerView.ViewHolder {

            ImageView imageViewImage, delete_barang;
            TextView textViewTitle;
            TextView textViewHarga;


            public My_viewHolder(@NonNull View itemView) {
                super(itemView);
                delete_barang = itemView.findViewById(R.id.delete_barang);
                imageViewImage = itemView.findViewById(R.id.gambar);
                textViewTitle = itemView.findViewById(R.id.nama_barang);
                textViewHarga = itemView.findViewById(R.id.harga);

            }
        }

        @Override
        public void onBindViewHolder(@NonNull Recycle_adapterViewBarangInput.My_viewHolder holder, final int position) {

            holder.textViewTitle.setText(categories.getProductsArrayList().get(position).getNama_barang());
            holder.textViewHarga.setText(categories.getProductsArrayList().get(position).getHarga());
            Glide.with(getApplicationContext()).load("https://lades.lombokit.com/upload/barang/" + categories.getProductsArrayList().get(position).getgambar()).into(holder.imageViewImage);

            holder.delete_barang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    delete_barang(categories.getProductsArrayList().get(position).getId_barang());
                }
            });
        }

        @Override
        public int getItemCount() {
            return categories.productsArrayList.size();
        }
    }

    private void delete_barang(final int id_barang) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_delete_barang, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("success")) {
                    Toast.makeText(getApplicationContext(), "Berhasil di hapus", Toast.LENGTH_LONG).show();
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
                map.put("id_barang", String.valueOf(id_barang));
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}


