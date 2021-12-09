package id.co.bspguard.android.bravo.home;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import de.hdodenhof.circleimageview.CircleImageView;
import id.co.bspguard.android.bravo.AlertDialog.KAlertDialog;
import id.co.bspguard.android.bravo.ConnectionLost;
import id.co.bspguard.android.bravo.MainActivity;
import id.co.bspguard.android.bravo.accounts.Login;
import id.co.bspguard.android.bravo.bankmember.BankMember;
import id.co.bspguard.android.bravo.calculator.Calculator;
import id.co.bspguard.android.bravo.contact.Contact;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyArrayResult;
import id.co.bspguard.android.bravo.functions.VolleyArrayService;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.loan.ApprovalLoan;
import id.co.bspguard.android.bravo.loan.FormLoan;
import id.co.bspguard.android.bravo.loan.Loan;
import id.co.bspguard.android.bravo.memberdeposit.ListDeposit;
import id.co.bspguard.android.bravo.memberloan.FormChangeDeposit;
import id.co.bspguard.android.bravo.memberloan.ListLoan;
import id.co.bspguard.android.bravo.memberprofile.MemberProfile;
import id.co.bspguard.android.bravo.memberresign.MainResign;
import id.co.bspguard.android.bravo.memberretrievedeposit.MainRetrieveDeposit;
import id.co.bspguard.android.bravo.notifications.MainNotifications;

public class ActivityAwal extends Fragment implements View.OnClickListener {

    private ViewPager viewPager;
    RequestQueue rq;
    List<SliderUtil> sliderImg;
    RecyclerView listLoan;
    Fungsi fn = new Fungsi();
    LinearLayout pinjaman, simpanan, pencairan, pengajuan, kalkulator, bank, hubungi, more, approvalLoan;
    String requestUrl = fn.url()+"slider-news";
    String mainDataUrl = fn.url()+"get-data-main";
    String getTopLoanUrl = fn.url()+"get-top-loan";
    String getTotalNotif = fn.url()+"count-notification";
    LinearLayout linearLayout;

    private static RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView, recyclerView2;
    List<MyData> data_list;
    List<MyDataTour> data_list_car;

    private List<TopLoanDataSet> list = new ArrayList<TopLoanDataSet>();
    private TopLoanAdapter loanAdapter;

    VolleyArrayResult volleyArrayResult = null;
    VolleyArrayService volleyArrayService;
    VolleyObjectResult volleyObjectResult, vObjectUserResult, topLoanObjectResult = null;
    VolleyObjectService volleyObjectService, vObjectUserService, topLoanObjectService;

    Animation atg;
    boolean connected;
    public static ActivityAwal newInstance() {
        ActivityAwal fragment = new ActivityAwal();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      connected = fn.isActiveNetwork(getActivity());
      if(!connected){
        startActivity(new Intent(getActivity(), ConnectionLost.class));
      }
    }

    Button btnpinjaman, btnsimpanan, btnresign, btnkalukaltor, btncontact, btndatabank, btnlogout, btnpencairan, btnpengajuan;
    TextView viewProfile, nameProfile, nomorKoperasi, totalsimpanan, totalpinjaman;
    ImageView btnnotification;
    ImageView imgProfile;
    CircleImageView profile_image;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_awal, container, false);

      Dexter.withContext(getContext())
        .withPermissions(
          Manifest.permission.CAMERA,
          Manifest.permission.INTERNET,
          Manifest.permission.ACCESS_NETWORK_STATE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener() {
        @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
        @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
      }).check();

        viewPager = (ViewPager) view.findViewById(R.id.vPager);
        final NotificationBadge badge = view.findViewById(R.id.badge);

        atg = AnimationUtils.loadAnimation(getActivity(), R.anim.atg);

        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        nameProfile = (TextView) view.findViewById(R.id.name);
        nomorKoperasi = (TextView) view.findViewById(R.id.no_member);
        viewProfile = (TextView) view.findViewById(R.id.viewprofile);
        imgProfile = (ImageView) view.findViewById(R.id.profile_image);
        totalpinjaman = (TextView) view.findViewById(R.id.total_pinjaman);
        totalsimpanan = (TextView) view.findViewById(R.id.total_simpanan);

//        btnpinjaman = (Button) view.findViewById(R.id.btn_pinjaman);
//        btnsimpanan = (Button) view.findViewById(R.id.btn_simpanan);
//        btnresign = (Button) view.findViewById(R.id.btn_resign);
//        btnpencairan = (Button) view.findViewById(R.id.btn_pencairan);
//        btnkalukaltor = (Button) view.findViewById(R.id.kalukaltorPinjaman);
//        btndatabank = (Button) view.findViewById(R.id.databank);
//        btnlogout = (Button) view.findViewById(R.id.logout);
//        btnpengajuan = (Button) view.findViewById(R.id.btn_pengajuan);

//        btncontact = (Button) view.findViewById(R.id.contact);

        btnnotification = (ImageView) view.findViewById(R.id.notificationBtn);
        linearLayout = (LinearLayout) view.findViewById(R.id.top5LoanTitle);


        pinjaman = (LinearLayout) view.findViewById(R.id.lPinjaman);
        simpanan = (LinearLayout) view.findViewById(R.id.lSimpanan);
        pencairan = (LinearLayout) view.findViewById(R.id.lPencairan);
        pengajuan = (LinearLayout) view.findViewById(R.id.lPengajuan);
        kalkulator = (LinearLayout) view.findViewById(R.id.lKalkulator);
        bank = (LinearLayout) view.findViewById(R.id.lBank);
        hubungi = (LinearLayout) view.findViewById(R.id.lHubungi);
        more = (LinearLayout) view.findViewById(R.id.lMore);

        listLoan = (RecyclerView) view.findViewById(R.id.recyclerviewTopLoan);
        listLoan.setHasFixedSize(true);
        listLoan.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        listLoan.computeHorizontalScrollExtent();




        viewProfile.setOnClickListener(this);
//        btnpinjaman.setOnClickListener(this);
//        btnsimpanan.setOnClickListener(this);
//        btnkalukaltor.setOnClickListener(this);
//        btncontact.setOnClickListener(this);
//        btndatabank.setOnClickListener(this);
//        btnlogout.setOnClickListener(this);
//        btnresign.setOnClickListener(this);
//        btnpencairan.setOnClickListener(this);
//        btnpengajuan.setOnClickListener(this);
        btnnotification.setOnClickListener(this);

        pinjaman.setOnClickListener(this);
        simpanan.setOnClickListener(this);
        pencairan.setOnClickListener(this);
        pengajuan.setOnClickListener(this);
        kalkulator.setOnClickListener(this);
        bank.setOnClickListener(this);
        hubungi.setOnClickListener(this);
        more.setOnClickListener(this);
        //approvalLoan.setOnClickListener(this);


      volleyObjectResult = new VolleyObjectResult() {

        @Override
        public void resSuccess(String requestType, JSONObject response) {
          try {
            JSONObject data = response.getJSONObject("data");
            int notificationNumber = data.getInt("totalUnread");
            badge.setNumber(notificationNumber);

          } catch (JSONException e) {
            e.printStackTrace();
          }
        }

        @Override
        public void resError(String requestType, VolleyError error) {
          Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
//              startActivity(new Intent(getContext(), ConnectionLost.class));

        }
      };
      volleyObjectService = new VolleyObjectService(volleyObjectResult, getContext());
      volleyObjectService.getJsonObject("GETCALL", getTotalNotif);


        rq = Volley.newRequestQueue(this.getActivity());
        sliderImg = new ArrayList<>();
        data_list = new ArrayList<>();
        data_list_car = new ArrayList<>();
        //========================================== Image Slider Atas ==============================
        volleyObjectResult = new VolleyObjectResult() {

            @Override
            public void resSuccess(String requestType, JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String urlImage = fn.urlImageNews(jsonObject.getString("image_name"));
                        SliderUtil sliderUtil = new SliderUtil();
                        sliderUtil.setSliderImgUrl(urlImage);
                        sliderImg.add(sliderUtil);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                HomeSliderPagerAdapter hspa = new HomeSliderPagerAdapter(sliderImg, getActivity());
                hspa.notifyDataSetChanged();
                viewPager.setAdapter(hspa);
            }

            @Override
            public void resError(String requestType, VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
//              startActivity(new Intent(getContext(), ConnectionLost.class));

            }
        };
        volleyObjectService = new VolleyObjectService(volleyObjectResult, getContext());
        volleyObjectService.getJsonObject("GETCALL", requestUrl);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new myTimerTask(), 1000, 4000);

        this.setData();
        return view;
    }

    private void setData() {
      final String access_token = fn.getdatalogin(getActivity());
      String userid = fn.getDataMember(getContext());
      String permision = fn.getDataPermission(getContext());
      HashMap<Object, Object> headers = new HashMap<Object, Object>();
      headers.put("user_id", userid);

      final JSONObject data = new JSONObject(headers);

      vObjectUserResult = new VolleyObjectResult() {

          @Override
          public void resSuccess(String requestType, JSONObject response) {
              try {
                  JSONObject jsonObject = response.getJSONObject("member");


                  nameProfile.setText(jsonObject.getString("full_name"));
                  nomorKoperasi.setText(jsonObject.getString("nik_koperasi"));
                  totalsimpanan.setText(jsonObject.getString("total_deposit"));
                  totalpinjaman.setText(jsonObject.getString("total_loan"));
                  String image = jsonObject.getString("picture");

                  Glide.with(getActivity()).asBitmap().load(image)
                          .diskCacheStrategy(DiskCacheStrategy.ALL)
                          .placeholder(R.mipmap.ic_launcher)
                          .into(imgProfile);


              } catch (JSONException e) {
                  e.printStackTrace();
              }

              HomeSliderPagerAdapter hspa = new HomeSliderPagerAdapter(sliderImg, getActivity());
              hspa.notifyDataSetChanged();
              viewPager.setAdapter(hspa);
          }

          @Override
          public void resError(String requestType, VolleyError error) {
            new KAlertDialog(getContext(), KAlertDialog.WARNING_TYPE)
              .setTitleText("Oops...")
              .setContentText("Network connection problem")
              .setConfirmText("OK")
              .show();


          }
      };
      vObjectUserService = new VolleyObjectService(vObjectUserResult, getContext());
      vObjectUserService.postJsonObject("GETCALL", mainDataUrl, data);



      topLoanObjectResult = new VolleyObjectResult() {
        @Override
        public void resSuccess(String requestType, JSONObject response) {
          try {
            JSONArray jsonArray = response.getJSONArray("data");

            int id_count = 0;
            list.clear();
            if(jsonArray.length() > id_count) {
              for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject msLoans = jsonObject.getJSONObject("ms_loans");

                TopLoanDataSet data = new TopLoanDataSet();
                data.setId(msLoans.getString("id"));
                data.setLogo(msLoans.getString("logo"));
                data.setLoan_name(msLoans.getString("loan_name"));
                data.setPlafon(msLoans.getString("plafon"));
                data.setDescription(msLoans.getString("description"));
                data.setRate_of_interest(msLoans.getString("rate_of_interest"));
                data.setTenor(msLoans.getString("tenor"));
                if(msLoans.getInt("plafon") > 0)
                {
                  data.setBatas_pinjaman(msLoans.getInt("plafon"));
                }else{
                  data.setBatas_pinjaman(0);
                }
                data.setBiaya_admin(msLoans.getInt("biaya_admin"));
                data.setBiaya_transfer(msLoans.getInt("biaya_transfer"));
                data.setBunga(msLoans.getDouble("rate_of_interest"));
                data.setBunga_berjalan(msLoans.getDouble("biaya_bunga_berjalan"));
                data.setProvisi(msLoans.getDouble("provisi"));


                list.add(data);
              }
              loanAdapter = new TopLoanAdapter(getContext(), list);
              loanAdapter.notifyDataSetChanged();
              listLoan.setAdapter(loanAdapter);

              listLoan.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                  super.onScrollStateChanged(recyclerView, newState);
//                  AutoTransition autoTransition = new AutoTransition();
//                  autoTransition.setDuration(1000);
                  Slide slide = new Slide();
                  slide.setDuration(600);
                  if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    int position = recyclerView.computeHorizontalScrollOffset();
                    if(position > 30){
                      slide.setSlideEdge(Gravity.START);
                      TransitionManager.beginDelayedTransition(linearLayout, slide);
                        linearLayout.setVisibility(View.GONE);
                    }else if(position < 10){
                      slide.setSlideEdge(Gravity.START);
                      TransitionManager.beginDelayedTransition(linearLayout, slide);
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                  }
                }
              });

            }else {
              Thread.sleep(2000);
              Toast.makeText(getContext(), "Pinjaman belum tersedia", Toast.LENGTH_LONG).show();
            }

          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        @Override
        public void resError(String requestType, VolleyError error) {
//          Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
//          startActivity(new Intent(getContext(), ConnectionLost.class));

        }
      };
      topLoanObjectService = new VolleyObjectService(topLoanObjectResult, getContext());
      topLoanObjectService.getJsonObject("GETCALL", getTopLoanUrl);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_simpanan:
          case R.id.lSimpanan:
            Intent intentSimpanan = new Intent(getActivity(), ListDeposit.class);
                getActivity().startActivity(intentSimpanan);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
//            case R.id.btn_pinjaman:
          case R.id.lPinjaman:
            Intent intentPinjaman = new Intent(getActivity(), ListLoan.class);
                getActivity().startActivity(intentPinjaman);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case R.id.viewprofile:
                Intent intentVProfile = new Intent(getActivity(), MemberProfile.class);
              Pair[] pairs = new Pair[1];
              pairs[0] = new Pair<View, String>(imgProfile, "goToProfile");
              ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairs);
                getActivity().startActivity(intentVProfile, options.toBundle());
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
//            case R.id.btn_resign:
          case R.id.lResign:
            Intent intentResign = new Intent(getActivity(), MainResign.class);
              getActivity().startActivity(intentResign);
              getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
              break;
//            case R.id.btn_pencairan:
          case R.id.lPencairan:
            Intent intentPencairan = new Intent(getActivity(), MainRetrieveDeposit.class);
              getActivity().startActivity(intentPencairan);
              getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
              break;
//            case R.id.kalukaltorPinjaman:
          case R.id.lKalkulator:
                Intent intentKalkulator = new Intent(getActivity(), Calculator.class);
                getActivity().startActivity(intentKalkulator);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
//            case R.id.contact:
          case R.id.lHubungi:
            Intent intentContact = new Intent(getActivity(), Contact.class);
                getActivity().startActivity(intentContact);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
//            case R.id.databank:
          case R.id.lBank:
            Intent intentBank = new Intent(getActivity(), BankMember.class);
                getActivity().startActivity(intentBank);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
//          case R.id.btn_pengajuan:
          case R.id.lPengajuan:
            Intent intentPengajuan = new Intent(getActivity(), Loan.class);
                getActivity().startActivity(intentPengajuan);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            break;
          case R.id.lApproveLoan:
            Intent intentApprovalLoan = new Intent(getActivity(), ApprovalLoan.class);
            getActivity().startActivity(intentApprovalLoan);
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            break;
          case R.id.lChangeDeposit:
            Intent intentChangeDeposit = new Intent(getActivity(), FormChangeDeposit.class);
            getActivity().startActivity(intentChangeDeposit);
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            break;
          case R.id.lMore:
            final BottomSheetDialog bt = new BottomSheetDialog(getActivity(), R.style.DialogLoan);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_sheet_menu,null);
            LinearLayout bmPinjaman = (LinearLayout) view.findViewById(R.id.lPinjaman);
            LinearLayout bmSimpanan = (LinearLayout) view.findViewById(R.id.lSimpanan);
            LinearLayout bmPencairan = (LinearLayout) view.findViewById(R.id.lPencairan);
            LinearLayout bmKalkulator = (LinearLayout) view.findViewById(R.id.lKalkulator);
            LinearLayout bmResign = (LinearLayout) view.findViewById(R.id.lResign);
            LinearLayout bmPengajuan = (LinearLayout) view.findViewById(R.id.lPengajuan);
            LinearLayout bmLogout = (LinearLayout) view.findViewById(R.id.lLogout);
            LinearLayout bmHubungi = (LinearLayout) view.findViewById(R.id.lHubungi);
            LinearLayout bmBank = (LinearLayout) view.findViewById(R.id.lBank);
            LinearLayout bmApprovalLoan = (LinearLayout) view.findViewById(R.id.lApproveLoan);
            LinearLayout bmChangeDeposit = (LinearLayout) view.findViewById(R.id.lChangeDeposit);

            bmPinjaman.setOnClickListener(this::onClick);
            bmSimpanan.setOnClickListener(this::onClick);
            bmPencairan.setOnClickListener(this::onClick);
            bmKalkulator.setOnClickListener(this::onClick);
            bmResign.setOnClickListener(this::onClick);
            bmPengajuan.setOnClickListener(this::onClick);
            bmLogout.setOnClickListener(this::onClick);
            bmHubungi.setOnClickListener(this::onClick);
            bmBank.setOnClickListener(this::onClick);
            bmApprovalLoan.setOnClickListener(this::onClick);
            bmChangeDeposit.setOnClickListener(this::onClick);

            bt.setContentView(view);
            bt.show();
            break;
          case R.id.notificationBtn:
            Intent intentNotification = new Intent(getActivity(), MainNotifications.class);
            getActivity().startActivity(intentNotification);
            getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            break;
//          case R.id.logout:
          case R.id.lLogout:

            String datauser = fn.getdatalogin(getActivity());
                if(datauser != null) {

                    final SharedPreferences sesdata = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor lds = sesdata.edit();
                    lds.clear();
                    lds.commit();


                    Intent intentLogin = new Intent(getActivity(), Login.class);
                    intentLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getActivity().startActivity(intentLogin);
                    getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }

                break;
            default:
                break;
        }
    }

    public class myTimerTask extends TimerTask {
        @Override
        public void run() {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < sliderImg.size() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            };
        }
    }

}
