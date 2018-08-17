package com.example.nguyen.tokentesst;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;

import com.example.nguyen.tokentesst.Fragment.FourFragment;
import com.example.nguyen.tokentesst.Fragment.InfoFragment;
import com.example.nguyen.tokentesst.Fragment.OneFragment;
import com.example.nguyen.tokentesst.Fragment.ThreeFragment;
import com.example.nguyen.tokentesst.Fragment.TwoFragment;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {


    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        FirebaseMessaging.getInstance().subscribeToTopic("NTB");
    }

    private void Anhxa() {
        int[] icons = {R.drawable.ic_dhnew1,
                R.drawable.ic_dhnew,
                R.drawable.ic_hoanthanh,
                R.drawable.ic_user
        };

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < icons.length; i++) {
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }
        tabLayout.getTabAt(0).select();
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.insertNewFragment(new OneFragment());
        adapter.insertNewFragment(new TwoFragment());
        adapter.insertNewFragment(new ThreeFragment());
        adapter.insertNewFragment(new FourFragment());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, MainLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();

        super.onBackPressed();
    }


    public void logout(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(this, MainLoginActivity.class));
        this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        this.finish();
    }

    public void info(View view){
        startActivity(new Intent(this, InfoFragment.class));
    }

    public void changepass(View view){
        startActivity(new Intent(this, ChangePassword.class));
    }

}

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void insertNewFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

    }



