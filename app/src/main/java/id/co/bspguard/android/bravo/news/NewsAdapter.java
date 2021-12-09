package id.co.bspguard.android.bravo.news;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.bspguard.android.bravo.MainActivity;
import id.co.bspguard.android.bravo.R;


public class NewsAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<NewsDataSet> list;
    private ArrayList<NewsDataSet> arrayList;


    public NewsAdapter(Activity activity, List<NewsDataSet> list) {
        this.activity = activity;
        this.list = list;
        this.arrayList = new ArrayList<NewsDataSet>();
        this.arrayList.addAll(list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_news, null);
        ImageView news_image = (ImageView) convertView.findViewById(R.id.newsImage);
        TextView news_title = (TextView) convertView.findViewById(R.id.newsTittle);
        TextView news_date = (TextView) convertView.findViewById(R.id.newsTime);
        TextView news_description = (TextView) convertView.findViewById(R.id.newsDescription);

        final NewsDataSet mds = list.get(position);
        news_date.setText(mds.getTimestamp());
        news_title.setText(mds.getTitle());
        news_description.setText(mds.getDescription());

        Glide.with(activity).asBitmap().load(mds.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .into(news_image);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iSlider = new Intent(activity, NewsDetails.class);
                Bundle extras = new Bundle();
                extras.putString("id", list.get(position).getId());
                extras.putString("image",  list.get(position).getImage());
                extras.putString("timestamp",  list.get(position).getTimestamp());
                extras.putString("title",  list.get(position).getTitle());
                extras.putString("description",  list.get(position).getDescription());
                iSlider.putExtras(extras);
                activity.startActivity(iSlider);
            }
        });

        return convertView;
    }

    //====================================== Fungsi Filtering EditText Cari Country ================================
    public void filter(String text) {
        text = text.toLowerCase(Locale.getDefault());
        list.clear();
        if (text.length() == 0){
            list.addAll(arrayList);
        } else {
            for (NewsDataSet mds : arrayList){
                if (mds.getTitle().toLowerCase(Locale.getDefault()).contains(text)){
                    list.add(mds);
                }
            }
        }
        notifyDataSetChanged();
    }
}
