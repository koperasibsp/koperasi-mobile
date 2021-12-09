package id.co.bspguard.android.bravo.memberloan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.github.clans.fab.FloatingActionButton;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelySaveStateHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.bspguard.android.bravo.AlertDialog.KAlertDialog;
import id.co.bspguard.android.bravo.ConnectionLost;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.home.ActivityAwal;
import id.co.bspguard.android.bravo.memberdeposit.DetailDeposit;
import id.co.bspguard.android.bravo.memberresign.MainResign;

public class ListLoan  extends AppCompatActivity implements View.OnClickListener {
    Fungsi fn = new Fungsi();

    ListView listLoan;
    FloatingActionButton filter, ajukan;
    VolleyObjectResult vor;
    VolleyObjectService vos;
    JSONObject data = null;
    String url = fn.url() + "my-loan";
    String urlLoanFilter = fn.url() + "filter-loan";

    private List<ListLoanDataSet> list = new ArrayList<ListLoanDataSet>();
    private AdapterListLoan loanAdapter;

    private ProgressBar progressBar;
    private LovelySaveStateHandler saveStateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main_loan_list);

      boolean connected = fn.isActiveNetwork(getApplication());
      if(!connected){
        startActivity(new Intent(getApplication(), ConnectionLost.class));
      }

      saveStateHandler = new LovelySaveStateHandler();

      listLoan = (ListView) findViewById(R.id.listLoan);
      filter = (FloatingActionButton) findViewById(R.id.filter);
      ajukan = (FloatingActionButton) findViewById(R.id.addloan);

      filter.setOnClickListener(this);
      ajukan.setOnClickListener(this);

      vor = new VolleyObjectResult() {
        @Override
        public void resSuccess(String requestType, JSONObject response) {

          //Toast.makeText(ListTour.this, response.toString(), Toast.LENGTH_LONG).show();
          progressBar = (ProgressBar) findViewById(R.id.proBar);
          try {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            JSONArray jsonArray = response.getJSONArray("data");
            int id_count = 0;
            if(jsonArray.length() > id_count) {
              for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject msLoan = jsonObject.getJSONObject("ms_loans");
                ListLoanDataSet data = new ListLoanDataSet();
                data.setId(jsonObject.getString("id"));
                data.setLoan_number(jsonObject.getString("loan_number"));
                data.setValue(jsonObject.getString("value"));
                data.setStart_date(jsonObject.getString("start_date"));
                data.setEnd_date(jsonObject.getString("end_date"));
                data.setPeriod(jsonObject.getString("period"));
                data.setIn_period(jsonObject.getString("in_period"));
                data.setApproval(jsonObject.getString("approval"));
                data.setLoan_name(msLoan.getString("loan_name"));
                data.setLogo(msLoan.getString("logo"));
                list.add(data);
              }
              loanAdapter = new AdapterListLoan(ListLoan.this, list);
              loanAdapter.notifyDataSetChanged();
              listLoan.setAdapter(loanAdapter);
            }else {
              Thread.sleep(2000);
              new KAlertDialog(ListLoan.this, KAlertDialog.WARNING_TYPE)
                .setTitleText("Warning...")
                .setContentText("Anda belum melakukan pinjaman")
                .setConfirmText("OK")
                .show();
            }

          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        @Override
        public void resError(String requestType, VolleyError error) {
//          Toast.makeText(ListLoan.this, error.toString(), Toast.LENGTH_LONG).show();
        }
      };
      vos = new VolleyObjectService(vor, ListLoan.this);
      vos.getJsonObject("GETCALL", url);

    }


  @Override
  public void onClick(View v) {
    showLovelyDialog(v.getId(), null);
  }

  private void showLovelyDialog(int dialogId, Bundle savedInstanceState) {
    switch (dialogId) {
      case R.id.filter:
        showMultiChoiceDialog(savedInstanceState);
        break;
      case R.id.addloan:
        break;
      default:
        break;
    }
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    saveStateHandler.saveInstanceState(outState);
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onRestoreInstanceState(@NonNull Bundle savedState) {
    super.onRestoreInstanceState(savedState);
    if (LovelySaveStateHandler.wasDialogOnScreen(savedState)) {
      //Dialog won't be restarted automatically, so we need to call this method.
      //Each dialog knows how to restore its state
      showLovelyDialog(LovelySaveStateHandler.getSavedDialogId(savedState), savedState);
    }
  }

  private void showMultiChoiceDialog(Bundle savedInstanceState) {
    String[] items = getResources().getStringArray(R.array.filterloan);
    new LovelyChoiceDialog(this, R.style.CheckBoxTintTheme)
      .setTopColorRes(R.color.softbsp)
      .setTitle("Filter")
      .setIcon(R.drawable.ic_filter)
      .setInstanceStateHandler(R.id.filter, saveStateHandler)
      .setItemsMultiChoice(items, new LovelyChoiceDialog.OnItemsSelectedListener<String>() {
        @Override
        public void onItemsSelected(List<Integer> positions, List<String> items1) {
//          Toast.makeText(ListLoan.this,
//            ListLoan.this.getString(R.string.you_ordered, TextUtils.join("\n", items1)),
//            Toast.LENGTH_SHORT)
//            .show();

          HashMap<Object, Object> headers = new HashMap<Object, Object>();
          headers.put("filter", items1);

          final JSONObject data = new JSONObject(headers);

          vor = new VolleyObjectResult() {
            @Override
            public void resSuccess(String requestType, JSONObject response) {

              //Toast.makeText(ListTour.this, response.toString(), Toast.LENGTH_LONG).show();
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
                    JSONObject msLoan = jsonObject.getJSONObject("ms_loans");
                    ListLoanDataSet data = new ListLoanDataSet();
                    data.setId(jsonObject.getString("id"));
                    data.setLoan_number(jsonObject.getString("loan_number"));
                    data.setValue(jsonObject.getString("value"));
                    data.setStart_date(jsonObject.getString("start_date"));
                    data.setEnd_date(jsonObject.getString("end_date"));
                    data.setPeriod(jsonObject.getString("period"));
                    data.setIn_period(jsonObject.getString("in_period"));
                    data.setApproval(jsonObject.getString("approval"));
                    data.setLoan_name(msLoan.getString("loan_name"));
                    data.setLogo(msLoan.getString("logo"));
                    list.add(data);
                  }
                  loanAdapter = new AdapterListLoan(ListLoan.this, list);
                  loanAdapter.notifyDataSetChanged();
                  listLoan.setAdapter(loanAdapter);
                }else {
                  Thread.sleep(2000);
                  new KAlertDialog(ListLoan.this, KAlertDialog.WARNING_TYPE)
                    .setTitleText("Warning...")
                    .setContentText("Data pinjaman tidak ditemukan")
                    .setConfirmText("OK")
                    .show();
                }

              } catch (Exception e) {
                e.printStackTrace();
              }
            }
            @Override
            public void resError(String requestType, VolleyError error) {
              Toast.makeText(ListLoan.this, error.toString(), Toast.LENGTH_LONG).show();
            }
          };
          vos = new VolleyObjectService(vor, ListLoan.this);
          vos.postJsonObject("GETCALL", urlLoanFilter, data);
        }
      })
      .setConfirmButtonText(R.string.confirm)
      .setSavedInstanceState(savedInstanceState)
      .show();
  }
  public void onBackPressed()
  {
    // code here to show dialog
    super.onBackPressed();  // optional depending on your needs
    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
  }
}
