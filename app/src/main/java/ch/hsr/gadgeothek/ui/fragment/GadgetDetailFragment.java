package ch.hsr.gadgeothek.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.constant.Constant;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.domain.Loan;
import ch.hsr.gadgeothek.domain.Reservation;
import ch.hsr.gadgeothek.service.Callback;
import ch.hsr.gadgeothek.service.LibraryService;
import ch.hsr.gadgeothek.ui.GadgetDetailCallback;


public class GadgetDetailFragment extends Fragment implements View.OnClickListener {

    private Gadget gadget;
    private Reservation reservation;
    private Loan loan;


    private GadgetDetailCallback gadgetDetailCallback;

    public GadgetDetailFragment() {
        // Required empty public constructor
    }


    public static GadgetDetailFragment newInstance(Gadget gadget, Loan loan, Reservation reservation) {
        GadgetDetailFragment fragment = new GadgetDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constant.GADGET, gadget);
        args.putSerializable(Constant.RESERVATION, reservation);
        args.putSerializable(Constant.LOAN, loan);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gadget = (Gadget) getArguments().getSerializable(Constant.GADGET);
            loan = (Loan) getArguments().getSerializable(Constant.LOAN);
            reservation = (Reservation) getArguments().getSerializable(Constant.RESERVATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gadget_detail, container, false);

        TextView nameTextView = (TextView) rootView.findViewById(R.id.gadgetDetailNameTextView);
        TextView manufacturerTextView = (TextView) rootView.findViewById(R.id.gadgetDetailManufacturerTextView);
        TextView conditionTextView = (TextView) rootView.findViewById(R.id.gadgetDetailConditionTextView);
        TextView priceTextView = (TextView) rootView.findViewById(R.id.gadgetDetailPriceTextView);

        Button reserveButton = (Button) rootView.findViewById(R.id.gadgetDetailReserveBtn);
        reserveButton.setVisibility(View.VISIBLE);
        if (loan != null) {
            reserveButton.setVisibility(View.GONE);
        } else if (reservation != null) {
            reserveButton.setText(R.string.delete_reservation);
        }else{
            reserveButton.setText(R.string.reserve);
        }


        nameTextView.setText(gadget.getName());
        manufacturerTextView.setText(gadget.getManufacturer());
        conditionTextView.setText(gadget.getCondition().toString());
        priceTextView.setText(String.valueOf(gadget.getPrice()));


        //TODO: Check if there is a reservation
        // TODO: Change button label AND function depending on reservation
        reserveButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        if (activity instanceof GadgetDetailCallback) {
            gadgetDetailCallback = (GadgetDetailCallback) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement GadgetDetailCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        gadgetDetailCallback = null;
    }

    @Override
    public void onClick(View v) {
        if (reservation != null) {
            gadgetDetailCallback.onReserveDelete(reservation);
        } else {
            gadgetDetailCallback.onReserve(gadget);
        }
    }
}
