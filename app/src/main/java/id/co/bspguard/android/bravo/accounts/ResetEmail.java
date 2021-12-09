package id.co.bspguard.android.bravo.accounts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import id.co.bspguard.android.bravo.AlertDialog.KAlertDialog;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;

public class ResetEmail extends AppCompatActivity {
  Button reset_email;
  EditText input_email, input_password;
  VolleyObjectResult volleyObjectResult, vor = null;
  VolleyObjectService volleyObjectService, vos;
  Fungsi fungsi = new Fungsi();
  String url = fungsi.url()+"account-setting/email/update";
  String email, password;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reset_email);

    reset_email = (Button) findViewById(R.id.btnResetEmail);
    input_email = (EditText) findViewById(R.id.email);
    input_password = (EditText) findViewById(R.id.password);
    final String access_token = fungsi.getdatalogin(ResetEmail.this);


    reset_email.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(!validate()){
          new KAlertDialog(ResetEmail.this, KAlertDialog.WARNING_TYPE)
            .setTitleText("Oops...")
            .setContentText("Isian form reset email tidak valid!")
            .setConfirmText("OK")
            .show();
          reset_email.setEnabled(true);
        }

        email = input_email.getText().toString();
        password = input_password.getText().toString();

        HashMap<Object, Object> dt = new HashMap<Object, Object>();
        dt.put("Content-Type", "application/json; charset=UTF-8");
        dt.put("token", access_token);
        dt.put("email", email);
        dt.put("password", password);
        final JSONObject data = new JSONObject(dt);

        vor = new VolleyObjectResult() {
          @Override
          public void resSuccess(String requestType, JSONObject response) {
            try {

              boolean error = response.getBoolean("error");

              if(error){

                new KAlertDialog(ResetEmail.this, KAlertDialog.ERROR_TYPE)
                  .setTitleText("Oops...")
                  .setContentText(response.getString("message"))
                  .setConfirmText("OK")
                  .show();
                return;
              }

              new KAlertDialog(ResetEmail.this, KAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success...")
                .setContentText(response.getString("message"))
                .setConfirmText("OK")
                .show();
              return;
            } catch (Exception e) {
              e.printStackTrace();

            }
          }
          @Override
          public void resError(String requestType, VolleyError error) {
            View view = findViewById(R.id.coordinator);
            String message = "Maaf terjadi kesalahan.";
            fungsi.showSnackbar(view, message);
          }
        };
        vos = new VolleyObjectService(vor, ResetEmail.this);
        vos.postJsonObject("POSTCALL", url, data);
      }
    });
  }

  public boolean validate() {
    boolean valid = true;

    String email = input_email.getText().toString();
    String password = input_password.getText().toString();

    if (email.isEmpty()) {
      input_email.setError("Email lama wajib diisi");
      valid = false;
    }

    if (password.isEmpty()) {
      input_password.setError("Password wajib diisi");
      valid = false;
    }

    return valid;
  }

  public void onBackPressed()
  {
    super.onBackPressed();
    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
  }

}
