package id.co.bspguard.android.bravo.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.co.bspguard.android.bravo.ConnectionLost;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyArrayResult;
import id.co.bspguard.android.bravo.functions.VolleyArrayService;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;

public class MainNews extends AppCompatActivity {

    private ListView listNews;
    VolleyObjectResult volleyObjectResult, vor = null;
    VolleyObjectService volleyObjectService, vos;
    Fungsi fungsi = new Fungsi();
    String url = fungsi.url()+"news";
    private List<NewsDataSet> list = new ArrayList<NewsDataSet>();
    private NewsAdapter newsAdapter;
    JSONObject data = null;
    private ShimmerFrameLayout shimmerNews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_news);

        boolean connected = fungsi.isActiveNetwork(getApplication());
        if(!connected){
          startActivity(new Intent(getApplication(), ConnectionLost.class));
        }

        listNews = (ListView) findViewById(R.id.listNews);
        final NewsDataSet nds = new NewsDataSet();
        shimmerNews = findViewById(R.id.shimmer_news);


        vor = new VolleyObjectResult() {
            @Override
            public void resSuccess(String requestType, JSONObject response) {
                //Toast.makeText(ListKereta.this, response.toString(), Toast.LENGTH_LONG).show();
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject object =  jsonArray.getJSONObject(i);
                        NewsDataSet nds = new NewsDataSet();
                        nds.setTitle(object.getString("title"));
                        String urlImage = fungsi.urlImageNews(object.getString("image_name"));
                        nds.setImage(urlImage);
                        String content = fungsi.stripHtml(object.getString("content"));
                        nds.setDescription(content);
                        nds.setTimestamp(object.getString("published_at"));
                        nds.setId(object.getString("id"));
                        list.add(nds);
                    }
                    newsAdapter = new NewsAdapter(MainNews.this, list);
                    newsAdapter.notifyDataSetChanged();
                    listNews.setAdapter(newsAdapter);
                    shimmerNews.stopShimmer();
                    shimmerNews.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    shimmerNews.stopShimmer();
                    shimmerNews.setVisibility(View.GONE);
                }
            }
            @Override
            public void resError(String requestType, VolleyError error) {
                View view = findViewById(R.id.coordinator);
                String message = "Maaf data berita tidak ditemukan.";
                fungsi.showSnackbar(view, message);
                shimmerNews.stopShimmer();
                shimmerNews.setVisibility(View.GONE);
            }
        };
        vos = new VolleyObjectService(vor, MainNews.this);
        vos.getJsonObject("GETCALL", url);
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerNews.startShimmer();
    }

    @Override
    public void onPause() {
        shimmerNews.stopShimmer();
        super.onPause();
    }

    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
