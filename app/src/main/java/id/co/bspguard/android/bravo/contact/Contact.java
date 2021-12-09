package id.co.bspguard.android.bravo.contact;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import id.co.bspguard.android.bravo.AlertDialog.KAlertDialog;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.home.ActivityAwal;
import id.co.bspguard.android.bravo.loan.FormLoan;
import id.co.bspguard.android.bravo.memberloan.ListLoan;

public class Contact extends AppCompatActivity {

  Fungsi fn = new Fungsi();
  EditText judul, pesan;
  Button kirim;
  String urlKirimPesan = fn.url()+"post-contact";
  String isiJudul, isiPesan;
  VolleyObjectResult vor;
  VolleyObjectService vos;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_contact);

      judul = (EditText) findViewById(R.id.input_judul);
      pesan = (EditText) findViewById(R.id.input_pesan);
      kirim = (Button) findViewById(R.id.btn_kirim);


      kirim.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (!validate()) {
            invalid();
            return;
          }

          HashMap dt = new HashMap();
          dt.put("judul", judul.getText().toString());
          dt.put("pesan", pesan.getText().toString());
          final JSONObject data = new JSONObject(dt);

          final ACProgressFlower dialog = new ACProgressFlower.Builder(Contact.this)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .text("Loading ...")
            .fadeColor(Color.DKGRAY).build();
          dialog.show();

          vor = new VolleyObjectResult() {

            @Override
            public void resSuccess(String requestType, JSONObject response) {

              try {

                if(response.getBoolean("error")){
                  new KAlertDialog(Contact.this, KAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText(response.getString("message"))
                    .setConfirmText("OK")
                    .show();
                  dialog.dismiss();
                  return;
                }
                judul.setText("");
                pesan.setText("");
                new KAlertDialog(Contact.this, KAlertDialog.SUCCESS_TYPE)
                  .setTitleText("Success...")
                  .setContentText("Terima kasih, pesan anda telah terkirim")
                  .setConfirmText("OK")
                  .show();

                dialog.dismiss();
                Intent intent = new Intent(Contact.this, ActivityAwal.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

              } catch (Exception e) {
                dialog.dismiss();
              }
            }

            @Override
            public void resError(String requestType, VolleyError error) {
              new KAlertDialog(Contact.this, KAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Network connection timeout!")
                .setConfirmText("OK")
                .show();
              dialog.dismiss();

            }
          };
          vos = new VolleyObjectService(vor, Contact.this);
          vos.postJsonObject("POSTCALL", urlKirimPesan, data);
        }
      });

  }

  public void onBackPressed()
  {
      // code here to show dialog
      super.onBackPressed();  // optional depending on your needs
      overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
  }

  public void invalid() {
    new KAlertDialog(Contact.this, KAlertDialog.WARNING_TYPE)
      .setTitleText("Oops...")
      .setContentText("Pastikan semua form diisi!")
      .setConfirmText("OK")
      .show();
      kirim.setEnabled(true);
  }

  public boolean validate() {
    boolean valid = true;

    isiJudul = judul.getText().toString();
    isiPesan = pesan.getText().toString();

    if (isiJudul.isEmpty()) {
      judul.setError("Judul wajib diisi!");
      valid = false;
    } else {
      judul.setError(null);
    }

    if (isiPesan.isEmpty()) {
      pesan.setError("Pesan wajib diisi!");
      valid = false;
    } else {
      pesan.setError(null);
    }

    return valid;

  }
}
