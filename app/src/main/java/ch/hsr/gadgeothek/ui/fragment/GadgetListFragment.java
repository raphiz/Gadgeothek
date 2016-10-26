package ch.hsr.gadgeothek.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.constant.Tab;
import ch.hsr.gadgeothek.ui.GadgetItemAdapter;
import ch.hsr.gadgeothek.ui.GadgetListCallback;
import ch.hsr.gadgeothek.ui.util.RecyclerViewEmptySupport;


public class GadgetListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private boolean initialized = false;
    private boolean loaded = false;

    private Tab tab;

    private RecyclerViewEmptySupport gadgetRecyclerView;

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

        gadgetRecyclerView = (RecyclerViewEmptySupport) rootView.findViewById(R.id.gadgetRecyclerView);

        gadgetRecyclerView.setEmptyView(getEmptyLayout(rootView));

        GadgetItemAdapter adapter = gadgetListCallback.getAdapter(tab);

        gadgetRecyclerView.setHasFixedSize(true);
        gadgetRecyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        gadgetRecyclerView.setLayoutManager(llm);


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
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                gadgetListCallback.onGadgetListRefresh();
            }
        });
    }

    private View getEmptyLayout(View rootView) {
        switch (tab) {
            case GADGETS:
                return rootView.findViewById(R.id.layout_empty_gadgets);
            case RESERVATIONS:
                return rootView.findViewById(R.id.layout_empty_reservations);
            case LOANS:
                return rootView.findViewById(R.id.layout_empty_loans);
            default:
                return null;
        }
    }
}
