package id.co.bspguard.android.bravo.news;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import id.co.bspguard.android.bravo.R;

public class NewsDetails extends AppCompatActivity {
    Bundle bundle = new Bundle();
    ImageView newsImage;
    TextView title, id, description, time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        bundle = getIntent().getExtras();

        title = (TextView) findViewById(R.id.newsTittle);
        newsImage = (ImageView) findViewById(R.id.newsImage);
        description = (TextView) findViewById(R.id.newsDescription);
        time = (TextView) findViewById(R.id.newsTime);

        Glide.with(NewsDetails.this).asBitmap().load(bundle.getString("image"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .into(newsImage);
        title.setText(bundle.getString("title"));
        description.setText(bundle.getString("description"));
        time.setText(bundle.getString("timestamp"));

    }

    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
