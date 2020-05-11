package id.lombokit.emarkethamzanwadisupplier.Pesanan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.lombokit.emarkethamzanwadisupplier.Models.Categories;
import id.lombokit.emarkethamzanwadisupplier.Models.Products;
import id.lombokit.emarkethamzanwadisupplier.R;
import id.lombokit.emarkethamzanwadisupplier.Root_url.Url_root;
import id.lombokit.emarkethamzanwadisupplier.SessionManager.SessionManager;

public class Detail_pesanan extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    Url_root root = new Url_root();
    String slug_viewProduct="view_product.php";
    String slug_totalHarga ="totalHarga.php";
    String slug_proses_barang ="proses_barang.php";
    String url_viewProduct = root.Url+slug_viewProduct;
    String url_totalHarga = root.Url+slug_totalHarga;
    String url_proses = root.Url+slug_proses_barang;
    String url_update_status = root.Url+"update_status.php";
    String url_push_noatifikasi= root.Url+"notifikasi_belanja.php";

    String token_device;

    private Categories categories;
    private RecyclerView recyclerView;
    private Recycle_adapter adapter;
    SessionManager sessionManager;

    TextView textViewJudul,textViewValueSum;
    Button btn_proses;
    SwipeRefreshLayout swipeRefreshLayout;
    String id_user,v_time;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);

        init();
        eventSwipe();
        object();
        sumBelanja();
        eventClik();


        //proses_barang_pesanan();
    }




    private void init() {

        sessionManager = new SessionManager(this);
        textViewJudul = findViewById(R.id.title);
        textViewValueSum = findViewById(R.id.value_sum);
        swipeRefreshLayout = findViewById(R.id.swipe);
        recyclerView = findViewById(R.id.list_barang);
        btn_proses = findViewById(R.id.selesaikan);
        id_user = getIntent().getStringExtra("id_user");
        textViewJudul.setText("Daftar Barang Pesanan");
    }

    @SuppressLint("ResourceAsColor")
    private  void eventSwipe(){
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout != null){
                    swipeRefreshLayout.setRefreshing(true);
                }
                loadDataBarang();


            }
        });

    }
    private void object() {
        categories = new Categories();
        categories.productsArrayList = new ArrayList<>();

        adapter = new Recycle_adapter(getApplicationContext(),categories);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onRefresh() {
        loadDataBarang();
    }

    private  void loadDataBarang(){
        swipeRefreshLayout.setRefreshing(true);
        if (categories.productsArrayList !=null){
            categories.productsArrayList.clear();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_viewProduct, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0 ;i<jsonArray.length();i++){
                            JSONObject data = jsonArray.getJSONObject(i);
                            int id_barang = data.getInt("id_barang");
                            String nama_barang = data.getString("nama_barang");
                            String gambar = data.getString("gambar");
                            String harga = data.getString("harga");
                            String qty = data.getString("qty");
                            String total = data.getString("total");
                            String jam = data.getString("jam");
                            String status = data.getString("status");
                            String get_token_device_user = data.getString("token");
                            Products products = new Products(
                                    id_barang,
                                    nama_barang,
                                    gambar,
                                    harga,
                                    qty,
                                    total,
                                    jam,
                                    status,
                                    get_token_device_user

                            );
                            categories.productsArrayList.add(products);
                        }
                        recyclerView.setAdapter(adapter);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<>();
                    map.put("id",id_user);
                    map.put("id_sup",sessionManager.getSpIduser());
                    return map;
                }
            };
            Volley.newRequestQueue(this).add(stringRequest);
        }

    }



    private  void sumBelanja(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_totalHarga, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int total = jsonObject.getInt("sum");
                    Locale localeID = new Locale("in","ID");
                    NumberFormat rupiahFormat=NumberFormat.getCurrencyInstance(localeID);
                    textViewValueSum.setText(rupiahFormat.format((double)total));

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
                map.put("id",id_user);
                map.put("id_sup",sessionManager.getSpIduser());
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
    private void eventClik() {
        btn_proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proses_barang_pesanan();
            }
        });
    }
    private void proses_barang_pesanan() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateNow = new SimpleDateFormat("yyyy/MM/dd h:m:s ");
        v_time = dateNow.format(calendar.getTime());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_proses, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")){
                    startActivity(new Intent(getApplicationContext(), UserPemesan.class));
                    Toast.makeText(getApplicationContext(),"Barang berhasil proses",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Barang gagal proses",Toast.LENGTH_LONG).show();
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
                map.put("id",id_user);
                map.put("jam",v_time);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }
    public  class Recycle_adapter extends RecyclerView.Adapter<Recycle_adapter.My_viewHolder>{
        private Context context;
        private Categories categories;

        public Recycle_adapter(Context context, Categories categories) {
            this.context = context;
            this.categories = categories;
        }

        @NonNull
        @Override
        public Recycle_adapter.My_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_product,parent,false);
            return new Recycle_adapter.My_viewHolder(viewItem);
        }
        public class My_viewHolder extends RecyclerView.ViewHolder {

            ImageView imageViewImage;
            TextView textViewTitle;
            TextView textViewHarga;
            TextView textViewQuantityTxt;
            TextView textViewTotalharga;
            TextView textViewJam;
            RadioGroup radioGroup;
            RadioButton radioButtonProses,radioButtonTolak;
            LinearLayout layout_green;



            public My_viewHolder(@NonNull View itemView) {
                super(itemView);
                textViewTitle = itemView.findViewById(R.id.nama_barang);
                textViewHarga = itemView.findViewById(R.id.price);
                textViewQuantityTxt = itemView.findViewById(R.id.quatity);
                textViewTotalharga = itemView.findViewById(R.id.totaL_harga);
                imageViewImage  = itemView.findViewById(R.id.image);
                textViewJam =itemView.findViewById(R.id.value_tglpesanan);
                radioGroup = itemView.findViewById(R.id.rg);
                radioButtonProses =itemView.findViewById(R.id.proses);
                radioButtonTolak =itemView.findViewById(R.id.tolak);
                layout_green = itemView.findViewById(R.id.green);


            }
        }

        @Override
        public void onBindViewHolder(@NonNull final Recycle_adapter.My_viewHolder holder, final int position) {
            Locale localeID = new Locale("in","ID");
            NumberFormat rupiahFormat=NumberFormat.getCurrencyInstance(localeID);
            int c_harga = Integer.parseInt(categories.getProductsArrayList().get(position).getHarga());
            int c_totalH =Integer.parseInt(categories.getProductsArrayList().get(position).getTotal());

            holder.textViewTitle.setText(categories.getProductsArrayList().get(position).getNama_barang());
            holder.textViewHarga.setText(rupiahFormat.format((double)c_harga));
            holder.textViewQuantityTxt.setText(categories.getProductsArrayList().get(position).getQty());
            holder.textViewTotalharga.setText(rupiahFormat.format((double)c_totalH));
            holder.textViewJam.setText(categories.getProductsArrayList().get(position).getJam());
            Glide.with(getApplicationContext()).load("https://lades.lombokit.com/upload/barang/"+categories.getProductsArrayList().get(position).getgambar()).into(holder.imageViewImage);

            final int parse = Integer.parseInt(categories.getProductsArrayList().get(position).getStatus());
                 if (parse==1){
                     holder.radioButtonProses.setChecked(true);
                 }else if(parse==2){
                     holder.radioGroup.setVisibility(View.GONE);
                     holder.layout_green.setVisibility(View.VISIBLE);
                 }else{
                     holder.layout_green.setVisibility(View.GONE);
                     holder.radioGroup.setVisibility(View.VISIBLE);
                 }

            holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch(checkedId){
                        case R.id.proses:
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                            update_status(categories.getProductsArrayList().get(position).getId_barang());
                            push_notifikasi("Pesanan anda sedang di proses",categories.getProductsArrayList().get(position).getToken());
                        case R.id.tolak:
                            push_notifikasi("Ditolak",categories.getProductsArrayList().get(position).getToken());
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return categories.productsArrayList.size();
        }
    }

    private void push_notifikasi(final String s, final String token) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_push_noatifikasi, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("token",token);
                map.put("title","Pesan dari suplayer");
                map.put("body",s);
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void update_status(final int id_barang) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update_status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")){
                    startActivity(new Intent(getApplicationContext(), UserPemesan.class));
                    Toast.makeText(getApplicationContext(),"Barang berhasil proses",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Barang gagal proses",Toast.LENGTH_LONG).show();
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
                map.put("id",id_user);
                map.put("id_barang",String.valueOf(id_barang));
                return map;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
