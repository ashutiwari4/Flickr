package com.ashutosh.flicker.app;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.ashutosh.flicker.R;

/**
 * Created by Reetesh on 3/5/2017.
 */

public class BaseActivity extends AppCompatActivity {



    public void replaceFragment(int id, Fragment fragment) {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(id, fragment).addToBackStack(null).commit();
    }

    public Snackbar getSnackBar(int length, String msg, String actionMsg) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.cl_home), msg, length);
        return snackbar;

    }
}
