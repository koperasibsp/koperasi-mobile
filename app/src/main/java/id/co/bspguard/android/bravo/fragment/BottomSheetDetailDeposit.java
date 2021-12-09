package id.co.bspguard.android.bravo.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import id.co.bspguard.android.bravo.R;

public class BottomSheetDetailDeposit extends BottomSheetDialogFragment {
  Bundle bundle = new Bundle();
  public static BottomSheetDetailDeposit newInstance() {
    BottomSheetDetailDeposit fragment = new BottomSheetDetailDeposit();
    fragment.getArguments();
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public void setupDialog(Dialog dialog, int style) {
    View contentView = View.inflate(getContext(), R.layout.bottom_sheet_detail_my_deposit, null);
    dialog.setContentView(contentView);
    Bundle bundle = getArguments();

    String argDespositNumber = bundle.getString("depositNumber","");
    String argDepositType = bundle.getString("depositType","");
    String argDepositTotal = bundle.getString("depositTotal","");
    String argDepositTanggal = bundle.getString("depositTanggal","");
    String argDepositDesc = bundle.getString("depositDesc","");
    String argDepositStatus = bundle.getString("depositStatus","");

    TextView depositNumber = (TextView) contentView.findViewById(R.id.depositNumber);
    TextView depositType = (TextView) contentView.findViewById(R.id.depositType);
    TextView depositTotal = (TextView) contentView.findViewById(R.id.depositTotal);
    TextView depositTanggal = (TextView) contentView.findViewById(R.id.depositTanggal);
    TextView depositDesc = (TextView) contentView.findViewById(R.id.depositDesc);
    TextView depositStatus = (TextView) contentView.findViewById(R.id.depositStatus);


    depositNumber.setText(argDespositNumber);
    depositType.setText(argDepositType);
    depositTotal.setText(argDepositTotal);
    depositTanggal.setText(argDepositTanggal);
    depositDesc.setText(argDepositDesc);
    depositStatus.setText(argDepositStatus);


    ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
  }
}
