package id.co.bspguard.android.bravo.loan;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.github.clans.fab.FloatingActionButton;
import com.yarolegovich.lovelydialog.LovelySaveStateHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.home.ActivityAwal;

public class ApprovalLoan extends AppCompatActivity {
  Fungsi fn = new Fungsi();
  RecyclerView listLoan;
  FloatingActionButton filter, ajukan;
  VolleyObjectResult vor;
  VolleyObjectService vos;
  JSONObject data = null;
  String url = fn.url() + "approval-loan";

  String token;
  private List<ApprovalLoanDataSet> list = new ArrayList<ApprovalLoanDataSet>();
  private ApprovalLoanAdapter loanAdapter;
  ActionBar actionBar;
  ListView listApprovalLoan;

  private ProgressBar progressBar;
  private LovelySaveStateHandler saveStateHandler;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_approval_loan);

    listApprovalLoan = (ListView) findViewById(R.id.listApprovalLoan);

    vor = new VolleyObjectResult() {
      @Override
      public void resSuccess(String requestType, JSONObject response) {

        progressBar = (ProgressBar) findViewById(R.id.proBar);
        try {
          progressBar.setVisibility(View.VISIBLE);
          progressBar.setVisibility(View.GONE);
          JSONArray jsonArray = response.getJSONArray("data");
          int id_count = 0;
          list.clear();
          if(jsonArray.length() > id_count) {
            for (int i = 0; i < jsonArray.length(); i++) {
              JSONObject jsonObject = jsonArray.getJSONObject(i);
              JSONObject tsLoans = jsonObject.getJSONObject("ts_loans");
              JSONObject member = tsLoans.getJSONObject("member");
              JSONObject msLoans = tsLoans.getJSONObject("ms_loans");


              ApprovalLoanDataSet data = new ApprovalLoanDataSet();
              data.setId(jsonObject.getString("id"));
              data.setName(member.getString("full_name"));
              data.setValue(tsLoans.getString("value"));
              data.setLoan_number(tsLoans.getString("loan_number"));
              data.setLoan_name(msLoans.getString("loan_name"));

              list.add(data);
            }
            loanAdapter = new ApprovalLoanAdapter(ApprovalLoan.this, list);
            loanAdapter.notifyDataSetChanged();
            listApprovalLoan.setAdapter(loanAdapter);
          }else {
            Thread.sleep(2000);
            Toast.makeText(ApprovalLoan.this, "Anda belum memiliki data approval", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ApprovalLoan.this, ActivityAwal.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      @Override
      public void resError(String requestType, VolleyError error) {
//        startActivity(new Intent(getApplicationContext(), ConnectionLost.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
      }
    };
    vos = new VolleyObjectService(vor, ApprovalLoan.this);
    vos.getJsonObject("GETCALL", url);

  }
}
