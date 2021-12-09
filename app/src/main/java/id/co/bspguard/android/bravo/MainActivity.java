package id.co.bspguard.android.bravo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.onesignal.OneSignal;

import id.co.bspguard.android.bravo.accounts.Login;
import id.co.bspguard.android.bravo.functions.AlphaForeGroundColorSpan;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.ScrollViewHelper;
import id.co.bspguard.android.bravo.home.ActivityAwal;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bnv;
    Fragment selectedFragment = null;
    ActionBar actionBar;

    Fungsi fn = new Fungsi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        OneSignal.startInit(this)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init();


        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler(getApplication()))
                .init();


        setContentView(R.layout.activity_main);

        String datauser = fn.getdatalogin(MainActivity.this);
        if(datauser == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }


//        actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#029a48")));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ActivityAwal.newInstance());
        transaction.commit();
    }




}
