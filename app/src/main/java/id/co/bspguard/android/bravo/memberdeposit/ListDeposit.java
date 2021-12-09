package id.co.bspguard.android.bravo.memberdeposit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.github.clans.fab.FloatingActionButton;
import com.yarolegovich.lovelydialog.LovelySaveStateHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.bspguard.android.bravo.ConnectionLost;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.home.ActivityAwal;

public class ListDeposit extends AppCompatActivity implements View.OnClickListener {
  Fungsi fn = new Fungsi();

  ListView listDeposit;
  FloatingActionButton filter, ajukan;
  VolleyObjectResult vor;
  VolleyObjectService vos;
  JSONObject data = null;
  String url = fn.url() + "my-deposit";

  String token;
  private List<ListDepositDataset> list = new ArrayList<ListDepositDataset>();
  private AdapterListDeposit depositAdapter;
  ActionBar actionBar;

  private ProgressBar progressBar;
  private LovelySaveStateHandler saveStateHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_deposit_list);

    boolean connected = fn.isActiveNetwork(getApplication());
    if(!connected){
      startActivity(new Intent(getApplication(), ConnectionLost.class));
    }

    listDeposit = (ListView) findViewById(R.id.listDeposit);

    vor = new VolleyObjectResult() {
      @Override
      public void resSuccess(String requestType, JSONObject response) {

//        Toast.makeText(ListDeposit.this, response.toString(), Toast.LENGTH_LONG).show();
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
              ListDepositDataset data = new ListDepositDataset();
              data.setDeposit_id(jsonObject.getString("id"));
              data.setValue(jsonObject.getString("value"));
              data.setDeposit_name(jsonObject.getString("deposit_name"));
              list.add(data);
            }
            depositAdapter = new AdapterListDeposit(ListDeposit.this, list);
            depositAdapter.notifyDataSetChanged();
            listDeposit.setAdapter(depositAdapter);
          }else {
            Thread.sleep(2000);
            Toast.makeText(ListDeposit.this, "Anda belum memiliki simpanan", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ListDeposit.this, ActivityAwal.class);
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
    vos = new VolleyObjectService(vor, ListDeposit.this);
    vos.getJsonObject("GETCALL", url);


  }

  @Override
  public void onClick(View v) {

  }
  public void onBackPressed()
  {
    // code here to show dialog
    super.onBackPressed();  // optional depending on your needs
    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
  }
}
