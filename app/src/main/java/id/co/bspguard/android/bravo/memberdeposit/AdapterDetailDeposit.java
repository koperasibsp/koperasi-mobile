package id.co.bspguard.android.bravo.memberdeposit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import id.co.bspguard.android.bravo.R;

public class AdapterDetailDeposit extends BaseAdapter {
  private Activity activity;
  private LayoutInflater inflater;
  private List<ListDetailDepositDataSet> list;

  public AdapterDetailDeposit(Activity activity, List<ListDetailDepositDataSet> list) {
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
      convertView = inflater.inflate(R.layout.list_row_my_detail_deposit, null);
    TextView depositNumber = (TextView) convertView.findViewById(R.id.depositNumber);
    TextView depositValue = (TextView) convertView.findViewById(R.id.depositValue);
    TextView depositType = (TextView) convertView.findViewById(R.id.depositType);

    final ListDetailDepositDataSet pds = list.get(position);
    depositNumber.setText(pds.getDeposit_number());
    depositType.setText(pds.getType());
    depositValue.setText(pds.getValue());

    String price = pds.getValue();
    int val = (int) Double.parseDouble(String.valueOf(price));
    String s = NumberFormat.getIntegerInstance(Locale.GERMAN).format(val);
    depositValue.setText(s);

    return convertView;
  }
}
