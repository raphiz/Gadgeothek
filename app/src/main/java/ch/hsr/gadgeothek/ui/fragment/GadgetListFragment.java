package ch.hsr.gadgeothek.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.service.Callback;
import ch.hsr.gadgeothek.service.LibraryService;
import ch.hsr.gadgeothek.ui.GadgetListCallback;


public class GadgetListFragment extends Fragment {

    private String pageTitle;

    private ListView gadgetListView;

    private List<Gadget> gadgetList;

    private GadgetListCallback gadgetListCallback;

    public GadgetListFragment() {
        // Required empty public constructor
    }

    public static GadgetListFragment getInstance(String pagetTitle, ArrayList<Gadget> gadgetList) {
        GadgetListFragment fragment = new GadgetListFragment();
        Bundle args = new Bundle();
        args.putString(Constant.PAGE_TITLE, pagetTitle);
        args.putSerializable(Constant.GADGET_LIST, gadgetList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageTitle = getArguments().getString(Constant.PAGE_TITLE);
            gadgetList = (List<Gadget>) getArguments().getSerializable(Constant.GADGET_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gadget_list, container, false);
        gadgetListView = (ListView) rootView.findViewById(R.id.gadgedListView);

        gadgetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gadget clickedGaddget = (Gadget) parent.getItemAtPosition(position);
                gadgetListCallback.onGadgetClicked(clickedGaddget);
            }
        });


        populateListView();

        return rootView;
    }

    private void populateListView() {
        if (gadgetList.isEmpty()) {
            LibraryService.getGadgets(new Callback<List<Gadget>>() {
                @Override
                public void onCompletion(List<Gadget> input) {
                    gadgetList.addAll(input);
                    gadgetListView.setAdapter(new GadgetItemAdapter(gadgetList));
                }

                @Override
                public void onError(String message) {
                    //TODO error handling
                    Log.e("load gadgets", "error during loading gadgets: " + message);
                }
            });
        }

    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (activity instanceof GadgetListCallback) {
            gadgetListCallback = (GadgetListCallback) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement GadgetListCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        gadgetList = null;
        gadgetListCallback = null;
    }

    public class GadgetItemAdapter extends ArrayAdapter<Gadget> {
        private List<Gadget> gadgetList;

        public GadgetItemAdapter(List<Gadget> gadgetList) {
            super(getActivity(), R.layout.gadgetview_item);
            this.gadgetList = gadgetList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Gadget gadget = gadgetList.get(position);
            if (convertView == null) {
                LayoutInflater layoutInflater =
                        (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.gadgetview_item, null);
            }

            TextView manufacturerView = (TextView) convertView.findViewById(R.id.textViewManufacturer);
            TextView gadgetNameView = (TextView) convertView.findViewById(R.id.textViewGadgetName);

            manufacturerView.setText(gadget.getManufacturer());
            gadgetNameView.setText(gadget.getName());

            return convertView;
        }

        @Override
        public Gadget getItem(int position) {
            return gadgetList.get(position);
        }

        @Override
        public int getCount() {
            return gadgetList.size();
        }
    }
}
