package id.co.bspguard.android.bravo.accounts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import id.co.bspguard.android.bravo.AlertDialog.KAlertDialog;
import id.co.bspguard.android.bravo.R;

import butterknife.ButterKnife;
import butterknife.BindView;
import id.co.bspguard.android.bravo.calculator.Calculator;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.MyTagHandler;
import id.co.bspguard.android.bravo.functions.VolleyArrayResult;
import id.co.bspguard.android.bravo.functions.VolleyArrayService;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.loan.FormLoan;
import id.co.bspguard.android.bravo.memberdeposit.DetailDeposit;

public class Signup extends AppCompatActivity {

    private List<PositionDataset> list_position = new ArrayList<PositionDataset>();
    private ListView listPosition;
    private PositionAdapter positionAdapter;

    private List<LocationKerjaDataSet> info_kerja = new ArrayList<LocationKerjaDataSet>();
    private ListView infoKerja;
    private LocationKerjaAdapter kerjaAdapter;

    private List<LocationKerjaDataSet> info_project = new ArrayList<LocationKerjaDataSet>();
    private ListView infoProject;
    private LocationKerjaAdapter projectAdapter;

    private List<PemotonganDataSet> list = new ArrayList<PemotonganDataSet>();
    private ListView listPotong;
    private PemotonganAdapter pemotonganAdapter;

    private RadioButton radioButton;

    VolleyObjectResult volleyObjectResult, vor = null;
    VolleyObjectService volleyObjectService, vos;

    VolleyArrayService vas;
    VolleyArrayService var = null;

    Fungsi fn = new Fungsi();

    String sukarela;
    String requestJabatan = fn.url()+"position";
    String url = fn.url();
    String requestLocationKerja = fn.url()+"getlocation";
    String urlproject = fn.url()+"getproject";

    String requestPolicyRegister = fn.url()+"global-policy/1";
    String requestIdCard = "http://api.myjson.com/bins/fk8s6";



    String listIdPosition, listNamePosition, listIdKerja, listNameKerja, listNameProject, listIdProject, radiopotong;
    ActionBar actionBar;
    EditText positionID, sPosition, kerjaID, sKerja, _nameText, _passwordText, _emailText, _nameNik, _ktp, _pil_pemotongan, _sukarela, _inputNominalPotong;
    RadioGroup _radio_potong;
    Button _signupButton;
    TextView _loginLink, _linkSLA;
    CheckBox _chhecbox_sla;
    Spinner regionSpinner;
    Spinner projectSpinner;
    private ProgressDialog pDialog;
    Boolean isLain = false;

        String pemotongan;
    @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);

//            regionSpinner = findViewById(R.id.regionspinner);
//            projectSpinner = findViewById(R.id.projectspinner);
//            displayLoader();
//            loadStateCityDetails();

            _nameText = (EditText) findViewById(R.id.input_name);
            _signupButton = (Button) findViewById(R.id.btn_signup);
            _passwordText = (EditText) findViewById(R.id.input_password);
            _emailText = (EditText) findViewById(R.id.input_email);
            _loginLink = (TextView) findViewById(R.id.link_login);
//            _nameJabatan = (EditText) findViewById(R.id.input_jabatan);
//            _nameKerja = (EditText) findViewById(R.id.input_kerja);
            _nameNik = (EditText) findViewById(R.id.input_nik);
            _ktp = (EditText) findViewById(R.id.input_ktp);
//            _nameproyek = (EditText) findViewById(R.id.input_proyek);

//            positionID = (EditText) findViewById(R.id.positionID);
//            positionID.setVisibility(View.GONE);

            _pil_pemotongan = (EditText) findViewById(R.id.pil_pemotongan);
            _sukarela = (EditText) findViewById(R.id.inputSukarela);
            _radio_potong = (RadioGroup) findViewById(R.id.radio_potong);
            _linkSLA = (TextView) findViewById(R.id.linkSLA);
            _chhecbox_sla = (CheckBox) findViewById(R.id.checkbox_sla);
            _inputNominalPotong = (EditText) findViewById(R.id.inputNominalPotong);

//            cardviewInputPotong = (CardView) findViewById(R.id.cardviewInputPotong);


//            _nameKerja.setVisibility(View.GONE);

            _signupButton.setEnabled(false);
            _chhecbox_sla.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if ( isChecked )
                    {
                        _signupButton.setEnabled(true);
                    }
                    else{
                        _signupButton.setEnabled(false);
                    }
                }
            });


            _radio_potong.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch(checkedId){
                        case R.id.radio_50:
                            radiopotong = "50000";
                            _inputNominalPotong.setVisibility(View.GONE);
                            isLain = false;
                            break;
                        case R.id.radio_100:
                            radiopotong = "100000";
                            _inputNominalPotong.setVisibility(View.GONE);
                            isLain = false;
                            break;
                        case R.id.radio_200:
                            radiopotong = "200000";
                            _inputNominalPotong.setVisibility(View.GONE);
                            isLain = false;
                            break;
                        case R.id.radio_lain:
                            radiopotong = "";
                            _inputNominalPotong.setVisibility(View.VISIBLE);
                            isLain = true;
                          break;
                    }
                }
            });

            _signupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signup();
                }
            });

            _loginLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Finish the registration screen and return to the Login activity
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }
            });

            _inputNominalPotong.addTextChangedListener(onTextChangedListener());
            _pil_pemotongan.setOnClickListener(new View.OnClickListener(){

            private String[] namePotong={"1x Gaji","2x Gaji"};
            private String[] potongVal={"1","2"};

            @Override
            public void onClick(View v) {
                list.clear();

                AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this, R.style.DialogTheme);
                LayoutInflater inflater = Signup.this.getLayoutInflater();
                View view = inflater.inflate(R.layout.list_potong, null, false);

                view.setPadding(10,10,10,10);
                listPotong = (ListView) view.findViewById(R.id.listPotong);
                builder.setView(view);

                for (int i=0; i<namePotong.length; i++){
                    PemotonganDataSet pds = new PemotonganDataSet();
                    pds.setId_pemotongan(potongVal[i]);
                    pds.setName_pemotongan(namePotong[i]);
                    list.add(pds);
                }

                pemotonganAdapter = new PemotonganAdapter(Signup.this, list);
                pemotonganAdapter.notifyDataSetChanged();
                listPotong.setAdapter(pemotonganAdapter);

                final AlertDialog alertDialog = builder.create();
                alertDialog.setTitle(Html.fromHtml("<font color='#FF7F27'>Simpanan pokok dipotong sebanyak :</font>"));
                Window window = alertDialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                alertDialog.show();
                listPotong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String lnp = list.get(position).getName_pemotongan();
                        String lnid = list.get(position).getId_pemotongan();
                        _pil_pemotongan.setText(lnp);
                        pemotongan = lnid;
                        alertDialog.dismiss();
                    }
                });

            }
        });

        _linkSLA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });
        }

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

        public void signup() {
            if (!validate()) {
                onSignupFailed();
              new KAlertDialog(Signup.this, KAlertDialog.WARNING_TYPE)
                .setTitleText("Warning...")
                .setContentText("Isi semua form dengan benar")
                .setConfirmText("OK")
                .show();
                return;
            }

          final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
            .themeColor(Color.WHITE)
            .text("Loading ...")
            .fadeColor(Color.DKGRAY).build();
          dialog.show();

            _signupButton.setEnabled(false);
            String email = _emailText.getText().toString();
            String nama = _nameText.getText().toString();
            String nik = _nameNik.getText().toString();
            String ktp = _ktp.getText().toString();


            EditText pass = (EditText)findViewById(R.id.input_password);
            String password = pass.getText().toString();

            if(_sukarela.getText().toString() == ""){
              sukarela = "0";
            }else{
              sukarela = _sukarela.getText().toString();
            }

            if(radiopotong == ""){
                radiopotong = _inputNominalPotong.getText().toString();
            }

            HashMap<String, String> dt = new HashMap<String, String>();
            dt.put("fullname", nama);
            dt.put("username", nik);
            dt.put("email", email);
            dt.put("password", password);
            dt.put("nik_bsp", nik);
            dt.put("nik", ktp);
            dt.put("sukarela", sukarela);
            dt.put("wajib", radiopotong);
            dt.put("pemotongan", pemotongan);

            final JSONObject data = new JSONObject(dt);

            vor = new VolleyObjectResult() {
                @Override
                public void resSuccess(String requestType, JSONObject response) {
                    try {
                        if(response.getBoolean("error")){
                          new KAlertDialog(Signup.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText(response.getString("message"))
                            .show();
                            dialog.dismiss();
                          _signupButton.setEnabled(true);
                        }else{
                          new KAlertDialog(Signup.this, KAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success...")
                            .setContentText("Pendaftaran berhasil tunggu tim kami mengubungi")
                            .show();
                          dialog.dismiss();
                          _signupButton.setEnabled(true);
                          dialog.dismiss();
                          Intent loginPage = new Intent(Signup.this, Login.class);
                          startActivity(loginPage);
                        }


                    } catch (Exception e) {

                        e.printStackTrace();
                      dialog.dismiss();
                      new KAlertDialog(Signup.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Terjadi kesalahan !!")
                        .setConfirmText("OK")
                        .show();
                    }
                }

                @Override
                public void resError(String requestType, VolleyError error) {
                  dialog.dismiss();
                }
            };
            vos = new VolleyObjectService(vor, Signup.this);
            vos.postJsonObject("POSTCALL", url+"register", data);
        }



        public void onSignupSuccess() {
            _signupButton.setEnabled(true);
            setResult(RESULT_OK, null);
            finish();
        }

        public void onSignupFailed() {
            _signupButton.setEnabled(true);
        }

        public boolean validate() {
            boolean valid = true;

            String name = _nameText.getText().toString();
            String email = _emailText.getText().toString();
            String password = _passwordText.getText().toString();
            String noKtp = _ktp.getText().toString();
            String nikBsp = _nameNik.getText().toString();
            String inputNominal = _inputNominalPotong.getText().toString();

            if((isLain && inputNominal.isEmpty()) || (isLain && Integer.parseInt(inputNominal) < 50000)){
              _inputNominalPotong.setError("nominal lain harus lebih besar dari 50.000");
            }

            if (name.isEmpty() || name.length() < 5) {
                _nameText.setError("nama minimal 5 karakter");
                valid = false;
            } else {
                _nameText.setError(null);
            }

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _emailText.setError("format email salah");
                valid = false;
            } else {
                _emailText.setError(null);
            }

            if (password.isEmpty() || password.length() < 6) {
                _passwordText.setError("password minimal 6 karakter");
                valid = false;
            } else {
                _passwordText.setError(null);
            }

            if (noKtp.isEmpty() || noKtp.length() < 14) {
              _ktp.setError("nomor ktp minimal 14 karakter");
              valid = false;
            } else {
              _ktp.setError(null);
            }

            if (nikBsp.isEmpty() || nikBsp.length() < 5) {
              _nameNik.setError("nik tidak boleh kosong");
              valid = false;
            } else {
              _nameNik.setError(null);
            }

            return valid;
        }

    private PopupWindow pw;
    private void showPopup() {
        volleyObjectResult = new VolleyObjectResult() {
            @Override
            public void resSuccess(String requestType, JSONObject response) {

                try {
                    JSONObject jsonarray = response.getJSONObject("data");

                    LayoutInflater inflater = (LayoutInflater)
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.popup_sla_register,
                            (ViewGroup) findViewById(R.id.popup_1));
                  ShimmerFrameLayout shimmer = layout.findViewById(R.id.shimmer);
                  shimmer.setVisibility(View.GONE);
                    pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,true);
                    pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
                    WebView sla = (WebView) layout.findViewById(R.id.txtView);
                    sla.setBackgroundColor(Color.TRANSPARENT);
                    sla.loadDataWithBaseURL(null, jsonarray.getString("description"), "text/html", "utf-8", null);
                    Button agree = (Button) layout.findViewById(R.id.agree);
                    Button dontagree = (Button) layout.findViewById(R.id.dont_agree);
                    agree.setOnClickListener(agree_button);
                    dontagree.setOnClickListener(dontagree_button);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            @Override
            public void resError(String requestType, VolleyError error) {
              new KAlertDialog(Signup.this, KAlertDialog.WARNING_TYPE)
                .setTitleText("Oops...")
                .setContentText("Network connection problem")
                .setConfirmText("OK")
                .show();
            }
        };

        volleyObjectService = new VolleyObjectService(volleyObjectResult, Signup.this);
        volleyObjectService.getJsonObject("GETCALL", requestPolicyRegister);

    }

    private View.OnClickListener agree_button = new View.OnClickListener() {
        public void onClick(View v) {
            _chhecbox_sla = (CheckBox) findViewById(R.id.checkbox_sla);
            _chhecbox_sla.setChecked(true);
            pw.dismiss();
        }
    };

    private View.OnClickListener dontagree_button = new View.OnClickListener() {
        public void onClick(View v) {
            _chhecbox_sla = (CheckBox) findViewById(R.id.checkbox_sla);
            _chhecbox_sla.setChecked(false);
            pw.dismiss();
        }
    };



    private void displayLoader() {
        pDialog = new ProgressDialog(Signup.this);
        pDialog.setMessage("Loading Data.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    /**
     * Helps in downloading the state and city details
     * and populating the spinner
     */
    private void loadStateCityDetails() {
        final List<infoKerjaDataSet> regionList = new ArrayList<>();
        final List<String> region = new ArrayList<>();
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, requestLocationKerja, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseArray) {
                        pDialog.dismiss();
                        try {
                            //Parse the JSON response array by iterating over it
                            List<String> regionLists = new ArrayList<>();
                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject response = responseArray.getJSONObject(i);
                                regionLists.add(responseArray.getString(i));

                                String regionName = response.getString("name_area");
                                JSONArray project = response.getJSONArray("project");
                                JSONArray branch = response.getJSONArray("branch");

                                List<String> projectLists = new ArrayList<>();
                                List<String> locationLists = new ArrayList<>();
                                List<String> branchLists = new ArrayList<>();

                                for (int b = 0; b < branch.length(); b++){
                                    branchLists.add(branch.getString(b));
                                }

                                for (int j = 0; j < project.length(); j++) {
                                    projectLists.add(project.getString(j));

                                    JSONObject locationList = project.getJSONObject(j);
                                    JSONArray locations = locationList.getJSONArray("locations");
                                    for(int l= 0; l < locations.length(); l++){
                                        locationLists.add(locations.getString(l));
                                    }

                                }
                                regionList.add(new infoKerjaDataSet(regionName, regionLists, projectLists, locationLists, branchLists));
                                region.add(regionName);

                            }
                            final infoKerjaAdapter stateAdapter = new infoKerjaAdapter(Signup.this,
                                    R.layout.project_list, R.id.projectSpinnerText, regionList);
                            regionSpinner.setAdapter(stateAdapter);

                            regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    //Populate City list to the second spinner when
                                    // a state is chosen from the first spinner
                                    infoKerjaDataSet regionDetail = stateAdapter.getItem(position);
                                    List<String> regionList = regionDetail.getRegion();
                                    ArrayAdapter citiesAdapter = new ArrayAdapter<>(Signup.this,
                                            R.layout.region_list, R.id.regionSpinnerText, regionList);
                                    projectSpinner.setAdapter(citiesAdapter);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //Display error message whenever an error occurs
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

}
