package id.co.bspguard.android.bravo.memberloan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import id.co.bspguard.android.bravo.AlertDialog.KAlertDialog;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.calculator.Calculator;
import id.co.bspguard.android.bravo.contact.Contact;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.home.ActivityAwal;
import id.co.bspguard.android.bravo.loan.FormLoan;
import id.co.bspguard.android.bravo.loan.LoanDataSet;

public class FormChangeDeposit extends AppCompatActivity {
  Fungsi fn = new Fungsi();
  EditText nominalWajib, nominalSukarela;
  TextView valSukarela, valWajib, valPokok, valLainnya;
  Button ajukanPerubahan;
  String urlRincianSimpanan = fn.url()+"rincian-simpanan";
  String urlAjukanPerubahanSimpanan = fn.url()+"ajukan-perubahan-simpanan";
  VolleyObjectResult vor, volleyObjectResult;
  VolleyObjectService vos, volleyObjectService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_form_change_deposit);
    final String access_token = fn.getdatalogin(FormChangeDeposit.this);

    nominalSukarela = (EditText) findViewById(R.id.simpananSukarela);
    nominalWajib = (EditText) findViewById(R.id.simpananWajib);
    ajukanPerubahan = (Button) findViewById(R.id.ajukanPerubahanSimpanan);
    valSukarela = (TextView) findViewById(R.id.valSukarela);
    valWajib = (TextView) findViewById(R.id.valWajib);

    vor = new VolleyObjectResult() {

      @Override
      public void resSuccess(String requestType, JSONObject response) {
        try {
          JSONObject jsonObject = response.getJSONObject("data");
          valWajib.setText(fn.formatIntRupiah(jsonObject.getString("wajib")));
          valSukarela.setText(fn.formatIntRupiah(jsonObject.getString("sukarela")));

          RincianSimpananDataSet rds = new RincianSimpananDataSet();
          rds.setWajib(jsonObject.getString("wajib"));
          rds.setSukarela(jsonObject.getString("sukarela"));

        } catch (JSONException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void resError(String requestType, VolleyError error) {
        new KAlertDialog(FormChangeDeposit.this, KAlertDialog.WARNING_TYPE)
          .setTitleText("Oops...")
          .setContentText("Network connection problem")
          .setConfirmText("OK")
          .show();
      }
    };
    vos = new VolleyObjectService(vor, getApplicationContext());
    vos.getJsonObject("GETCALL", urlRincianSimpanan);





    ajukanPerubahan.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final ACProgressFlower dialog = new ACProgressFlower.Builder(FormChangeDeposit.this)
          .direction(ACProgressConstant.DIRECT_CLOCKWISE)
          .themeColor(Color.WHITE)
          .text("Loading ...")
          .fadeColor(Color.DKGRAY).build();
        dialog.show();

        RincianSimpananDataSet rds = new RincianSimpananDataSet();
        HashMap dt = new HashMap();
        dt.put("token", access_token);
        dt.put("wajib", nominalWajib.getText());
        dt.put("sukarela", nominalSukarela.getText());
        dt.put("last_wajib", rds.getWajib());
        dt.put("last_sukarela", rds.getSukarela());
        final JSONObject data = new JSONObject(dt);
        volleyObjectResult = new VolleyObjectResult() {

          @Override
          public void resSuccess(String requestType, JSONObject response) {
            try {

              if(response.getBoolean("error")){
                new KAlertDialog(FormChangeDeposit.this, KAlertDialog.ERROR_TYPE)
                  .setTitleText("Oops...")
                  .setContentText(response.getString("message"))
                  .setConfirmText("OK")
                  .show();
                dialog.dismiss();
                return;
              }
              nominalWajib.setText("");
              nominalSukarela.setText("");
              new KAlertDialog(FormChangeDeposit.this, KAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success...")
                .setContentText("Terima kasih, perubahan simpanan telah dikirim")
                .setConfirmText("OK")
                .show();

              dialog.dismiss();
              Intent intent = new Intent(FormChangeDeposit.this, ActivityAwal.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);

            } catch (Exception e) {
              dialog.dismiss();
            }
          }

          @Override
          public void resError(String requestType, VolleyError error) {
            new KAlertDialog(FormChangeDeposit.this, KAlertDialog.WARNING_TYPE)
              .setTitleText("Oops...")
              .setContentText("Network connection problem")
              .setConfirmText("OK")
              .show();
          }
        };
        volleyObjectService = new VolleyObjectService(volleyObjectResult, getApplicationContext());
        volleyObjectService.postJsonObject("POSTCALL", urlAjukanPerubahanSimpanan, data);
      }
    });


  }
}
