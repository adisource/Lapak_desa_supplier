package id.lombokit.emarkethamzanwadisupplier.Pesanan;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.lombokit.emarkethamzanwadisupplier.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DaftarPesanan extends Fragment {


    public DaftarPesanan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daftar_pesanan, container, false);
    }

}
