package id.co.bspguard.android.bravo.calculator;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.bspguard.android.bravo.AlertDialog.KAlertDialog;
import id.co.bspguard.android.bravo.ConnectionLost;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.loan.BulanPinjamanAdapter;
import id.co.bspguard.android.bravo.loan.BulanPinjamanDataSet;
import id.co.bspguard.android.bravo.loan.FormLoan;
import id.co.bspguard.android.bravo.loan.Loan;
import id.co.bspguard.android.bravo.loan.LoanAdapter;
import id.co.bspguard.android.bravo.loan.LoanDataSet;

public class Calculator extends AppCompatActivity {

    private ListView dataPlafon;
    VolleyObjectResult volleyObjectResult, vor, vorLoan, vorTenor, vorDay = null;
    VolleyObjectService volleyObjectService, vos, vosLoan, vosTenor, vosDay;
  Fungsi fungsi = new Fungsi();
  String url = fungsi.url()+"plafon";
  String url_loan = fungsi.url() + "loan";
  private List<ListPinjamanDataSet> listLoan = new ArrayList<ListPinjamanDataSet>();
  private ListPinjamanAdapter loanAdapter;
  private List<BulanPinjamanDataSet> listBulanPinjaman = new ArrayList<BulanPinjamanDataSet>();
  private BulanPinjamanAdapter bulanPinjamanAdapter;
  TextView batasCicil, jenisPinjamanText, bungaPinjaman, nominalPinjaman,
    nominalDibayarkanPinjaman, biayaAdminPinjaman, biayaProvisiPinjaman,
    bungaBerjalanPinjaman, danaDiterimaPinjaman, cicilanPerbulanPinjaman, biayaTransferPinjaman;
  ListView listDataPinjaman, listDataBulanPinjaman;
  String id_penjamin;
  int maksimumTenor;
  int lama_cicilan;
  int nominal_cicilan;
  int bunga_cicilan;
  int nominal_dibayar;
  int biaya_provisi;
  int bungaberjalan;
  int pinjaman_diterima;
  int day;
  int cicilan_perbulan;
  int totalbungacicilan;
  LinearLayout rincianPinjaman;
  String urlgetDayBungaBerjalan = fungsi.url()+"get-bunga-berjalan";
  private List<CalculatorDataSet> list = new ArrayList<CalculatorDataSet>();
    JSONObject data = null;

    TextView hasil, besarajuan, pinjaman;
    Button hitung;
    EditText plafon, tenor, jenis_pinjaman;
  String loanName;
  double provisi, bunga, bunga_berjalan;
  int biaya_admin;
  int biaya_transfer;
  int batas_pinjaman;
    boolean connected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        connected = fungsi.isActiveNetwork(getApplication());
        if(!connected){
          startActivity(new Intent(getApplication(), ConnectionLost.class));
        }

        hitung = (Button) findViewById(R.id.btn_hitung);
        plafon = (EditText) findViewById(R.id.inputplafon);
        jenis_pinjaman = (EditText) findViewById(R.id.jenis_pinjaman);
        tenor = (EditText) findViewById(R.id.tenor);
        pinjaman = (TextView) findViewById(R.id.pinjaman);

        bungaPinjaman = (TextView) findViewById(R.id.bunga_pinjaman);
        nominalPinjaman = (TextView) findViewById(R.id.pinjaman_diajukan);
        nominalDibayarkanPinjaman = (TextView) findViewById(R.id.nominal_dibayarkan);
        biayaAdminPinjaman = (TextView) findViewById(R.id.biaya_admin);
        biayaProvisiPinjaman = (TextView) findViewById(R.id.biaya_provisi);
        bungaBerjalanPinjaman = (TextView) findViewById(R.id.bunga_berjalan);
        danaDiterimaPinjaman = (TextView) findViewById(R.id.pinjaman_diterima);
        cicilanPerbulanPinjaman = (TextView) findViewById(R.id.cicilan_perbulan);
        biayaTransferPinjaman = (TextView) findViewById(R.id.biaya_transfer);
      rincianPinjaman = (LinearLayout) findViewById(R.id.rincian);

      pinjaman.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(getApplicationContext(), Loan.class);
          startActivity(intent);
        }
      });

      vorDay = new VolleyObjectResult() {

        @Override
        public void resSuccess(String requestType, JSONObject response) {
          try {
            day = response.getInt("data");

          } catch (JSONException e) {
            e.printStackTrace();
          }
        }

        @Override
        public void resError(String requestType, VolleyError error) {
          new KAlertDialog(Calculator.this, KAlertDialog.WARNING_TYPE)
            .setTitleText("Oops...")
            .setContentText("Network connection problem")
            .setConfirmText("OK")
            .show();
        }
      };
      vosDay = new VolleyObjectService(vorDay, getApplicationContext());
      vosDay.getJsonObject("GETCALL", urlgetDayBungaBerjalan);

      jenis_pinjaman.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


          listLoan.clear();
          final AlertDialog.Builder builder = new AlertDialog.Builder(Calculator.this, R.style.DialogLoan);
          final LayoutInflater inflater = Calculator.this.getLayoutInflater();
          final View view = inflater.inflate(R.layout.bottom_sheet_jenis_pinjaman, null, false);
          listDataPinjaman = (ListView) view.findViewById(R.id.list_Pinjaman);
          builder.setView(view);

          vorLoan = new VolleyObjectResult() {

            @Override
            public void resSuccess(String requestType, JSONObject response) {
              try {
                int id_count = 0;
                JSONArray jsonArray = response.getJSONArray("data");
                if(jsonArray.length() > id_count) {
                  for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    ListPinjamanDataSet lds = new ListPinjamanDataSet();
                    lds.setId(jsonObject.getString("id"));
                    lds.setLogo(jsonObject.getString("logo"));
                    lds.setLoan_name(jsonObject.getString("loan_name"));
                    lds.setPlafon(jsonObject.getString("plafon"));
                    lds.setDescription(jsonObject.getString("description"));
                    lds.setRate_of_interest(jsonObject.getDouble("rate_of_interest"));
                    lds.setTenor(jsonObject.getString("tenor"));
                    lds.setBatas_pinjaman(jsonObject.getInt("plafon"));
                    lds.setBiaya_admin(jsonObject.getInt("biaya_admin"));
                    lds.setBunga(jsonObject.getDouble("rate_of_interest"));
                    lds.setBiaya_transfer(jsonObject.getInt("biaya_transfer"));
                    lds.setBunga_berjalan(jsonObject.getDouble("biaya_bunga_berjalan"));
                    lds.setProvisi(jsonObject.getDouble("provisi"));
                    listLoan.add(lds);
                  }
                }else{
                  Toast.makeText(getApplicationContext(), "Pinjaman belum tersedia", Toast.LENGTH_LONG).show();
                }

              } catch (JSONException e) {
                e.printStackTrace();
              }

              loanAdapter = new ListPinjamanAdapter(Calculator.this, listLoan);
              loanAdapter.notifyDataSetChanged();
              listDataPinjaman.setAdapter(loanAdapter);
            }

            @Override
            public void resError(String requestType, VolleyError error) {
              new KAlertDialog(Calculator.this, KAlertDialog.WARNING_TYPE)
                .setTitleText("Oops...")
                .setContentText("Network connection problem")
                .setConfirmText("OK")
                .show();
            }
          };
          vosLoan = new VolleyObjectService(vorLoan, getApplicationContext());
          vosLoan.getJsonObject("GETCALL", url_loan);
          final AlertDialog alertDialog = builder.create();
          Window window = alertDialog.getWindow();
          window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
          alertDialog.show();
          listDataPinjaman.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              String jenisPinjaman = listLoan.get(position).getLoan_name();
              jenis_pinjaman.setText(jenisPinjaman);
              id_penjamin = listLoan.get(position).getId();
              maksimumTenor = Integer.parseInt(listLoan.get(position).getTenor());
              biaya_admin  = listLoan.get(position).getBiaya_admin();
              provisi  = listLoan.get(position).getProvisi();
              bunga_berjalan = listLoan.get(position).getBunga_berjalan();
              biaya_transfer = listLoan.get(position).getBiaya_transfer();
              bunga = listLoan.get(position).getRate_of_interest();
              alertDialog.dismiss();
            }
          });

        }
      });

      tenor.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listBulanPinjaman.clear();
          final AlertDialog.Builder builderBulan = new AlertDialog.Builder(Calculator.this, R.style.DialogLoan);
          final LayoutInflater inflater = Calculator.this.getLayoutInflater();
          final View viewBulan = inflater.inflate(R.layout.bottom_sheet_bulan, null, false);
          builderBulan.setView(viewBulan);
          listDataBulanPinjaman = (ListView) viewBulan.findViewById(R.id.list_Bulan);
          int cBulan = 1;
          for (int i = 0; i < maksimumTenor; i++){
            BulanPinjamanDataSet bulanPinjamanDataSet = new BulanPinjamanDataSet();
            bulanPinjamanDataSet.setBulan(cBulan++);
            listBulanPinjaman.add(bulanPinjamanDataSet);
          }
          bulanPinjamanAdapter = new BulanPinjamanAdapter(Calculator.this, listBulanPinjaman);
          bulanPinjamanAdapter.notifyDataSetChanged();
          listDataBulanPinjaman.setAdapter(bulanPinjamanAdapter);
          final AlertDialog alertDialog = builderBulan.create();
          Window window = alertDialog.getWindow();
          window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
          alertDialog.show();
          listDataBulanPinjaman.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              int name_penjamin = listBulanPinjaman.get(position).getBulan();
              tenor.setText(String.valueOf(name_penjamin));
              lama_cicilan = Integer.parseInt(String.valueOf(tenor.getText()));
              alertDialog.dismiss();
            }
          });
        }
      });

        hitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if (!validate()) {
                new KAlertDialog(Calculator.this, KAlertDialog.WARNING_TYPE)
                  .setTitleText("Oops...")
                  .setContentText("Isian form tidak valid")
                  .setConfirmText("OK")
                  .show();
                return;
              }

              rincianPinjaman.setVisibility(View.VISIBLE);
              nominal_cicilan = Integer.parseInt(String.valueOf(plafon.getText()));
              bunga_cicilan = (int) ((nominal_cicilan * (bunga / 100)) / lama_cicilan) * lama_cicilan;
              totalbungacicilan = (int) bunga_cicilan * lama_cicilan;

              nominal_dibayar = (int) ((nominal_cicilan * (bunga / 100)));
              nominal_dibayar = nominal_cicilan + totalbungacicilan;
              biaya_provisi = (int) ((nominal_cicilan * (provisi / 100)));
              bungaberjalan = (int) ((nominal_cicilan * (bunga_berjalan / 100)) * day);
              pinjaman_diterima = nominal_cicilan - biaya_admin - biaya_provisi - bungaberjalan - biaya_transfer;
              cicilan_perbulan = (nominal_cicilan / lama_cicilan) + (int) bunga_cicilan;

              bungaPinjaman.setText(fungsi.formatIntRupiah(String.valueOf(totalbungacicilan)) +" ( " + fungsi.formatIntRupiah(String.valueOf(bunga_cicilan)) + " x " + lama_cicilan +" )");
              nominalPinjaman.setText(fungsi.formatIntRupiah(String.valueOf(nominal_cicilan)));
              nominalDibayarkanPinjaman.setText(fungsi.formatIntRupiah(String.valueOf(nominal_dibayar)));
              biayaAdminPinjaman.setText(fungsi.formatIntRupiah(String.valueOf(biaya_admin)));
              biayaProvisiPinjaman.setText(fungsi.formatIntRupiah(String.valueOf(biaya_provisi)));
              bungaBerjalanPinjaman.setText(fungsi.formatIntRupiah(String.valueOf(bungaberjalan)));
              danaDiterimaPinjaman.setText(fungsi.formatIntRupiah(String.valueOf(pinjaman_diterima)));
              cicilanPerbulanPinjaman.setText(fungsi.formatIntRupiah(String.valueOf(cicilan_perbulan)));
              biayaTransferPinjaman.setText((fungsi.formatIntRupiah(String.valueOf(biaya_transfer))));
            }

        });




    }

  public boolean validate() {
    boolean valid = true;

    String iPlafon = plafon.getText().toString();
    String iTenor = tenor.getText().toString();



    if (iPlafon.isEmpty() || Integer.parseInt(iPlafon) == 0) {
      plafon.setError("plafon tidak boleh kosong atau 0");
      valid = false;
    } else {
      plafon.setError(null);
    }

    if (iTenor.isEmpty() || Integer.parseInt(iTenor) == 0) {
      tenor.setError("tenor tidak boleh kosong atau 0");
      valid = false;
    } else {
      tenor.setError(null);
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
