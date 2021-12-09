package id.co.bspguard.android.bravo.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;

import id.co.bspguard.android.bravo.AlertDialog.KAlertDialog;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.calculator.Calculator;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;

public class ResetPassword extends AppCompatActivity {
  Button btnReset;
  EditText passwordLama, passwordBaru, konfirmasiPasswordBaru;
  VolleyObjectResult volleyObjectResult, vor = null;
  VolleyObjectService volleyObjectService, vos;
  Fungsi fungsi = new Fungsi();
  String url = fungsi.url()+"account-setting/password/update";
  String old_password, new_password, confirm_password;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reset_password);

    passwordLama = (EditText) findViewById(R.id.passwordLama);
    passwordBaru = (EditText) findViewById(R.id.passwordBaru);
    konfirmasiPasswordBaru = (EditText) findViewById(R.id.confirmpasswordBaru);
    btnReset = (Button) findViewById(R.id.btnResetPassword);
    final String access_token = fungsi.getdatalogin(ResetPassword.this);
    btnReset.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(!validate()){
          new KAlertDialog(ResetPassword.this, KAlertDialog.WARNING_TYPE)
            .setTitleText("Oops...")
            .setContentText("Isian form reset password tidak valid!")
            .setConfirmText("OK")
            .show();
          btnReset.setEnabled(true);
        }

        old_password = passwordLama.getText().toString();
        new_password = passwordBaru.getText().toString();
        confirm_password = konfirmasiPasswordBaru.getText().toString();

        HashMap<Object, Object> dt = new HashMap<Object, Object>();
        dt.put("Content-Type", "application/json; charset=UTF-8");
        dt.put("token", access_token);
        dt.put("old_password", old_password);
        dt.put("new_password", new_password);
        dt.put("confirm_password", confirm_password);

        final JSONObject data = new JSONObject(dt);

        vor = new VolleyObjectResult() {
          @Override
          public void resSuccess(String requestType, JSONObject response) {
            try {

              boolean error = response.getBoolean("error");

              if(error){

                new KAlertDialog(ResetPassword.this, KAlertDialog.ERROR_TYPE)
                  .setTitleText("Oops...")
                  .setContentText(response.getString("message"))
                  .setConfirmText("OK")
                  .show();
                return;
              }

              new KAlertDialog(ResetPassword.this, KAlertDialog.SUCCESS_TYPE)
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
        vos = new VolleyObjectService(vor, ResetPassword.this);
        vos.postJsonObject("POSTCALL", url, data);
      }
    });

  }

  public boolean validate() {
    boolean valid = true;

    String oldPass = passwordLama.getText().toString();
    String newPass = passwordBaru.getText().toString();
    String confirmNewPass = konfirmasiPasswordBaru.getText().toString();

    if (oldPass.isEmpty() || oldPass.length() < 6) {
      passwordLama.setError("Password lama wajib diisi minimal 6 karakter");
      valid = false;
    }

    if (!confirmNewPass.equals(newPass)) {
      konfirmasiPasswordBaru.setError("Konfirmasi password baru tidak sama dengan password baru");
      valid = false;
    }

    if (newPass.isEmpty() || newPass.length() < 6) {
      passwordBaru.setError("Password baru wajib diisi minimal 6 karakter");
      valid = false;
    }

    if (newPass.isEmpty() || newPass.length() < 6) {
      konfirmasiPasswordBaru.setError("Konfirmasi password baru wajib diisi minimal 6 karakter");
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
