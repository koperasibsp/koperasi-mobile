package id.co.bspguard.android.bravo.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.functions.VolleyObjectResult;
import id.co.bspguard.android.bravo.functions.VolleyObjectService;
import id.co.bspguard.android.bravo.news.NewsDetails;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.CustomViewHolder>{

  private List<NotificationDataSet> notificationDataSet;
  private Context context;
  private Activity act;
  Fungsi fn = new Fungsi();
  String url = fn.url();
  VolleyObjectService vos;
  VolleyObjectResult vor;

  public NotificationAdapter(Context context, List<NotificationDataSet> list, Activity activity){
    this.notificationDataSet=list;
    this.context=context;
    this.act=activity;
    }

  @Override
  public CustomViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
    View view=LayoutInflater.from(this.context).inflate(R.layout.list_row_notification,parent,false);
    CustomViewHolder viewHolder=new CustomViewHolder(view);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(CustomViewHolder holder,final int position){
    final NotificationDataSet get = notificationDataSet.get(position);
    if(get.read_at != "null"){
      holder.contentList.setBackgroundColor(Color.parseColor("#f7f7f7"));;
    }
    holder.title.setText(get.getTitle());
    holder.description.setText(fn.stripHtml(get.getDescription()));
    holder.contentList.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(get.getType().equals("article")){
          Intent iSlider = new Intent(context, NewsDetails.class);
          try {
            updateData(get, "read");
            Bundle extras = new Bundle();
            extras.putString("id", get.getObject().getString("id"));
            extras.putString("image", fn.urlImageNews(get.getObject().getString("image_name")));
            extras.putString("timestamp",  get.getObject().getString("published_at"));
            extras.putString("title",  get.getObject().getString("title"));
            extras.putString("description", fn.stripHtml(get.getObject().getString("content")));
            iSlider.putExtras(extras);
            context.startActivity(iSlider);
          } catch (JSONException e) {
            e.printStackTrace();
          }

        }
      }
    });

    holder.deleteNotification.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(get.getType().equals("article")){
          updateData(get, "delete");
          notificationDataSet.remove(position);
          notifyItemRemoved(position);
        }
      }
    });
  }


  @Override
  public int getItemCount(){
    return(null!=notificationDataSet?notificationDataSet.size():0);
    }


  @Override
  public long getItemId(int position){
    return position;
    }

  @Override
  public int getItemViewType(int position){
    return position;
    }

  class CustomViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView title, description;
    LinearLayout deleteNotification, contentList;

    public CustomViewHolder(final View itemView) {
      super(itemView);
      title = (TextView) itemView.findViewById(R.id.title);
      description = (TextView) itemView.findViewById(R.id.description);
      deleteNotification = (LinearLayout) itemView.findViewById(R.id.deletenotification);
      contentList = (LinearLayout) itemView.findViewById(R.id.contentList);

    }
  }

  private void updateData(NotificationDataSet get, String type) {
    String urlAction = "";
    if(type.equals("delete")){
      urlAction = url+"delete-notification/"+get.getId();
    }

    if(type.equals("read")){
      urlAction = url+"read-notification/"+get.getId();
    }

    vor = new VolleyObjectResult() {
      @Override
      public void resSuccess(String requestType, JSONObject response) {
        try {
          notifyDataSetChanged();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      @Override
      public void resError(String requestType, VolleyError error) {
        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
      }
    };

    vos = new VolleyObjectService(vor, context);
    vos.getJsonObject("GETCALL", urlAction);


  }

}
