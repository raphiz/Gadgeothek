package ch.hsr.gadgeothek.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.domain.Gadget;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GadgetListFragment.OnGadgetListInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GadgetListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GadgetListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private ListView gadgetListView;

    private List<Gadget> gadgetList;

    private OnGadgetListInteractionListener mListener;

    public GadgetListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GadgetListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GadgetListFragment newInstance(ArrayList<Gadget> gadgetList) {
        GadgetListFragment fragment = new GadgetListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, gadgetList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           gadgetList = (List<Gadget>) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gadget_list, container, false);
        gadgetListView = (ListView) rootView.findViewById(R.id.gadgedListView);

        populateListView();

        return rootView;
    }

    private void populateListView() {
        Gadget mockGadget1 = new Gadget("GoPro 3 Hero");
        mockGadget1.setManufacturer("GoPro");
        Gadget mockGadget2 = new Gadget("Phantom IV");
        mockGadget1.setManufacturer("DJI");
        gadgetList.add(mockGadget1);
        gadgetList.add(mockGadget2);
        ArrayAdapter<Gadget> adapter = new GadgetItemAdapter();

        gadgetListView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGadgetListInteractionListener) {
            mListener = (OnGadgetListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnGadgetListInteractionListener {
        // TODO: Update argument type and name
        void onGadgetListInteraction(Uri uri);
    }

    public class GadgetItemAdapter extends ArrayAdapter<Gadget> {

        public GadgetItemAdapter() {
            super(getActivity(), R.layout.gadgetview_item);
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
    }
}
