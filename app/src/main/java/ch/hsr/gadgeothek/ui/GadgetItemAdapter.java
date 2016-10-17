package ch.hsr.gadgeothek.ui;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.domain.Gadget;

public class GadgetItemAdapter extends ArrayAdapter<Gadget> {
    private List<Gadget> gadgetList;
    private Context activity;

    public GadgetItemAdapter(Context activity) {
        super(activity, R.layout.gadgetview_item);
        this.activity = activity;
        this.gadgetList = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Gadget gadget = gadgetList.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.gadgetview_item, null);

            TextView gadgetNameView = (TextView) convertView.findViewById(R.id.textViewGadgetName);
            TextView manufacturerView = (TextView) convertView.findViewById(R.id.textViewManufacturer);

            Pair<TextView, TextView> views = new Pair<>(gadgetNameView, manufacturerView);
            convertView.setTag(views);
        }

        Pair<TextView, TextView> views = (Pair<TextView, TextView>)  convertView.getTag();
        TextView gadgetNameView = views.first;
        TextView manufacturerView = views.second;

        manufacturerView.setText(gadget.getManufacturer());
        gadgetNameView.setText(gadget.getName());

        return convertView;
    }

    public void setGadgetList(List<Gadget> gadgetList) {
        this.gadgetList = gadgetList;
    }

    @Override
    public Gadget getItem(int position) {
        return gadgetList.get(position);
    }

    @Override
    public int getCount() {
        return gadgetList.size();
    }

    @Override
    public boolean isEmpty() {
        return gadgetList.isEmpty();
    }
}
