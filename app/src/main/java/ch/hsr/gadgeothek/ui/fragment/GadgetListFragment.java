package ch.hsr.gadgeothek.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.constant.Tab;
import ch.hsr.gadgeothek.ui.GadgetListCallback;


public class GadgetListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private boolean initialized = false;
    private boolean loaded = false;

    private Tab tab;

    private RecyclerView gadgetRecyclerView;


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

    public class GadgetAdapter extends RecyclerView.Adapter<GadgetAdapter.GadgetViewHolder>{

        private final View emptyLayout;

        public GadgetAdapter(View emptyLayout) {
            super();
            this.emptyLayout = emptyLayout;
        }

        @Override
        public GadgetViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewgroup_gadgetcard, viewGroup, false);
            GadgetViewHolder pvh = new GadgetViewHolder(emptyLayout);
            return pvh;
        }



        @Override
        public void onBindViewHolder(GadgetViewHolder holder, int position) {
            //TO BE DONE
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public  class GadgetViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
//            TextView personName;

            GadgetViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.gadget_detail_cardview);
//                personName = (TextView)itemView.findViewById(R.id.person_name);
            }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gadget_list, container, false);

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        swipeLayout.setOnRefreshListener(this);

        gadgetRecyclerView = (RecyclerView) rootView.findViewById(R.id.gadgedRecyclerView);

        View emptyLayout = inflater.inflate(R.layout.gadgetview_empty_reservations, null);
        GadgetAdapter adapter = new GadgetAdapter(emptyLayout);

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
        // TODO: Execute in Runnable
        gadgetListCallback.onGadgetListRefresh(this);
    }
}
