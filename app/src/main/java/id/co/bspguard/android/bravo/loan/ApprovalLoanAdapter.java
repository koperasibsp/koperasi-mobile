package id.co.bspguard.android.bravo.loan;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.bspguard.android.bravo.R;

public class ApprovalLoanAdapter extends BaseAdapter {
  private Activity activity;
  private LayoutInflater inflater;
  private List<ApprovalLoanDataSet> list;
  private ArrayList<ApprovalLoanDataSet> arrayList;


  public ApprovalLoanAdapter(Activity activity, List<ApprovalLoanDataSet> list) {
    this.activity = activity;
    this.list = list;
    this.arrayList = new ArrayList<ApprovalLoanDataSet>();
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
    convertView = inflater.inflate(R.layout.list_row_approval_loan, null);
    TextView memberName = (TextView) convertView.findViewById(R.id.member_name);
    TextView loanValue = (TextView) convertView.findViewById(R.id.value);

    ApprovalLoanDataSet apds = list.get(position);
    memberName.setText(apds.getName());
    int val = (int) Double.parseDouble(String.valueOf(apds.getValue()));
    final String s = NumberFormat.getIntegerInstance(Locale.GERMAN).format(val);
    loanValue.setText(s);
    return convertView;
  }

  //====================================== Fungsi Filtering EditText Cari Member ================================
  public void filter(String text) {
    text = text.toLowerCase(Locale.getDefault());
    list.clear();
    if (text.length() == 0){
      list.addAll(arrayList);
    } else {
      for (ApprovalLoanDataSet bds : arrayList){
        if (String.valueOf(bds.getName()).toLowerCase(Locale.getDefault()).contains(text)){
          list.add(bds);
        }
      }
    }
    notifyDataSetChanged();
  }

}
