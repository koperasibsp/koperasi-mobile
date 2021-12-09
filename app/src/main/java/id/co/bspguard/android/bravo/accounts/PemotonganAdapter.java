package id.co.bspguard.android.bravo.accounts;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.bspguard.android.bravo.R;

public class PemotonganAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<PemotonganDataSet> list;
    private ArrayList<PemotonganDataSet> arrayList;

    public PemotonganAdapter(Activity activity, List<PemotonganDataSet> list) {
        this.activity = activity;
        this.list = list;
        this.arrayList = new ArrayList<PemotonganDataSet>();
        this.arrayList.addAll(list);
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
            convertView = inflater.inflate(R.layout.list_row_potong, null);
        TextView namePotong = (TextView) convertView.findViewById(R.id.namapotong);
        TextView idPotong = (TextView) convertView.findViewById(R.id.valuepotong);
        final PemotonganDataSet lds = list.get(position);

        namePotong.setText(lds.getName_pemotongan());
        idPotong.setText(lds.getId_pemotongan());

        return convertView;
    }
}
