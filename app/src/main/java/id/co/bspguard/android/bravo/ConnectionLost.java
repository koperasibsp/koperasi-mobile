package id.co.bspguard.android.bravo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import id.co.bspguard.android.bravo.accounts.Login;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.home.ActivityAwal;

public class ConnectionLost extends AppCompatActivity {

  Fungsi fn = new Fungsi();
  Button home;
  boolean connected;
  String activity;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    String datauser = fn.getdatalogin(ConnectionLost.this);

    //menghilangkan ActionBar
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_connection_lost);

    home =(Button) findViewById(R.id.home);
    connected = fn.isActiveNetwork(this);
    final Handler handler = new Handler();

    final ACProgressFlower dialog = new ACProgressFlower.Builder(ConnectionLost.this)
      .direction(ACProgressConstant.DIRECT_CLOCKWISE)
      .themeColor(Color.WHITE)
      .text("Loading...")
      .fadeColor(Color.DKGRAY).build();

    home.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          dialog.show();
            handler.postDelayed(new Runnable() {
              @Override
              public void run() {
               onBackPressed();
                dialog.dismiss();

              }
            }, 3000L); //3000 L = 3 detik
      }
    });
  }


}

