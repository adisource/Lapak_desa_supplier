package id.lombokit.emarkethamzanwadisupplier.Pesanan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import id.lombokit.emarkethamzanwadisupplier.Adapters.DaftarBarangPesananAdapter;
import id.lombokit.emarkethamzanwadisupplier.Adapters.ProductsPagerAdapter;
import id.lombokit.emarkethamzanwadisupplier.R;

public class ListBarangStatusBerbeda extends AppCompatActivity {
    private DaftarBarangPesananAdapter daftarBarangPesananAdapter;
    private TabLayout customTab;
    private ViewPager pager;

    String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_barang_status_berbeda);

        pager = (ViewPager)findViewById(R.id.pager);
        customTab=  findViewById(R.id.customTab);
        id_user = getIntent().getStringExtra("id_user");

        int parse = Integer.parseInt(id_user);



        customTab.addTab(customTab.newTab().setText("TERTUNDA"));
        customTab.addTab(customTab.newTab().setText("PROSES"));
        customTab.addTab(customTab.newTab().setText("SELESAI"));
        daftarBarangPesananAdapter = new DaftarBarangPesananAdapter(getSupportFragmentManager(),customTab.getTabCount(),parse);
        pager.setAdapter(daftarBarangPesananAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(customTab));
        pager.setOffscreenPageLimit(customTab.getTabCount());

        customTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }
}
