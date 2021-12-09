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

public class PositionAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<PositionDataset> list;
    private ArrayList<PositionDataset> arrayList;


    public PositionAdapter(Activity activity, List<PositionDataset> list) {
        this.activity = activity;
        this.list = list;
        this.arrayList = new ArrayList<PositionDataset>();
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
            convertView = inflater.inflate(R.layout.list_row_position, null);
        TextView namePosition = (TextView) convertView.findViewById(R.id.name);
        TextView idPosition = (TextView) convertView.findViewById(R.id.id_position);
        final PositionDataset pds = list.get(position);

        namePosition.setText(pds.getName_position());
        idPosition.setText(pds.getId_position());

        return convertView;
    }

    //====================================== Fungsi Filtering EditText Cari Country ================================
    public void filter(String text) {
        text = text.toLowerCase(Locale.getDefault());
        list.clear();
        if (text.length() == 0){
            list.addAll(arrayList);
        } else {
            for (PositionDataset pds : arrayList){
                if (pds.getName_position().toLowerCase(Locale.getDefault()).contains(text)){
                    list.add(pds);
                }
            }
        }
        notifyDataSetChanged();
    }
}
