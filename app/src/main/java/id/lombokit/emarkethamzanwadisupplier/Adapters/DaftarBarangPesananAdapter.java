package id.lombokit.emarkethamzanwadisupplier.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import id.lombokit.emarkethamzanwadisupplier.Fragments.MinumanListFragment;
import id.lombokit.emarkethamzanwadisupplier.Fragments.ObatanListFragment;
import id.lombokit.emarkethamzanwadisupplier.Fragments.SembakoListFragment;
import id.lombokit.emarkethamzanwadisupplier.Pesanan.DaftarBarangStatusProses;
import id.lombokit.emarkethamzanwadisupplier.Pesanan.DaftarBarangStatusSukses;
import id.lombokit.emarkethamzanwadisupplier.Pesanan.DaftarBarangStatusTunda;

public class DaftarBarangPesananAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    int mNumOfTabs;

    int id_user;


    public DaftarBarangPesananAdapter(FragmentManager fm, int NumOfTabs,int id_user) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.id_user = id_user;
    }


    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("id_user", String.valueOf(id_user));
        DaftarBarangStatusTunda daftarBarangStatusTunda = new DaftarBarangStatusTunda();
        DaftarBarangStatusProses daftarBarangStatusProses = new DaftarBarangStatusProses();
        DaftarBarangStatusSukses daftarBarangStatusSukses = new DaftarBarangStatusSukses();
        daftarBarangStatusTunda.setArguments(bundle );
        daftarBarangStatusProses.setArguments(bundle);
        daftarBarangStatusSukses.setArguments(bundle);
        mFragmentList.add(daftarBarangStatusTunda);
        mFragmentList.add(daftarBarangStatusProses);
        mFragmentList.add(daftarBarangStatusSukses);
        return mFragmentList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
