package id.co.bspguard.android.bravo.memberloan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.functions.Fungsi;

public class AdapterListLoan extends BaseAdapter {

  private Activity activity;
  private LayoutInflater inflater;
  private List<ListLoanDataSet> list;
  Fungsi fn = new Fungsi();

  public AdapterListLoan(Activity activity, List<ListLoanDataSet> list) {
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
      convertView = inflater.inflate(R.layout.list_row_my_loan, null);
      TextView loanName = (TextView) convertView.findViewById(R.id.loan_name);
      TextView loanValue = (TextView) convertView.findViewById(R.id.value);
      TextView loanNumber = (TextView) convertView.findViewById(R.id.deposit_number);
      TextView loanPeriod = (TextView) convertView.findViewById(R.id.period);
      TextView loanApproval = (TextView) convertView.findViewById(R.id.approval);
      ImageView imageView = (ImageView) convertView.findViewById(R.id.imagePinjaman);

    final ListLoanDataSet pds = list.get(position);
      loanName.setText(pds.getLoan_name());
      loanValue.setText(pds.getValue());
      String price = pds.getValue();
      int val = Integer.parseInt(price);
      String s = NumberFormat.getIntegerInstance(Locale.GERMAN).format(val);
      loanValue.setText(s);
      loanNumber.setText(pds.getLoan_number());
      loanPeriod.setText(pds.getPeriod());
      loanApproval.setText(pds.getApproval());

      String url = fn.urlImages(pds.getLogo());

      Glide.with(convertView.getContext()).asBitmap().load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.mipmap.ic_launcher)
        .into(imageView);

      convertView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          Intent intent = new Intent(activity, DetailLoan.class);
          intent.putExtra("id", pds.getId());
          activity.startActivity(intent);
          activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        }
      });


    return convertView;
  }


}
