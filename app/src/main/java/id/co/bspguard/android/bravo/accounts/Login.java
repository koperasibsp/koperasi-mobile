package id.co.bspguard.android.bravo.accounts;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import id.co.bspguard.android.bravo.AlertDialog.KAlertDialog;
import id.co.bspguard.android.bravo.MainActivity;
import id.co.bspguard.android.bravo.MyNotificationOpenedHandler;
import id.co.bspguard.android.bravo.R;
import android.os.Bundle;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.onesignal.OneSignal;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.memberretrievedeposit.MainRetrieveDeposit;

public class Login extends AppCompatActivity {


    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
  private static final int FORGOT_PASSWORD = 0;

    Fungsi fn = new Fungsi();

    Button _loginButton, _signupButton;
    EditText _inputPassword;
    AutoCompleteTextView _inputEmail;
    TextView _linkForgot;
    VolleyObjectResult vor;
    VolleyObjectService vos;
    JSONObject data = null;
    String token, uid;
    ActionBar actionBar;
    private ProgressBar progressBar;
    final String url = fn.url()+"auth/login";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _inputPassword = (EditText) findViewById(R.id.inputPassword);
        _inputEmail = (AutoCompleteTextView) findViewById(R.id.inputEmail);
        _linkForgot = (TextView) findViewById(R.id.link_forgot);
//        _signupButton = (Button) findViewById(R.id.btn_signup) ;

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

//      _signupButton.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//          Intent intent = new Intent(getApplicationContext(), Signup.class);
//          startActivityForResult(intent, FORGOT_PASSWORD);
//        }
//      });


      _linkForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

      OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

      // OneSignal Initialization\

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler(getApplication()))
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {

                SharedPreferences token = PreferenceManager
                        .getDefaultSharedPreferences(Login.this);
                SharedPreferences.Editor os = token.edit();

                os.putString("os_token", userId);
                os.commit();

                if (registrationId != null)
                    Log.d("debug", "registrationId:" + registrationId);

            }
        });


    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        _loginButton.setEnabled(false);


        uid = fn.getDeviceUniqueID(Login.this);
        String email = _inputEmail.getText().toString();
        String password = _inputPassword.getText().toString();

        SharedPreferences token = PreferenceManager
          .getDefaultSharedPreferences(Login.this);

        HashMap<String, String> dt = new HashMap<String, String>();
        dt.put("uid", uid);
        dt.put("email", email);
        dt.put("password", password);
        dt.put("os_token", token.getString("os_token", ""));
        final JSONObject data = new JSONObject(dt);

        final ACProgressFlower dialog = new ACProgressFlower.Builder(Login.this)
          .direction(ACProgressConstant.DIRECT_CLOCKWISE)
          .themeColor(Color.WHITE)
          .text("Loading ...")
          .fadeColor(Color.DKGRAY).build();
        dialog.show();
        vor = new VolleyObjectResult() {

          @Override
          public void resSuccess(String requestType, JSONObject response) {

            try {
              SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(Login.this);
              SharedPreferences.Editor lds = prefs.edit();

              JSONArray permissionJson = response.getJSONArray("permission");
              lds.putString("email", response.getString("email"));
              lds.putString("access_token", response.getString("access_token"));
              lds.putString("permission", permissionJson.toString());
              lds.putString("id", response.get("id").toString());
//
              lds.commit();
              onLoginSuccess();
              dialog.dismiss();
//                                // onLoginFailed();
              Intent inten = new Intent(getApplicationContext(), MainActivity.class);
              startActivity(inten);

            } catch (Exception e) {
              new KAlertDialog(Login.this, KAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Data akun tidak ditemukan")
                .setConfirmText("OK")
                .show();
              dialog.dismiss();
              onLoginFailed();
            }
          }

          @Override
          public void resError(String requestType, VolleyError error) {
//                      Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();
//                      Log.d("debug",error.toString());
            dialog.dismiss();
            onLoginFailed();

          }
        };
        vos = new VolleyObjectService(vor, Login.this);
        vos.postJsonObject("POSTCALL", url, data);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (requestCode == REQUEST_SIGNUP || requestCode == FORGOT_PASSWORD) {
        if (resultCode == RESULT_OK) {

          // TODO: Implement successful signup logic here
          // By default we just finish the Activity and log them in automatically
          this.finish();
        }
      }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {

        _inputPassword.setError("please check password");
        _inputEmail.setError("please check email address");

      new KAlertDialog(Login.this, KAlertDialog.WARNING_TYPE)
        .setTitleText("Oops...")
        .setContentText("Isian email atau password salah!")
        .setConfirmText("OK")
        .show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _inputEmail.getText().toString();
        String password = _inputPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _inputEmail.setError("enter a valid email address");
            valid = false;
        } else {
            _inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            _inputPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _inputPassword.setError(null);
        }

        return valid;
    }

}
