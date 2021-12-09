package id.co.bspguard.android.bravo.loan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.bspguard.android.bravo.R;

public class BulanPinjamanAdapter extends BaseAdapter {

  private Activity activity;
  private LayoutInflater inflater;
  private List<BulanPinjamanDataSet> list;
  private ArrayList<BulanPinjamanDataSet> arrayList;

  public BulanPinjamanAdapter(Activity activity, List<BulanPinjamanDataSet> list) {
    this.activity = activity;
    this.list = list;
    this.arrayList = new ArrayList<BulanPinjamanDataSet>();
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
    convertView = inflater.inflate(R.layout.list_row_bulan_pinjaman, null);
    TextView namePenjamin = (TextView) convertView.findViewById(R.id.nameBulan);
    BulanPinjamanDataSet plds = list.get(position);
    String bulan = plds.getBulan() + " bulan";
    namePenjamin.setText(bulan);
    return convertView;
  }

  //====================================== Fungsi Filtering EditText Cari Bandara ================================
  public void filter(String text) {
    text = text.toLowerCase(Locale.getDefault());
    list.clear();
    if (text.length() == 0){
      list.addAll(arrayList);
    } else {
      for (BulanPinjamanDataSet bds : arrayList){
        if (String.valueOf(bds.getBulan()).toLowerCase(Locale.getDefault()).contains(text)){
          list.add(bds);
        }
      }
    }
    notifyDataSetChanged();
  }

}
