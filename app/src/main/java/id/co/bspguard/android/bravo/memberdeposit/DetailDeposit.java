package id.co.bspguard.android.bravo.memberdeposit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import id.co.bspguard.android.bravo.accounts.Signup;
import id.co.bspguard.android.bravo.fragment.BottomSheetDetailDeposit;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;

public class DetailDeposit extends AppCompatActivity implements View.OnClickListener {
  Fungsi fn = new Fungsi();
  TextView depositName, depositValue;

  ListView listDepositDetail;
  FloatingActionButton filter, ajukan;
  VolleyObjectResult vor;
  VolleyObjectService vos;
  JSONObject data = null;
  String url = fn.url() + "my-deposit-detail";
  String urlFilterDeposit = fn.url() + "filter-deposit";

  String token;
  private List<ListDetailDepositDataSet> list = new ArrayList<ListDetailDepositDataSet>();
  private AdapterDetailDeposit detailDepositAdapter;
  ActionBar actionBar;
  Bundle bundle = new Bundle();
  private ProgressBar progressBar;
  private LovelySaveStateHandler saveStateHandler;
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_deposit_detail);

    boolean connected = fn.isActiveNetwork(getApplication());
    if(!connected){
      startActivity(new Intent(getApplication(), ConnectionLost.class));
    }

    saveStateHandler = new LovelySaveStateHandler();
    filter = (FloatingActionButton) findViewById(R.id.filter);
    filter.setOnClickListener(this);


    bundle = getIntent().getExtras();
    listDepositDetail = (ListView) findViewById(R.id.listDepositDetail);

    depositName = (TextView) findViewById(R.id.deposit_name);
    depositValue = (TextView) findViewById(R.id.depositValue);

    depositName.setText(bundle.getString("name", ""));
    depositValue.setText(bundle.getString("total", ""));
    String idDeposit = bundle.getString("id");

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
              ListDetailDepositDataSet data = new ListDetailDepositDataSet();
              data.setId(jsonObject.getString("id"));
              data.setDeposit_number(jsonObject.getString("deposit_number"));
              data.setType(jsonObject.getString("type"));
              data.setDeposit_type(jsonObject.getString("deposits_type"));
              data.setValue(jsonObject.getString("total_deposit"));
              data.setStatus(jsonObject.getString("status"));
              data.setCut_date(jsonObject.getString("post_date"));
              data.setDesc(jsonObject.getString("desc"));
              list.add(data);
            }
            detailDepositAdapter = new AdapterDetailDeposit(DetailDeposit.this, list);
            detailDepositAdapter.notifyDataSetChanged();
            listDepositDetail.setAdapter(detailDepositAdapter);
          }else {
            Thread.sleep(2000);

            new KAlertDialog(DetailDeposit.this, KAlertDialog.ERROR_TYPE)
              .setTitleText("Oops...")
              .setContentText("Anda belum memiliki simpanan")
              .setConfirmText("OK")
              .show();
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      @Override
      public void resError(String requestType, VolleyError error) {
//        Toast.makeText(DetailDeposit.this, error.toString(), Toast.LENGTH_LONG).show();
//        startActivity(new Intent(getApplicationContext(), ConnectionLost.class));

      }
    };
    vos = new VolleyObjectService(vor, DetailDeposit.this);
    vos.getJsonObject("GETCALL", url+"/"+idDeposit);
    listDepositDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String iValue = fn.formatNominalDouble(list.get(position).getValue());
        String iTanggal = fn.ubahTglHms(list.get(position).getCut_date());

        Bundle bundle = new Bundle();
        bundle.putString("depositNumber", list.get(position).getDeposit_number());
        bundle.putString("depositType", list.get(position).getType());
        bundle.putString("depositTanggal", iTanggal);
        bundle.putString("depositTotal", iValue);
        bundle.putString("depositDesc", list.get(position).getDesc());
        bundle.putString("depositStatus", list.get(position).getStatus());

        BottomSheetDetailDeposit bottomSheetDialog = BottomSheetDetailDeposit.newInstance();
        bottomSheetDialog.setArguments(bundle);
        bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
      }
    });
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
    String[] items = getResources().getStringArray(R.array.filterDeposit);
    final String idDeposit = bundle.getString("id");

    new LovelyChoiceDialog(this, R.style.CheckBoxTintTheme)
      .setTopColorRes(R.color.softbsp)
      .setTitle("Filter")
      .setIcon(R.drawable.ic_filter)
      .setInstanceStateHandler(R.id.filter, saveStateHandler)
      .setItemsMultiChoice(items, new LovelyChoiceDialog.OnItemsSelectedListener<String>() {
        @Override
        public void onItemsSelected(List<Integer> positions, List<String> items1) {
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
                    ListDetailDepositDataSet data = new ListDetailDepositDataSet();
                    data.setId(jsonObject.getString("id"));
                    data.setDeposit_number(jsonObject.getString("deposit_number"));
                    data.setType(jsonObject.getString("type"));
                    data.setDeposit_type(jsonObject.getString("deposits_type"));
                    data.setValue(jsonObject.getString("total_deposit"));
                    data.setStatus(jsonObject.getString("status"));
                    data.setCut_date(jsonObject.getString("post_date"));
                    data.setDesc(jsonObject.getString("desc"));
                    list.add(data);
                  }
                  detailDepositAdapter = new AdapterDetailDeposit(DetailDeposit.this, list);
                  detailDepositAdapter.notifyDataSetChanged();
                  listDepositDetail.setAdapter(detailDepositAdapter);
                }else {
                  Thread.sleep(2000);

                  new KAlertDialog(DetailDeposit.this, KAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Data simpanan tidak ditemukan")
                    .setConfirmText("OK")
                    .show();
                }

              } catch (Exception e) {
                e.printStackTrace();
              }
            }
            @Override
            public void resError(String requestType, VolleyError error) {
              Toast.makeText(DetailDeposit.this, error.toString(), Toast.LENGTH_LONG).show();
            }
          };
          vos = new VolleyObjectService(vor, DetailDeposit.this);
          vos.postJsonObject("GETCALL", urlFilterDeposit+"/"+idDeposit, data);
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
