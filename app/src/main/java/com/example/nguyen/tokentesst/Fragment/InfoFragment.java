package com.example.nguyen.tokentesst.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.nguyen.tokentesst.MainLoginActivity;
import com.example.nguyen.tokentesst.R;


public class InfoFragment extends Activity {



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info);


    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InfoFragment.this, MainLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
        super.onBackPressed();
    }


}