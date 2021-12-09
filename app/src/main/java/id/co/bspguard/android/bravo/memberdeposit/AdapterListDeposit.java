package id.co.bspguard.android.bravo.memberdeposit;

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

public class AdapterListDeposit extends BaseAdapter {
  private Activity activity;
  private LayoutInflater inflater;
  private List<ListDepositDataset> list;

  public AdapterListDeposit(Activity activity, List<ListDepositDataset> list) {
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
      convertView = inflater.inflate(R.layout.list_row_my_deposit, null);
    TextView depositName = (TextView) convertView.findViewById(R.id.deposit_name);
    TextView depositValue = (TextView) convertView.findViewById(R.id.value);

    final ListDepositDataset pds = list.get(position);
    depositName.setText(pds.getDeposit_name());
    depositValue.setText(pds.getValue());
    String price = pds.getValue();
    int val = (int) Double.parseDouble(String.valueOf(price));
    final String s = NumberFormat.getIntegerInstance(Locale.GERMAN).format(val);
    depositValue.setText(s);

    convertView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(activity, DetailDeposit.class);
        intent.putExtra("id", pds.getDeposit_id());
        intent.putExtra("name", pds.getDeposit_name());
        intent.putExtra("total", s);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
      }

    });


    return convertView;
  }
}
