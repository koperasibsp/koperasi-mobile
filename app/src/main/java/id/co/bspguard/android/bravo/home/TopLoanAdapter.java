package id.co.bspguard.android.bravo.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import id.co.bspguard.android.bravo.ConnectionLost;
import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.loan.FormLoan;
import id.co.bspguard.android.bravo.loan.LoanAdapter;
import id.co.bspguard.android.bravo.loan.LoanDataSet;

public class TopLoanAdapter extends RecyclerView.Adapter<TopLoanAdapter.MyViewHolder> {

  private Context activity ;
  private ActivityAwal activityAwal;
  private LayoutInflater inflater;
  private List<TopLoanDataSet> list;
  Fungsi fn = new Fungsi();
  TextView namaPinjaman, plafonPinjaman, bungaPinjaman, tenorPinjaman, keteranganPinjaman;
  ImageView logoPinjaman;
  String keterangan, plafon, iValue;
  Button ajukanPinjaman;
  public TopLoanAdapter(Context activity, List<TopLoanDataSet> list) {
    this.activity = activity;
    this.list = list;
  }

  @Override
  public TopLoanAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view ;
    LayoutInflater mInflater = LayoutInflater.from(activity);
    view = mInflater.inflate(R.layout.cardview_top_loan,parent,false);
    return new TopLoanAdapter.MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(TopLoanAdapter.MyViewHolder holder, final int position) {

    if(position == 4){

//        activityAwal.linearLayout.setVisibility(View.GONE);
//      holder.loanDescription.setVisibility(View.INVISIBLE);
//      Toast.makeText(activity, Integer.toString(position), Toast.LENGTH_LONG).show();
    }

    if(list.get(position).getDescription() == "null" || list.get(position).getDescription() == ""){
      keterangan = "not set";
    }else{
      keterangan = fn.stripHtml(list.get(position).getDescription());
    }

    if(list.get(position).getPlafon() == "null"){
      plafon = "0";
    }else{
      plafon = list.get(position).getPlafon();
    }

    iValue = fn.formatNominal(plafon);

    holder.loanName.setText(list.get(position).getLoan_name());
    holder.loanDescription.setText(keterangan);
    holder.loanTenor.setText("Tenor " + list.get(position).getTenor() + " bulan");
    final String url = fn.urlImages(list.get(position).getLogo());
    Glide.with(activity).asBitmap().load(url)
      .diskCacheStrategy(DiskCacheStrategy.ALL)
      .placeholder(R.mipmap.ic_launcher)
      .into(holder.imageView);
    holder.cardView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final BottomSheetDialog bt=new BottomSheetDialog(activity, R.style.DialogLoan);
        View view = LayoutInflater.from(activity).inflate(R.layout.bottom_sheet_detail_ms_loan,null);

        namaPinjaman = (TextView) view.findViewById(R.id.namaPinjaman);
        plafonPinjaman = (TextView) view.findViewById(R.id.plafonPinjaman);
        bungaPinjaman = (TextView) view.findViewById(R.id.bungaPinjaman);
        tenorPinjaman = (TextView) view.findViewById(R.id.tenorPinjaman);
        keteranganPinjaman = (TextView) view.findViewById(R.id.keteranganPinjaman);
        logoPinjaman = (ImageView) view.findViewById(R.id.logoPinjaman);
        ajukanPinjaman = (Button) view.findViewById(R.id.ajukanPinjaman);
        namaPinjaman.setText(list.get(position).getLoan_name());
        plafonPinjaman.setText(iValue);
        bungaPinjaman.setText(list.get(position).getRate_of_interest());
        tenorPinjaman.setText(list.get(position).getTenor());
        keteranganPinjaman.setText(keterangan);

        Glide.with(activity).asBitmap().load(url)
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .placeholder(R.mipmap.ic_launcher)
          .into(logoPinjaman);
        bt.setContentView(view);
        bt.show();

        ajukanPinjaman.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(activity, FormLoan.class);
            Bundle extras = new Bundle();
            extras.putString("id", list.get(position).getId());
            extras.putString("loan_name",  list.get(position).getLoan_name());
            extras.putString("rate_of_interest",  list.get(position).getRate_of_interest());
            extras.putString("plafon",  list.get(position).getPlafon());
            extras.putString("tenor", list.get(position).getTenor());
            extras.putDouble("provisi", list.get(position).getProvisi());
            extras.putDouble("bunga", list.get(position).getBunga());
            extras.putDouble("bunga_berjalan", list.get(position).getBunga_berjalan());
            extras.putInt("biaya_admin", list.get(position).getBiaya_admin());
            extras.putInt("batas_pinjaman", list.get(position).getBatas_pinjaman());
            extras.putInt("biaya_transfer", list.get(position).getBiaya_transfer());

            extras.putString("logo",  url);
            intent.putExtras(extras);
            activity.startActivity(intent);
          }
        });

      }
    });
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {

    TextView loanName, loanDescription, loanTenor;
    ImageView imageView;
    RelativeLayout cardView ;

    public MyViewHolder(View itemView) {
      super(itemView);

      loanName = (TextView) itemView.findViewById(R.id.top_loan_name);
      imageView = (ImageView) itemView.findViewById(R.id.top_loan_logo);
      cardView = (RelativeLayout) itemView.findViewById(R.id.cardview_id);
      loanDescription = (TextView) itemView.findViewById(R.id.top_loan_description);
      loanTenor = (TextView) itemView.findViewById(R.id.top_loan_tenor);

    }
  }

}
