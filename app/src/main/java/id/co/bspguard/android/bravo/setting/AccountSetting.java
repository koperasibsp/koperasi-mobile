package id.co.bspguard.android.bravo.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.accounts.ResetEmail;
import id.co.bspguard.android.bravo.accounts.ResetPassword;

public class AccountSetting  extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    RelativeLayout change_email, change_password;
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_setting);

    change_email = (RelativeLayout) findViewById(R.id.changeEmail);
    change_password = (RelativeLayout) findViewById(R.id.changePassword);

    change_password.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ResetPassword.class);
        startActivity(intent);
        return;
      }
    });

    change_email.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ResetEmail.class);
        startActivity(intent);
        return;
      }
    });

  }
}
