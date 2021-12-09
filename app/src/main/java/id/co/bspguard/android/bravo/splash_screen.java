package id.co.bspguard.android.bravo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import id.co.bspguard.android.bravo.AlertDialog.KAlertDialog;
import id.co.bspguard.android.bravo.accounts.Login;

import id.co.bspguard.android.bravo.contact.Contact;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.home.ActivityAwal;
import id.co.bspguard.android.bravo.R;


public class splash_screen extends AppCompatActivity {

        Fungsi fn = new Fungsi();
        TextView tvSplash, powered_by;
        ImageView logo;
        Animation atlogo, attext;
        LottieAnimationView lottie_image;
        String urlCheck = fn.url()+"check";
        VolleyObjectResult vor;
        VolleyObjectService vos;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            String datauser = fn.getdatalogin(splash_screen.this);

            //menghilangkan ActionBar
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_splash_screen);

          atlogo = AnimationUtils.loadAnimation(this, R.anim.atg);
          atlogo.setDuration(900);

          attext = AnimationUtils.loadAnimation(this, R.anim.atg);
          attext.setDuration(800);
          lottie_image = (LottieAnimationView) findViewById(R.id.lottie_image);

          logo = (ImageView) findViewById(R.id.logo_bsp);
          logo.setAnimation(atlogo);

          powered_by = (TextView) findViewById(R.id.powered_by);
          powered_by.setAnimation(attext);

          boolean connected = fn.isActiveNetwork(this);

          final Handler handler = new Handler();
          if(connected) {
            if (datauser != null) {

              vor = new VolleyObjectResult() {

                @Override
                public void resSuccess(String requestType, JSONObject response) {

                  try {

                    if (response.getBoolean("error")) {
                      Toast.makeText(getApplicationContext(), "Silahkan login kembali!", Toast.LENGTH_LONG);
                      String datauser = fn.getdatalogin(getApplicationContext());
                      if(datauser != null) {

                        final SharedPreferences sesdata = PreferenceManager
                          .getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor lds = sesdata.edit();
                        lds.clear();
                        lds.commit();

                      }
                      startActivity(new Intent(getApplicationContext(), Login.class));
                      finish();
                      return;
                    }else{
                      handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                          startActivity(new Intent(getApplicationContext(), MainActivity.class));
                          finish();
                        }
                      }, 6000L);
                    }

                  } catch (Exception e) {

                  }
                }

                @Override
                public void resError(String requestType, VolleyError error) {
                  new KAlertDialog(splash_screen.this, KAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Network connection timeout!")
                    .setConfirmText("OK")
                    .show();
                }
              };
              vos = new VolleyObjectService(vor, splash_screen.this);
              vos.getJsonObject("GETCALL", urlCheck);

            } else {
              handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                  startActivity(new Intent(getApplicationContext(), Login.class));
                  finish();
                }
              }, 3000L); //3000 L = 3 detik
            }
          }else{
            handler.postDelayed(new Runnable() {
              @Override
              public void run() {
                startActivity(new Intent(getApplicationContext(), ConnectionLost.class).putExtra("activity", "SplashScreen"));
                finish();
              }
            }, 3000L);
          }
          // ATTENTION: This was auto-generated to handle app links.
          Intent appLinkIntent = getIntent();
          String appLinkAction = appLinkIntent.getAction();
          Uri appLinkData = appLinkIntent.getData();
        }
    }

