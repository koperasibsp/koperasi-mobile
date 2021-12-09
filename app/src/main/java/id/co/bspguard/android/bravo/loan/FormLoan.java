package id.co.bspguard.android.bravo.loan;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import id.co.bspguard.android.bravo.AlertDialog.KAlertDialog;
import id.co.bspguard.android.bravo.MainActivity;
import id.co.bspguard.android.bravo.OpenBrowser;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.accounts.Login;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.home.HomeSliderPagerAdapter;
import id.co.bspguard.android.bravo.home.TopLoanDataSet;
import id.co.bspguard.android.bravo.memberloan.ListLoan;

public class FormLoan extends AppCompatActivity {
  Fungsi fn = new Fungsi();
  Bundle bundle = new Bundle();
  TextView batasCicil, jenisPinjaman, bungaPinjaman, nominalPinjaman,
    nominalDibayarkanPinjaman, biayaAdminPinjaman, biayaProvisiPinjaman,
    bungaBerjalanPinjaman, danaDiterimaPinjaman, cicilanPerbulanPinjaman, biayaTransferPinjaman;
  EditText penjamin, tenor, nominal;
  Button btnRincian, btnAjukan;
  LinearLayout rincianPinjaman;
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
  String id_penjamin;

  VolleyObjectResult vor;
  VolleyObjectService vos;
  ListView listDataPenjamin, listDataBulanPinjaman;

  String urlPenjamin = fn.url()+"get-penjamin-loan";
  String urlgetDayBungaBerjalan = fn.url()+"get-bunga-berjalan";
  String urlPostPinjaman = fn.url()+"post-loan";

  private List<PenjaminLoanDataSet> listPenjamin = new ArrayList<PenjaminLoanDataSet>();
  private PenjaminLoanAdapter penjaminLoanAdapter;

  private List<BulanPinjamanDataSet> listBulanPinjaman = new ArrayList<BulanPinjamanDataSet>();
  private BulanPinjamanAdapter bulanPinjamanAdapter;

  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_form_loan);

    Bundle bundle = getIntent().getExtras();
    String loanID = bundle.getString("id");
    String loanName = bundle.getString("loan_name");
    String loanTenor = bundle.getString("tenor");
    String loanPlafon = bundle.getString("plafon");
    String loanBunga = bundle.getString("rate_of_interest");
    double provisi = bundle.getDouble("provisi");
    double bunga = bundle.getDouble("bunga");
    double bunga_berjalan = bundle.getDouble("bunga_berjalan");
    int biaya_admin = bundle.getInt("biaya_admin");
    int biaya_transfer = bundle.getInt("biaya_transfer");
    int batas_pinjaman = bundle.getInt("batas_pinjaman");
    int tenorMaxPinjaman = Integer.parseInt(loanTenor);

    batasCicil = (TextView) findViewById(R.id.batasCicil);
    rincianPinjaman = (LinearLayout) findViewById(R.id.rincian);
    penjamin = (EditText) findViewById(R.id.penjamin_cicilan);
    tenor = (EditText) findViewById(R.id.lama_cicilan);
    nominal = (EditText) findViewById(R.id.jumlah_cicilan);

    jenisPinjaman = (TextView) findViewById(R.id.jenis_pinjaman);
    bungaPinjaman = (TextView) findViewById(R.id.bunga_pinjaman);
    nominalPinjaman = (TextView) findViewById(R.id.pinjaman_diajukan);
    nominalDibayarkanPinjaman = (TextView) findViewById(R.id.nominal_dibayarkan);
    biayaAdminPinjaman = (TextView) findViewById(R.id.biaya_admin);
    biayaProvisiPinjaman = (TextView) findViewById(R.id.biaya_provisi);
    bungaBerjalanPinjaman = (TextView) findViewById(R.id.bunga_berjalan);
    danaDiterimaPinjaman = (TextView) findViewById(R.id.pinjaman_diterima);
    cicilanPerbulanPinjaman = (TextView) findViewById(R.id.cicilan_perbulan);
    biayaTransferPinjaman = (TextView) findViewById(R.id.biaya_transfer);

    btnRincian = (Button) findViewById(R.id.rincian_cicilan);
    btnAjukan = (Button) findViewById(R.id.ajukan_cicilan);
    batasCicil.setText("Dapatkan cicilan pinjaman hingga batas maksimum pinjaman Rp. "+ fn.formatIntRupiah(loanPlafon));

    rincianPinjaman.setVisibility(View.GONE);

    vor = new VolleyObjectResult() {

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
        new KAlertDialog(FormLoan.this, KAlertDialog.WARNING_TYPE)
          .setTitleText("Oops...")
          .setContentText("Network connection problem")
          .setConfirmText("OK")
          .show();
      }
    };
    vos = new VolleyObjectService(vor, getApplicationContext());
    vos.getJsonObject("GETCALL", urlgetDayBungaBerjalan);

    btnRincian.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!validate()) {
          onRincianFailed();
          return;
        }
        rincianPinjaman.setVisibility(View.VISIBLE);
        nominal_cicilan = Integer.parseInt(String.valueOf(nominal.getText()));
        bunga_cicilan = (int) ((nominal_cicilan * (bunga / 100)) / lama_cicilan) * lama_cicilan;
        totalbungacicilan = (int) bunga_cicilan * lama_cicilan;


        nominal_dibayar = (int) ((nominal_cicilan * (bunga / 100)));
        nominal_dibayar = nominal_cicilan + totalbungacicilan;
        biaya_provisi = (int) ((nominal_cicilan * (provisi / 100)));
        bungaberjalan = (int) ((nominal_cicilan * (bunga_berjalan / 100)) * day);
        pinjaman_diterima = nominal_cicilan - biaya_admin - biaya_provisi - bungaberjalan - biaya_transfer;
        cicilan_perbulan = (nominal_cicilan / lama_cicilan) + (int) bunga_cicilan;

        jenisPinjaman.setText(loanName);
        bungaPinjaman.setText(fn.formatIntRupiah(String.valueOf(totalbungacicilan)) +" ( " + fn.formatIntRupiah(String.valueOf(bunga_cicilan)) + " x " + lama_cicilan +" )");
        nominalPinjaman.setText(fn.formatIntRupiah(String.valueOf(nominal_cicilan)));
        nominalDibayarkanPinjaman.setText(fn.formatIntRupiah(String.valueOf(nominal_dibayar)));
        biayaAdminPinjaman.setText(fn.formatIntRupiah(String.valueOf(biaya_admin)));
        biayaProvisiPinjaman.setText(fn.formatIntRupiah(String.valueOf(biaya_provisi)));
        bungaBerjalanPinjaman.setText(fn.formatIntRupiah(String.valueOf(bungaberjalan)));
        danaDiterimaPinjaman.setText(fn.formatIntRupiah(String.valueOf(pinjaman_diterima)));
        cicilanPerbulanPinjaman.setText(fn.formatIntRupiah(String.valueOf(cicilan_perbulan)));
        biayaTransferPinjaman.setText((fn.formatIntRupiah(String.valueOf(biaya_transfer))));
      }
    });

    tenor.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listBulanPinjaman.clear();
        final AlertDialog.Builder builderBulan = new AlertDialog.Builder(FormLoan.this, R.style.DialogLoan);
        final LayoutInflater inflater = FormLoan.this.getLayoutInflater();
        final View viewBulan = inflater.inflate(R.layout.bottom_sheet_bulan, null, false);
        builderBulan.setView(viewBulan);
        listDataBulanPinjaman = (ListView) viewBulan.findViewById(R.id.list_Bulan);
        int cBulan = 1;
        for (int i = 0; i < tenorMaxPinjaman; i++){
          BulanPinjamanDataSet bulanPinjamanDataSet = new BulanPinjamanDataSet();
          bulanPinjamanDataSet.setBulan(cBulan++);
          listBulanPinjaman.add(bulanPinjamanDataSet);
        }
        bulanPinjamanAdapter = new BulanPinjamanAdapter(FormLoan.this, listBulanPinjaman);
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



    penjamin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listPenjamin.clear();
        final AlertDialog.Builder builder = new AlertDialog.Builder(FormLoan.this, R.style.DialogLoan);
        final LayoutInflater inflater = FormLoan.this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.bottom_sheet_penjamin, null, false);
        listDataPenjamin = (ListView) view.findViewById(R.id.list_Penjamin);
        builder.setView(view);

        vor = new VolleyObjectResult() {

          @Override
          public void resSuccess(String requestType, JSONObject response) {
            try {
              int id_count = 0;
              JSONArray jsonArray = response.getJSONArray("data");
              if(jsonArray.length() > id_count) {
                for (int i = 0; i < jsonArray.length(); i++) {
                  JSONObject jsonObject = jsonArray.getJSONObject(i);

                  PenjaminLoanDataSet pds = new PenjaminLoanDataSet();
                  pds.setId(jsonObject.getString("id"));
                  pds.setName(jsonObject.getString("name"));
                  pds.setUsername(jsonObject.getString("username"));
                  listPenjamin.add(pds);
                }
              }else{
                Toast.makeText(getApplicationContext(), "Penjamin belum tersedia", Toast.LENGTH_LONG).show();
              }

            } catch (JSONException e) {
              e.printStackTrace();
            }

            penjaminLoanAdapter = new PenjaminLoanAdapter(FormLoan.this, listPenjamin);
            penjaminLoanAdapter.notifyDataSetChanged();
            listDataPenjamin.setAdapter(penjaminLoanAdapter);
          }

          @Override
          public void resError(String requestType, VolleyError error) {
            new KAlertDialog(FormLoan.this, KAlertDialog.WARNING_TYPE)
              .setTitleText("Oops...")
              .setContentText("Network connection problem")
              .setConfirmText("OK")
              .show();
          }
        };
        vos = new VolleyObjectService(vor, getApplicationContext());
        vos.getJsonObject("GETCALL", urlPenjamin);
        final AlertDialog alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        alertDialog.show();
        listDataPenjamin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String name_penjamin = listPenjamin.get(position).getName();
            penjamin.setText(name_penjamin);
            id_penjamin = listPenjamin.get(position).getId();
            alertDialog.dismiss();
          }
        });

      }
    });

    btnAjukan.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        SharedPreferences token = PreferenceManager
          .getDefaultSharedPreferences(FormLoan.this);

        HashMap dt = new HashMap();
        dt.put("penjamin", Integer.parseInt(id_penjamin));
        dt.put("value", nominal_cicilan);
        dt.put("loan_id", loanID);
        dt.put("tenor", lama_cicilan);
        final JSONObject data = new JSONObject(dt);

        final ACProgressFlower dialog = new ACProgressFlower.Builder(FormLoan.this)
          .direction(ACProgressConstant.DIRECT_CLOCKWISE)
          .themeColor(Color.WHITE)
          .text("Loading ...")
          .fadeColor(Color.DKGRAY).build();
        dialog.show();
        vor = new VolleyObjectResult() {

          @Override
          public void resSuccess(String requestType, JSONObject response) {

            try {

              if(response.getBoolean("error")){
                new KAlertDialog(FormLoan.this, KAlertDialog.ERROR_TYPE)
                  .setTitleText("Oops...")
                  .setContentText(response.getString("message"))
                  .setConfirmText("OK")
                  .show();
                dialog.dismiss();
                return;
              }

              new KAlertDialog(FormLoan.this, KAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success...")
                .setContentText("Terima kasih, pinjaman anda berhasil saat ini menunggu approval")
                .setConfirmText("OK")
                .show();

              dialog.dismiss();
              Intent intent = new Intent(FormLoan.this, ListLoan.class);
              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);

            } catch (Exception e) {
              new KAlertDialog(FormLoan.this, KAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText("Pengajuan pinjaman anda gagal!")
                .setConfirmText("OK")
                .show();
              dialog.dismiss();
            }
          }

          @Override
          public void resError(String requestType, VolleyError error) {
            new KAlertDialog(FormLoan.this, KAlertDialog.ERROR_TYPE)
              .setTitleText("Oops...")
              .setContentText("Network connection timeout!")
              .setConfirmText("OK")
              .show();
            dialog.dismiss();

          }
        };
        vos = new VolleyObjectService(vor, FormLoan.this);
        vos.postJsonObject("POSTCALL", urlPostPinjaman, data);
      }
    });


  }

  public void onRincianFailed() {
    new KAlertDialog(FormLoan.this, KAlertDialog.WARNING_TYPE)
      .setTitleText("Oops...")
      .setContentText("Pastikan semua form diisi!")
      .setConfirmText("OK")
      .show();
    btnRincian.setEnabled(true);
  }

  public boolean validate() {
    boolean valid = true;

    String sTenor = tenor.getText().toString();
    String sPenjamin = penjamin.getText().toString();
    String sNominal = nominal.getText().toString();

    if (sTenor.isEmpty()) {
      tenor.setError("Tenor wajib diisi!");
      valid = false;
    } else {
      tenor.setError(null);
    }

    if (sPenjamin.isEmpty()) {
      penjamin.setError("Penjamin wajib diisi!");
      valid = false;
    } else {
      penjamin.setError(null);
    }

    if (sNominal.isEmpty() || Integer.parseInt(sNominal) < 250000) {
      nominal.setError("Nominal tidak boleh kurang dari 25.000!");
      valid = false;
    } else {
      nominal.setError(null);
    }

    return valid;
  }

  public void onBackPressed()
  {
    // code here to show dialog
    super.onBackPressed();  // optional depending on your needs
    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    finish();
  }

}
