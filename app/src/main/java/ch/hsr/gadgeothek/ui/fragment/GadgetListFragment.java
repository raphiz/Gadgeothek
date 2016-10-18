package ch.hsr.gadgeothek.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.constant.Tab;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.ui.GadgetListCallback;


public class GadgetListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private boolean initialized = false;
    private boolean loaded = false;

    private Tab tab;

    private ListView gadgetListView;

    private GadgetListCallback gadgetListCallback;

    SwipeRefreshLayout swipeLayout;

    public GadgetListFragment() {
        // Required empty public constructor
    }

    public static GadgetListFragment getInstance(Tab tab) {
        GadgetListFragment fragment = new GadgetListFragment();
        Bundle args = new Bundle();
        args.putString(Constant.TAB, tab.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tab = Tab.valueOf(getArguments().getString(Constant.TAB));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gadget_list, container, false);

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        swipeLayout.setOnRefreshListener(this);

        gadgetListView = (ListView) rootView.findViewById(R.id.gadgedListView);
        gadgetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gadget clickedGaddget = (Gadget) parent.getItemAtPosition(position);
                gadgetListCallback.onGadgetClicked(clickedGaddget);
            }
        });
        View emptyLayout = inflater.inflate(R.layout.gadgetview_empty_reservations, null);
        // TODO: Empty View doesn't work yet
        gadgetListView.setEmptyView(emptyLayout);

        gadgetListView.setAdapter(gadgetListCallback.getAdapter(tab));

        if(!initialized) {
            swipeLayout.setRefreshing(!loaded);
            initialized = true;
        }
        return rootView;
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
        gadgetListCallback = null;
        initialized = false;
    }

    public void onDataRefreshed() {
        loaded = true;
        if(initialized){
            swipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        // TODO: Execute in Runnable
        gadgetListCallback.onGadgetListRefresh(this);
    }
}
