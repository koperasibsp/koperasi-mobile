package id.co.bspguard.android.bravo.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import id.co.bspguard.android.bravo.R;

public class BottomSheetDetailLoan extends BottomSheetDialogFragment {
  Bundle bundle = new Bundle();
  public static BottomSheetDetailLoan newInstance() {
    BottomSheetDetailLoan fragment = new BottomSheetDetailLoan();
    fragment.getArguments();
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public void setupDialog(Dialog dialog, int style) {
    View contentView = View.inflate(getContext(), R.layout.bottom_sheet_detail_my_loan, null);
    dialog.setContentView(contentView);
    Bundle bundle = getArguments();

    String argTenor = bundle.getString("tenor","");
    String argTagihan = bundle.getString("tagihan","");
    String argJasa = bundle.getString("jasa","");
    String argTanggal = bundle.getString("tanggal","");
    String argStatus = bundle.getString("status","");

    TextView tenor = (TextView) contentView.findViewById(R.id.tenor);
    TextView tagihan = (TextView) contentView.findViewById(R.id.valuePinjaman);
    TextView jasa = (TextView) contentView.findViewById(R.id.valueJasa);
    TextView tanggal = (TextView) contentView.findViewById(R.id.valueTanggal);
    TextView status = (TextView) contentView.findViewById(R.id.valueStatus);

    tenor.setText(argTenor);
    tagihan.setText(argTagihan);
    jasa.setText(argJasa);
    tanggal.setText(argTanggal);
    status.setText(argStatus);

    ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
  }
}
