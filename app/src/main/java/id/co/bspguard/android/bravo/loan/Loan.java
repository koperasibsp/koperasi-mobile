package id.co.bspguard.android.bravo.loan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.github.clans.fab.FloatingActionButton;
import com.yarolegovich.lovelydialog.LovelySaveStateHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.bspguard.android.bravo.ConnectionLost;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.home.ActivityAwal;

public class Loan extends AppCompatActivity {
  Fungsi fn = new Fungsi();

//  ListView listLoan;
//  GridView gridLoan;
  RecyclerView listLoan;
  FloatingActionButton filter, ajukan;
  VolleyObjectResult vor;
  VolleyObjectService vos;
  JSONObject data = null;
  String url = fn.url() + "loan";

  String token;
  private List<LoanDataSet> list = new ArrayList<LoanDataSet>();
  private LoanAdapter loanAdapter;
  ActionBar actionBar;

  private ProgressBar progressBar;
  private LovelySaveStateHandler saveStateHandler;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loan);

    boolean connected = fn.isActiveNetwork(getApplication());
    if(!connected){
      startActivity(new Intent(getApplication(), ConnectionLost.class));
    }

    listLoan = (RecyclerView) findViewById(R.id.recyclerview_id);

    vor = new VolleyObjectResult() {
      @Override
      public void resSuccess(String requestType, JSONObject response) {

//        Toast.makeText(Loan.this, response.toString(), Toast.LENGTH_LONG).show();
        try {
          JSONArray jsonArray = response.getJSONArray("data");
          int id_count = 0;
          list.clear();
          if(jsonArray.length() > id_count) {
            for (int i = 0; i < jsonArray.length(); i++) {
              JSONObject jsonObject = jsonArray.getJSONObject(i);
              LoanDataSet data = new LoanDataSet();
              data.setId(jsonObject.getString("id"));
              data.setLogo(jsonObject.getString("logo"));
              data.setLoan_name(jsonObject.getString("loan_name"));
              data.setPlafon(jsonObject.getString("plafon"));
              data.setDescription(jsonObject.getString("description"));
              data.setRate_of_interest(jsonObject.getString("rate_of_interest"));
              data.setTenor(jsonObject.getString("tenor"));
              data.setBatas_pinjaman(jsonObject.getInt("plafon"));
              data.setBiaya_admin(jsonObject.getInt("biaya_admin"));
              data.setBunga(jsonObject.getDouble("rate_of_interest"));
              data.setBiaya_transfer(jsonObject.getInt("biaya_transfer"));
              data.setBunga_berjalan(jsonObject.getDouble("biaya_bunga_berjalan"));
              data.setProvisi(jsonObject.getDouble("provisi"));

              list.add(data);
            }
            loanAdapter = new LoanAdapter(Loan.this, list);
            loanAdapter.notifyDataSetChanged();
            listLoan.setLayoutManager(new GridLayoutManager(Loan.this,3));
            listLoan.setAdapter(loanAdapter);
          }else {
            Thread.sleep(2000);
            Toast.makeText(Loan.this, "Pinjaman belum tersedia", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Loan.this, ActivityAwal.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      @Override
      public void resError(String requestType, VolleyError error) {
//        Toast.makeText(Loan.this, error.toString(), Toast.LENGTH_LONG).show();
      }
    };
    vos = new VolleyObjectService(vor, Loan.this);
    vos.getJsonObject("GETCALL", url);

  }
  public void onBackPressed()
  {
    // code here to show dialog
    super.onBackPressed();  // optional depending on your needs
    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
  }
}
