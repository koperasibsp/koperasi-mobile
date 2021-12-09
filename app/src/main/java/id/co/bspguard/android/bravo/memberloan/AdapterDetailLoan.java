package id.co.bspguard.android.bravo.memberloan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.fragment.BottomSheetDetailLoan;

public class AdapterDetailLoan extends BaseAdapter {

  private Activity activity;
  private LayoutInflater inflater;
  private List<ListDetailLoanDataSet> list;

  public AdapterDetailLoan(Activity activity, List<ListDetailLoanDataSet> list) {
    this.activity = activity;
    this.list = list;
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
    if (inflater == null)
      inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    if (convertView == null)
      convertView = inflater.inflate(R.layout.list_row_my_detail_loan, null);
      TextView loanValue = (TextView) convertView.findViewById(R.id.tagihan);
      TextView loanInPeriod = (TextView) convertView.findViewById(R.id.tenor);
      TextView loanApproval = (TextView) convertView.findViewById(R.id.status);

      final ListDetailLoanDataSet pds = list.get(position);
      loanValue.setText(pds.getValue());
      String price = pds.getValue();
      String service = pds.getService();
      int totalValue = Integer.parseInt(price) + Integer.parseInt(service);
      String s = NumberFormat.getIntegerInstance(Locale.GERMAN).format(totalValue);
      loanValue.setText(s);
      loanInPeriod.setText(pds.getIn_period()+"/"+pds.getPeriod());
      loanApproval.setText(pds.getApproval());


    return convertView;
  }
}
