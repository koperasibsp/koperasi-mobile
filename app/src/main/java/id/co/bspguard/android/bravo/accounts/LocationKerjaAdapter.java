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

public class LocationKerjaAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<LocationKerjaDataSet> list;
    private ArrayList<LocationKerjaDataSet> arrayList;


    public LocationKerjaAdapter(Activity activity, List<LocationKerjaDataSet> list) {
        this.activity = activity;
        this.list = list;
        this.arrayList = new ArrayList<LocationKerjaDataSet>();
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
            convertView = inflater.inflate(R.layout.list_row_kerja, null);
        TextView namePosition = (TextView) convertView.findViewById(R.id.name);
        TextView idPosition = (TextView) convertView.findViewById(R.id.id_kerja);
        final LocationKerjaDataSet lds = list.get(position);

        namePosition.setText(lds.getName_kerja());
        idPosition.setText(lds.getId_kerja());

        return convertView;
    }

    //====================================== Fungsi Filtering EditText Cari Country ================================
    public void filter(String text) {
        text = text.toLowerCase(Locale.getDefault());
        list.clear();
        if (text.length() == 0){
            list.addAll(arrayList);
        } else {
            for (LocationKerjaDataSet lds : arrayList){
                if (lds.getName_kerja().toLowerCase(Locale.getDefault()).contains(text)){
                    list.add(lds);
                }
            }
        }
        notifyDataSetChanged();
    }
}