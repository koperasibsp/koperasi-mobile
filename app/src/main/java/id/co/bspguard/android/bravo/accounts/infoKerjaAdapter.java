package id.co.bspguard.android.bravo.accounts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import id.co.bspguard.android.bravo.R;

public class infoKerjaAdapter extends ArrayAdapter<infoKerjaDataSet> {
  private List<infoKerjaDataSet> stateList = new ArrayList<>();

  infoKerjaAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<infoKerjaDataSet> stateList) {
    super(context, resource, spinnerText, stateList);
    this.stateList = stateList;
  }

  @Override
  public infoKerjaDataSet getItem(int position) {
    return stateList.get(position);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    return initView(position);

  }

  @Override
  public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    return initView(position);
  }

  /**
   * Gets the state object by calling getItem and
   * Sets the state name to the drop-down TextView.
   *
   * @param position the position of the item selected
   * @return returns the updated View
   */
  private View initView(int position) {
    infoKerjaDataSet stateList = getItem(position);
    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = inflater.inflate(R.layout.project_list, null);
    TextView textView =  v.findViewById(R.id.projectSpinnerText);
    textView.setText(stateList.getRegionName());
    return v;

  }
}
