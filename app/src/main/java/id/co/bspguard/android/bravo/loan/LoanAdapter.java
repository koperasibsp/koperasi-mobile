package id.co.bspguard.android.bravo.loan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import id.co.bspguard.android.bravo.R;
import id.co.bspguard.android.bravo.fragment.BottomSheetDetailLoan;
import id.co.bspguard.android.bravo.functions.Fungsi;
import id.co.bspguard.android.bravo.memberloan.DetailLoan;
import id.co.bspguard.android.bravo.memberloan.ListLoanDataSet;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder> {

  private Context activity ;
  private LayoutInflater inflater;
  private List<LoanDataSet> list;
  Fungsi fn = new Fungsi();
  TextView namaPinjaman, plafonPinjaman, bungaPinjaman, tenorPinjaman, keteranganPinjaman;
  ImageView logoPinjaman;
  String keterangan, plafon, iValue;
  Button ajukanPinjaman;
  public LoanAdapter(Context activity, List<LoanDataSet> list) {
    this.activity = activity;
    this.list = list;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view ;
    LayoutInflater mInflater = LayoutInflater.from(activity);
    view = mInflater.inflate(R.layout.grid_ms_loans,parent,false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(MyViewHolder holder, final int position) {

    holder.loanName.setText(list.get(position).getLoan_name());
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

        if(list.get(position).getPlafon() == "null"){
          plafon = "0";
        }else{
          plafon = list.get(position).getPlafon();
        }

        iValue = fn.formatNominal(plafon);

        namaPinjaman.setText(list.get(position).getLoan_name());
        plafonPinjaman.setText(iValue);
        bungaPinjaman.setText(list.get(position).getRate_of_interest());
        tenorPinjaman.setText(list.get(position).getTenor());

        if(list.get(position).getDescription() == "null" || list.get(position).getDescription() == ""){
          keterangan = "not set";
        }else{
          keterangan = fn.stripHtml(list.get(position).getDescription());
        }
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
            extras.putString("logo",  url);
            extras.putDouble("provisi", list.get(position).getProvisi());
            extras.putDouble("bunga", list.get(position).getBunga());
            extras.putDouble("bunga_berjalan", list.get(position).getBunga_berjalan());
            extras.putInt("biaya_admin", list.get(position).getBiaya_admin());
            extras.putInt("batas_pinjaman", list.get(position).getBatas_pinjaman());
            extras.putInt("biaya_transfer", list.get(position).getBiaya_transfer());
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

    TextView loanName;
    ImageView imageView;
    CardView cardView ;

    public MyViewHolder(View itemView) {
      super(itemView);

      loanName = (TextView) itemView.findViewById(R.id.loan_name);
      imageView = (ImageView) itemView.findViewById(R.id.logo);
      cardView = (CardView) itemView.findViewById(R.id.cardview_id);


    }
  }




}
