package id.co.bspguard.android.bravo.memberprofile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.bspguard.android.bravo.ConnectionLost;
import id.co.bspguard.android.bravo.MainActivity;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.accounts.Signup;
import id.co.bspguard.android.bravo.changeavatar.ChangeAvatar;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.memberretrievedeposit.MainRetrieveDeposit;
import id.co.bspguard.android.bravo.news.MainNews;
import id.co.bspguard.android.bravo.news.NewsAdapter;
import id.co.bspguard.android.bravo.news.NewsDataSet;
import id.co.bspguard.android.bravo.setting.AccountSetting;

public class MemberProfile extends AppCompatActivity {
  LinearLayout personalinfoLayout, memberinfoLayout;
  TextView personalinfotxt, memberinfotxt, nameMember, posisiMember, nik_ktp, nik_koperasi, nik_bsp, no_telp, alamat, email, brgabung, kontrak_awal, kontrak_akhir, status, region, proyek, cabang;
  ImageView imgProfile, setting;

  VolleyObjectResult volleyObjectResult, vor = null;
  VolleyObjectService volleyObjectService, vos;
  Fungsi fungsi = new Fungsi();
  String url = fungsi.url()+"my-profile";
  private List<NewsDataSet> list = new ArrayList<NewsDataSet>();
  private NewsAdapter newsAdapter;
  JSONObject data = null;
  private ShimmerFrameLayout shimmerNews;
  boolean connected;
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_member_profile);

    connected = fungsi.isActiveNetwork(getApplication());
    if(!connected){
      startActivity(new Intent(getApplication(), ConnectionLost.class));
      finish();
    }

    personalinfoLayout = (LinearLayout) findViewById(R.id.personalinfo);
    memberinfoLayout = (LinearLayout) findViewById(R.id.member_info);

    personalinfotxt = (TextView) findViewById(R.id.personalinfobtn);
    memberinfotxt = (TextView) findViewById(R.id.memberinfobtn);

    personalinfoLayout.setVisibility(View.VISIBLE);
    memberinfoLayout.setVisibility(View.GONE);

    setting = (ImageView) findViewById(R.id.setting);
    setting.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), AccountSetting.class);
        startActivity(intent);
        return;
      }
    });

    personalinfotxt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        personalinfoLayout.setVisibility(View.VISIBLE);
        memberinfoLayout.setVisibility(View.GONE);
        personalinfotxt.setTextColor(getResources().getColor(R.color.blue));
        memberinfotxt.setTextColor(getResources().getColor(R.color.grey));
      }
    });
    memberinfotxt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        personalinfoLayout.setVisibility(View.GONE);
        memberinfoLayout.setVisibility(View.VISIBLE);
        personalinfotxt.setTextColor(getResources().getColor(R.color.grey));
        memberinfotxt.setTextColor(getResources().getColor(R.color.blue));
      }
    });
    nameMember = (TextView) findViewById(R.id.name);
    posisiMember = (TextView) findViewById(R.id.posisi);
    nik_ktp = (TextView) findViewById(R.id.nik_ktp);
    email = (TextView) findViewById(R.id.email);
    no_telp = (TextView) findViewById(R.id.no_telp);
    nik_bsp = (TextView) findViewById(R.id.nik_bsp);
    nik_koperasi = (TextView) findViewById(R.id.nik_koperasi);
    alamat = (TextView) findViewById(R.id.alamat);
    brgabung = (TextView) findViewById(R.id.tgl_bergabung);
    kontrak_awal = (TextView) findViewById(R.id.awal_kontrak);
    kontrak_akhir = (TextView) findViewById(R.id.akhir_kontrak);
    status = (TextView) findViewById(R.id.status);
    region = (TextView) findViewById(R.id.area);
    proyek = (TextView) findViewById(R.id.proyek);
    cabang = (TextView) findViewById(R.id.cabang);
    imgProfile = (ImageView) findViewById(R.id.profile_image);

    vor = new VolleyObjectResult() {
      @Override
      public void resSuccess(String requestType, JSONObject response) {
        //Toast.makeText(ListKereta.this, response.toString(), Toast.LENGTH_LONG).show();
        try {
          JSONObject object = response.getJSONObject("data");
          JSONObject position = object.getJSONObject("position");
          JSONObject area = object.getJSONObject("region");
          JSONObject project = object.getJSONObject("project");

          nameMember.setText(object.getString("full_name"));
          nik_koperasi.setText(object.getString("nik_koperasi"));
          posisiMember.setText(position.getString("name"));
          nik_ktp.setText(object.getString("nik"));
          alamat.setText(object.getString("address"));
          no_telp.setText(object.getString("phone_number"));
          email.setText(object.getString("email"));
          nik_bsp.setText(object.getString("nik_bsp"));
          brgabung.setText(object.getString("join_date"));

          proyek.setText(project.getString("project_name"));
          region.setText(area.getString("name_area"));

          kontrak_awal.setText(object.getString("start_date"));
          kontrak_akhir.setText(object.getString("end_date"));

          if(object.getInt("is_permanent") == 1){
            kontrak_awal.setText(object.getString("keterangan"));
            kontrak_akhir.setText(object.getString("keterangan"));
          }
          status.setText("Tidak Aktif");
          if(object.getBoolean("is_active") == true){
            status.setText("Aktif");
          }


          String image = object.getString("picture");

          Glide.with(getApplicationContext()).asBitmap().load(image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.mipmap.ic_launcher)
            .into(imgProfile);

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      @Override
      public void resError(String requestType, VolleyError error) {
//        View view = findViewById(R.id.coordinator);
//        String message = "Maaf data berita tidak ditemukan.";
//        fungsi.showSnackbar(view, message);
//        startActivity(new Intent(getApplicationContext(), ConnectionLost.class).putExtra("activity", "MemberProfile"));


      }
    };
    vos = new VolleyObjectService(vor, MemberProfile.this);
    vos.getJsonObject("GETCALL", url);



    imgProfile.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          Intent avatar = new Intent(MemberProfile.this, ChangeAvatar.class);
          startActivity(avatar);
          return;
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
