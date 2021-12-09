package id.co.bspguard.android.bravo.accounts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import id.co.bspguard.android.bravo.R;

public class ForgotPassword extends AppCompatActivity {
    TextView _linkSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

      _linkSignup = (TextView) findViewById(R.id.link_signup);
      _linkSignup.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(getApplicationContext(), Signup.class);
          startActivity(intent);
        }
      });
    }

  public void onBackPressed()
  {
    // code here to show dialog
    super.onBackPressed();  // optional depending on your needs
    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
  }
}
