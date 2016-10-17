package ch.hsr.gadgeothek.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
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
import ch.hsr.gadgeothek.constant.Tab;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.service.Callback;
import ch.hsr.gadgeothek.service.LibraryService;
import ch.hsr.gadgeothek.ui.GadgetListCallback;


public class GadgetListFragment extends Fragment {

    private Tab pageTitle;

    private ListView gadgetListView;

    private GadgetListCallback gadgetListCallback;

    public GadgetListFragment() {
        // Required empty public constructor
    }

    public static GadgetListFragment getInstance(Tab pagetTitle) {
        GadgetListFragment fragment = new GadgetListFragment();
        Bundle args = new Bundle();
        args.putString(Constant.PAGE_TITLE, pagetTitle.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageTitle = Tab.valueOf(getArguments().getString(Constant.PAGE_TITLE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gadget_list, container, false);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                             @Override
                                             public void onRefresh() {
                                                 new Handler().postDelayed(new Runnable() {
                                                     @Override public void run() {
                                                         swipeLayout.setRefreshing(false);
                                                     }
                                                 }, 5000);
                                             }
                                         });

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

        gadgetListView.setAdapter(gadgetListCallback.getAdapter(pageTitle));
        //populateListView();

        return rootView;
    }

    /*private void populateListView() {
        if (gadgetList.isEmpty()) {
            LibraryService.getGadgets(new Callback<List<Gadget>>() {
                @Override
                public void onCompletion(List<Gadget> input) {
                    gadgetList.addAll(input);
                    gadgetListView.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onError(String message) {
                    //TODO error handling
                    Log.e("load gadgets", "error during loading gadgets: " + message);
                }
            });
        }

    }*/

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
    }
}
