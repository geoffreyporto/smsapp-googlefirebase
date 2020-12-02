package com.daa.jsmsapp.utils;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.daa.jsmsapp.R;

/*
    Refatoring 3
    Cumple con la (S), del principio de SOLID de buenas prácticas de desarrollo a nivel clases
*/

public class Fragmento {
    private Fragment fragment;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;

    //TABLA DE ERRORES DEE FRAGMENTOS
    private static final String ERROR_FRAGM_1000 = "1000";
    private static final String ERROR_FRAGM_1001 = "1001";
    private static final String ERROR_FRAGM_1002 = "1002";

    public Fragmento(Fragment fragment, FragmentManager fragmentManager) {
        this.fragment = fragment;
        this.fragmentManager = fragmentManager;
    }

    public void cargarFragmento() {
        try {
            transaction = fragmentManager.beginTransaction();
            //transaction.replace(R.id.framePrincipal,fragment);
            transaction.addToBackStack(fragment.toString());
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commit();
        } catch (Exception e) {
            Log.i(ERROR_FRAGM_1000,e.getMessage());
        }
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }


    public FragmentTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(FragmentTransaction transaction) {
        this.transaction = transaction;
    }

}
