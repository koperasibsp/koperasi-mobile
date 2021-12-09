package id.co.bspguard.android.bravo.memberloan;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import id.co.bspguard.android.bravo.fragment.BottomSheetDetailLoan;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.home.ActivityAwal;

public class DetailLoan extends AppCompatActivity{
  Fungsi fn = new Fungsi();
  TextView namaPinjaman, kodePinjaman, valuePinjaman, approvalPinjaman, periodPinjaman, valueSisaPinjaman;
  ImageView imagePinjaman;
  ListView listDetailLoan;
  FloatingActionButton filter, ajukan;
  VolleyObjectResult vor;
  VolleyObjectService vos;
  JSONObject data = null;
  String url = fn.url() + "my-loan-detail";
  String urlLoanFilter = fn.url() + "filter-loan";
  private List<ListDetailLoanDataSet> list = new ArrayList<ListDetailLoanDataSet>();
  private AdapterDetailLoan loanDetailAdapter;

  private ProgressBar progressBar;
  private LovelySaveStateHandler saveStateHandler;
  Bundle bundle = new Bundle();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_loan_detail_list);

    boolean connected = fn.isActiveNetwork(getApplication());
    if(!connected){
      startActivity(new Intent(getApplication(), ConnectionLost.class));
    }


    bundle = getIntent().getExtras();
    listDetailLoan = (ListView) findViewById(R.id.listLoan);
    namaPinjaman = (TextView) findViewById(R.id.namaPinjaman);
    kodePinjaman = (TextView) findViewById(R.id.kodePinjaman);
    valuePinjaman = (TextView) findViewById(R.id.nilaiPinjaman);
    valueSisaPinjaman = (TextView) findViewById(R.id.sisaPinjaman);
    approvalPinjaman = (TextView) findViewById(R.id.approvalPinjaman);
    periodPinjaman = (TextView) findViewById(R.id.periodPinjaman);
    imagePinjaman = (ImageView) findViewById(R.id.imagePinjaman);


//    card.setOnExpandedListener(new ExpandableCardView.OnExpandedListener() {
//      @Override
//      public void onExpandChanged(View v, boolean isExpanded) {
//        Toast.makeText(getApplicationContext(), isExpanded ? "Expanded!" : "Collapsed!", Toast.LENGTH_SHORT).show();
//      }
//    });

    vor = new VolleyObjectResult() {
      @Override
      public void resSuccess(String requestType, JSONObject response) {

        //Toast.makeText(ListTour.this, response.toString(), Toast.LENGTH_LONG).show();
        progressBar = (ProgressBar) findViewById(R.id.proBar);
        try {
          progressBar.setVisibility(View.VISIBLE);
          progressBar.setVisibility(View.GONE);
          JSONObject loan = response.getJSONObject("data");
          JSONObject msLoan = loan.getJSONObject("ms_loans");
          JSONArray jsonArray = loan.getJSONArray("detail");

          String period = loan.getString("period")+" bulan";
          String price = loan.getString("value");
          int val = (int) Double.parseDouble(String.valueOf(price));
          String s = NumberFormat.getIntegerInstance(Locale.GERMAN).format(val);
          String sisaPinjaman = fn.formatIntRupiah(loan.getString("sisa_pinjaman"));

          periodPinjaman.setText(period);
          namaPinjaman.setText(msLoan.getString("loan_name"));
          kodePinjaman.setText(loan.getString("loan_number"));
          valuePinjaman.setText(s);
          valueSisaPinjaman.setText(sisaPinjaman);
          approvalPinjaman.setText(loan.getString("approval"));
          String url = fn.urlImages(msLoan.getString("logo"));

          Glide.with(getApplicationContext()).asBitmap().load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.mipmap.ic_launcher)
            .into(imagePinjaman);

          int id_count = 0;
          if(jsonArray.length() > id_count) {
            for (int i = 0; i < jsonArray.length(); i++) {
              JSONObject jsonObject = jsonArray.getJSONObject(i);
              ListDetailLoanDataSet data = new ListDetailLoanDataSet();
              data.setId(jsonObject.getString("id"));
              data.setValue(jsonObject.getString("value"));
              data.setService(jsonObject.getString("service"));
              data.setPay_date(jsonObject.getString("pay_date"));
              data.setIn_period(jsonObject.getString("in_period"));
              data.setApproval(jsonObject.getString("approval"));
              data.setPeriod(loan.getString("period"));

              list.add(data);
            }
            loanDetailAdapter = new AdapterDetailLoan(DetailLoan.this, list);
            loanDetailAdapter.notifyDataSetChanged();
            listDetailLoan.setAdapter(loanDetailAdapter);
          }else {
            Thread.sleep(2000);
            Toast.makeText(DetailLoan.this, "Anda belum melakukan pinjaman", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(DetailLoan.this, ActivityAwal.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      @Override
      public void resError(String requestType, VolleyError error) {
//        Toast.makeText(DetailLoan.this, error.toString(), Toast.LENGTH_LONG).show();
      }
    };
    vos = new VolleyObjectService(vor, DetailLoan.this);
    vos.getJsonObject("GETCALL", url+"/"+bundle.getString("id"));
    listDetailLoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String iTenor = list.get(position).getIn_period() +"/"+ list.get(position).getPeriod();
        String iValue = fn.formatNominal(list.get(position).getValue());
        String iService = fn.formatNominal(list.get(position).getService());

        Bundle bundle = new Bundle();
        bundle.putString("tenor", iTenor);
        bundle.putString("tagihan", iValue);
        bundle.putString("jasa", iService);
        bundle.putString("tanggal", list.get(position).getPay_date());
        bundle.putString("status", list.get(position).getApproval());

        BottomSheetDetailLoan bottomSheetDialog = BottomSheetDetailLoan.newInstance();
        bottomSheetDialog.setArguments(bundle);
        bottomSheetDialog.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
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
