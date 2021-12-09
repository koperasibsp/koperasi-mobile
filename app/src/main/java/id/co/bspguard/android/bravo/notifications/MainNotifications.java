package id.co.bspguard.android.bravo.notifications;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.accounts.Login;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.OnLoadMoreListener;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;

public class MainNotifications extends AppCompatActivity {
  Fungsi fn = new Fungsi();

  int page = 1;
  int limit = 10;

  String urlNotification = fn.url()+"notifications";

  VolleyObjectService vos;
  VolleyObjectResult vor;

  ProgressBar progressBar;
  RecyclerView listNotification;
  TextView tvEmptyView;
  ImageView notfoundNotification;

  private List<NotificationDataSet> list = new ArrayList<NotificationDataSet>();
  private NotificationAdapter notificationAdapter;
  boolean loading = false;
  LinearLayoutManager linearLayoutManager;
  protected Handler handler;
  int pastVisibleItems, visibleItemCount, totalItemCount;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_notification);

    progressBar = (ProgressBar) findViewById(R.id.proBar);
    listNotification = (RecyclerView) findViewById(R.id.listNotification);
    notfoundNotification = (ImageView) findViewById(R.id.notfound);
//    tvEmptyView = (TextView) findViewById(R.id.empty_view);

    linearLayoutManager = new LinearLayoutManager(getApplicationContext());
    listNotification.setLayoutManager(linearLayoutManager);

    notificationAdapter = new NotificationAdapter(this, list, this);
    listNotification.setAdapter(notificationAdapter);

    listNotification.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0)
        {
          visibleItemCount = linearLayoutManager.getChildCount();
          totalItemCount = linearLayoutManager.getItemCount();
          pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

          if (!loading) {
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
              loading = true;
              page = page+1;
              loadData();
            }
          }
        }
      }
    });



      loadData();


  }

  private void loadData() {

    final ACProgressFlower dialog = new ACProgressFlower.Builder(MainNotifications.this)
      .direction(ACProgressConstant.DIRECT_CLOCKWISE)
      .themeColor(Color.WHITE)
      .text("Loading ...")
      .fadeColor(Color.DKGRAY).build();

    dialog.show();

    vor = new VolleyObjectResult() {
      @Override
      public void resSuccess(String requestType, JSONObject response) {
        try {
//          list.clear();
          JSONArray jsonArray = response.getJSONArray("data");

            int id_count = 0;
            if(jsonArray.length() > id_count) {
              for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject dataNotification = jsonObject.getJSONObject("data");
                JSONObject content = dataNotification.getJSONObject("content");
                NotificationDataSet notificationSet = new NotificationDataSet();
                notificationSet.setId(jsonObject.getString("id"));
                notificationSet.setRead_at(jsonObject.getString("read_at"));
                notificationSet.setTitle(content.getString("title"));
                notificationSet.setDescription(content.getString("description"));
                notificationSet.setNextUrl(response.getString("next_page_url"));
                notificationSet.setType(dataNotification.getString("url"));
                notificationSet.setObject(content.getJSONObject("object"));
                list.add(notificationSet);
              }
              dialog.dismiss();

              notificationAdapter.notifyDataSetChanged();
              loading = false;
            }else{
              if(Integer.parseInt(response.getString("total")) == 0){
                dialog.dismiss();
                notfoundNotification.setVisibility(View.VISIBLE);
                listNotification.setVisibility(View.GONE);

              }

            }
          dialog.dismiss();

//          Toast.makeText(getApplicationContext(), "?page="+page+"&limit="+limit, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
          dialog.dismiss();

          e.printStackTrace();

        }
      }
      @Override
      public void resError(String requestType, VolleyError error) {
        dialog.dismiss();

        Toast.makeText(MainNotifications.this, error.toString(), Toast.LENGTH_LONG).show();

      }
    };

    vos = new VolleyObjectService(vor, MainNotifications.this);
    vos.getJsonObject("GETCALL", urlNotification+"?page="+page+"&limit="+limit);

  }
}
