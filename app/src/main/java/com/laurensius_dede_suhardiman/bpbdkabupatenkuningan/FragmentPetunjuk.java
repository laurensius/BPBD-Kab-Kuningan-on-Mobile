package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentPetunjuk extends Fragment {

    public FragmentPetunjuk() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterPetunjuk = inflater.inflate(R.layout.fragment_petunjuk, container, false);
        return inflaterPetunjuk;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

    }


}
