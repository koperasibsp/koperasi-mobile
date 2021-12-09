package id.co.bspguard.android.bravo.bankmember;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.yarolegovich.lovelydialog.LovelySaveStateHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import id.co.bspguard.android.bravo.ConnectionLost;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.home.ActivityAwal;

public class BankMember extends AppCompatActivity {

  Fungsi fn = new Fungsi();
  TextView nama_bank, nama_akun_bank, nomor_akun_bank, cabang_bank;
  Button edit_bank;
  VolleyObjectResult vor;
  VolleyObjectService vos;
  JSONObject data = null;
  String url = fn.url() + "my-bank";

  String token;
  ActionBar actionBar;

  private ProgressBar progressBar;
  private LovelySaveStateHandler saveStateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_member);

        boolean connected = fn.isActiveNetwork(getApplication());
        if(!connected){
          startActivity(new Intent(getApplication(), ConnectionLost.class));
        }

        nama_akun_bank = (TextView) findViewById(R.id.valueNamaAkun);
        nama_bank = (TextView) findViewById(R.id.valueNamaBank);
        nomor_akun_bank = (TextView) findViewById(R.id.valueNomorAkun);
        cabang_bank = (TextView) findViewById(R.id.valueCabang);

      vor = new VolleyObjectResult() {
        @Override
        public void resSuccess(String requestType, JSONObject response) {

          try {
            JSONObject data = response.getJSONObject("data");
//            Toast.makeText(BankMember.this, response.toString(), Toast.LENGTH_LONG).show();

            if(!response.getBoolean("error")){

              nama_akun_bank.setText(data.getString("bank_account_name"));
              nama_bank.setText(data.getString("bank_name"));
              nomor_akun_bank.setText(data.getString("bank_account_number"));
              if(data.getString("bank_branch") == "null"){
                cabang_bank.setText("not set");
              }else {
                cabang_bank.setText(data.getString("bank_branch"));
              }

            }else {
              Thread.sleep(2000);
              Toast.makeText(BankMember.this, "Anda belum memiliki data bank", Toast.LENGTH_LONG).show();
              Intent intent = new Intent(BankMember.this, ActivityAwal.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);
            }

          } catch (Exception e) {
            Toast.makeText(BankMember.this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
          }
        }
        @Override
        public void resError(String requestType, VolleyError error) {
//          Toast.makeText(BankMember.this, error.toString(), Toast.LENGTH_LONG).show();
        }
      };
      vos = new VolleyObjectService(vor, BankMember.this);
      vos.getJsonObject("GETCALL", url);
    }

    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
