package id.co.bspguard.android.bravo.loan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.bspguard.android.bravo.R;

public class PenjaminLoanAdapter extends BaseAdapter {

  private Activity activity;
  private LayoutInflater inflater;
  private List<PenjaminLoanDataSet> list;
  private ArrayList<PenjaminLoanDataSet> arrayList;


  public PenjaminLoanAdapter(Activity activity, List<PenjaminLoanDataSet> list) {
    this.activity = activity;
    this.list = list;
    this.arrayList = new ArrayList<PenjaminLoanDataSet>();
    this.arrayList.addAll(list);
    this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
  public View getView(int position, View convertView, ViewGroup parent) {
    if (inflater == null) {
      inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    convertView = inflater.inflate(R.layout.list_row_penjamin_pinjaman, null);
    TextView namePenjamin = (TextView) convertView.findViewById(R.id.namaPenjamin);
    PenjaminLoanDataSet plds = list.get(position);
    namePenjamin.setText(plds.getName());
    return convertView;
  }

  //====================================== Fungsi Filtering EditText Cari Bandara ================================
  public void filter(String text) {
    text = text.toLowerCase(Locale.getDefault());
    list.clear();
    if (text.length() == 0){
      list.addAll(arrayList);
    } else {
      for (PenjaminLoanDataSet bds : arrayList){
        if (bds.getName().toLowerCase(Locale.getDefault()).contains(text)
          || bds.getName().toLowerCase(Locale.getDefault()).contains(text)
          || bds.getName().toLowerCase(Locale.getDefault()).contains(text)){
          list.add(bds);
        }
      }
    }
    notifyDataSetChanged();
  }

}
