package id.co.bspguard.android.bravo.memberresign;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

import id.co.bspguard.android.bravo.AlertDialog.KAlertDialog;
import id.co.bspguard.android.bravo.ConnectionLost;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.loan.FormLoan;
import id.co.bspguard.android.bravo.memberdeposit.DetailDeposit;

public class MainResign extends AppCompatActivity {
  Button kirim;
  EditText tanggal, alasan;
  CheckBox checkBox;
  TextView linkSla;
  int mDay, mMonth, mYear;

  VolleyObjectResult volleyObjectResult, vor = null;
  VolleyObjectService volleyObjectService, vos;
  Fungsi fungsi = new Fungsi();
  String urlPostResign = fungsi.url()+"post-resign";
  String requestPolicyRegister = fungsi.url()+"policy/3";
  JSONObject data = null;
  View rootview;
  private ShimmerFrameLayout shimmer;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_member_main_resign);

    boolean connected = fungsi.isActiveNetwork(getApplication());
    if(!connected){
      startActivity(new Intent(getApplication(), ConnectionLost.class));
    }

    View rootview = findViewById(android.R.id.content);
    kirim = (Button) findViewById(R.id.kirim);
    tanggal = (EditText) findViewById(R.id.tanggal);
    alasan = (EditText) findViewById(R.id.alasan);
    checkBox = (CheckBox) findViewById(R.id.checkbox_sla);
    linkSla = (TextView) findViewById(R.id.linkSLA);

    tanggal.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Calendar mcurrentDate = Calendar.getInstance();

        mDay   = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mYear  = mcurrentDate.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainResign.this, new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            month = month + 1;
            String mes = month/10==0?("0"+month): String.valueOf(month);
            String date = year + "-" + mes + "-" + dayOfMonth;
            tanggal.setText(date);
          }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
      }
    });

    kirim.setEnabled(false);
    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
      {
        if ( isChecked )
        {
          kirim.setEnabled(true);
        }
        else{
          kirim.setEnabled(false);
        }
      }
    });

    linkSla.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.d("namepolicy", "clicked");
        showPopup();
      }
    });


    kirim.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!validate()) {
          new KAlertDialog(MainResign.this, KAlertDialog.WARNING_TYPE)
            .setTitleText("Oops...")
            .setContentText("Isian form tidak valid")
            .setConfirmText("OK")
            .show();
          return;
        }
        String date = tanggal.getText().toString();
        String reason = alasan.getText().toString();
        HashMap<String, String> dt = new HashMap<String, String>();
        dt.put("date", date);
        dt.put("reason", reason);
        final JSONObject data = new JSONObject(dt);

        volleyObjectResult = new VolleyObjectResult() {
          @Override
          public void resSuccess(String requestType, JSONObject response) {

            try {
              boolean error = response.getBoolean("error");
              if(error){

                new KAlertDialog(MainResign.this, KAlertDialog.ERROR_TYPE)
                  .setTitleText("Oops...")
                  .setContentText(response.getString("message"))
                  .setConfirmText("OK")
                  .show();
                return;
              }

              new KAlertDialog(MainResign.this, KAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success...")
                .setContentText(response.getString("message"))
                .setConfirmText("OK")
                .show();
              tanggal.setText("");
              alasan.setText("");
              return;

            } catch (JSONException e) {
              e.printStackTrace();
            }


          }
          @Override
          public void resError(String requestType, VolleyError error) {
            new KAlertDialog(MainResign.this, KAlertDialog.WARNING_TYPE)
              .setTitleText("Oops...")
              .setContentText("Network connection problem")
              .setConfirmText("OK")
              .show();
          }
        };

        volleyObjectService = new VolleyObjectService(volleyObjectResult, MainResign.this);
        volleyObjectService.postJsonObject("POSTCALL", urlPostResign, data);
      }
    });


  }

  private PopupWindow pw;
  private void showPopup() {
    volleyObjectResult = new VolleyObjectResult() {
      @Override
      public void resSuccess(String requestType, JSONObject response) {

        try {
          JSONObject jsonarray = response.getJSONObject("data");


          LayoutInflater inflater = (LayoutInflater)
            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          View layout = inflater.inflate(R.layout.popup_sla_register,
            (ViewGroup) findViewById(R.id.popup_1));
          pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,true);
          pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
          WebView sla = (WebView) layout.findViewById(R.id.txtView);
          sla.setBackgroundColor(Color.TRANSPARENT);
          sla.loadDataWithBaseURL(null, jsonarray.getString("description"), "text/html", "utf-8", null);
          Button agree = (Button) layout.findViewById(R.id.agree);
          Button dontagree = (Button) layout.findViewById(R.id.dont_agree);
          ShimmerFrameLayout shimmerNews = layout.findViewById(R.id.shimmer);
          shimmerNews.stopShimmer();
          shimmerNews.setVisibility(View.GONE);
          agree.setOnClickListener(agree_button);
          dontagree.setOnClickListener(dontagree_button);
        } catch (JSONException e) {
          e.printStackTrace();
        }


      }
      @Override
      public void resError(String requestType, VolleyError error) {
        new KAlertDialog(MainResign.this, KAlertDialog.WARNING_TYPE)
          .setTitleText("Oops...")
          .setContentText("Network connection problem")
          .setConfirmText("OK")
          .show();
      }
    };

    volleyObjectService = new VolleyObjectService(volleyObjectResult, MainResign.this);
    volleyObjectService.getJsonObject("GETCALL", requestPolicyRegister);

  }

  private View.OnClickListener agree_button = new View.OnClickListener() {
    public void onClick(View v) {
      checkBox = (CheckBox) findViewById(R.id.checkbox_sla);
      checkBox.setChecked(true);
      pw.dismiss();
    }
  };

  private View.OnClickListener dontagree_button = new View.OnClickListener() {
    public void onClick(View v) {
      checkBox = (CheckBox) findViewById(R.id.checkbox_sla);
      checkBox.setChecked(false);
      pw.dismiss();
    }
  };

  public boolean validate() {
    boolean valid = true;

    String date = tanggal.getText().toString();
    String reason = alasan.getText().toString();

    if (reason.isEmpty() || reason.length() < 5) {
      alasan.setError("minimal 5 karakter");
      valid = false;
    } else {
      alasan.setError(null);
    }

    if (date.isEmpty()) {
      tanggal.setError("tanggal tidak boleh kosong");
      valid = false;
    } else {
      tanggal.setError(null);
    }
    return valid;
  }

  public void onBackPressed()
  {
    // code here to show dialog
    super.onBackPressed();  // optional depending on your needs
    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
  }

}
