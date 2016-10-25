package ch.hsr.gadgeothek.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.domain.Loan;
import ch.hsr.gadgeothek.domain.Reservation;
import ch.hsr.gadgeothek.ui.fragment.GadgetListFragment;


public class GadgetItemAdapter extends RecyclerView.Adapter<GadgetItemAdapter.GadgetViewHolder>{

    private List<Gadget> gadgetList;
    private List<Reservation> reservationList;
    private List<Loan> loanList;
    private Context activity;

    public GadgetItemAdapter(Context activity) {
        gadgetList = new ArrayList<>();
        this.activity = activity;
    }

    @Override
    public GadgetViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        GadgetViewHolder pvh;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewgroup_gadgetcard, viewGroup, false);
        return new GadgetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GadgetViewHolder holder, int position) {
        if (position == 0 && gadgetList.size() == 0) return; // show empty layout
        DateFormat dateFormatter = android.text.format.DateFormat.getDateFormat(activity);
        Resources resources = activity.getResources();
        Gadget gadget = gadgetList.get(position);

        holder.title.setText(gadget.getName());
        String conditionText = String.format("%s: %s",
                resources.getString(R.string.condition), gadget.getCondition().toString().toLowerCase());
        holder.condition.setText(conditionText);
        holder.manufacturer.setText(gadget.getManufacturer());
        holder.price.setText(String.valueOf(gadget.getPrice()));

        Loan loan = findLoan(gadget);
        if (loan != null) {
            holder.reserveButton.setVisibility(View.GONE);
            if (loan.isOverdue()) {
                String overDueText = String.format("%s %s",
                        resources.getString(R.string.loan_overdue), dateFormatter.format(loan.overDueDate()));
                holder.reservedAt.setText(overDueText);
                holder.reservedAt.setTextColor(Color.RED);
            } else {
                String returnUntilText = String.format("%s %s",
                        resources.getString(R.string.return_until), dateFormatter.format(loan.overDueDate()));
                holder.reservedAt.setText(returnUntilText);
                holder.reservedAt.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent));
            }
        } else {
            Reservation reservation = findReservation(gadget);
            if (reservation != null) {
                holder.reserveButton.setVisibility(View.GONE);
                holder.deleteReserveButton.setVisibility(View.VISIBLE);
                String reservedAtText = String.format("%s %s",
                            resources.getString(R.string.has_been_reserved_at),
                            dateFormatter.format(reservation.getReservationDate()));
                holder.reservedAt.setText(reservedAtText);
            } else {
                holder.reservedAt.setVisibility(View.GONE);
            }
        }
    }

    private Reservation findReservation(Gadget gadget) {
        if (reservationList == null) return null;
        for (Reservation reservation : reservationList) {
            if (reservation.equals(gadget) && !reservation.getFinished())
                return reservation;
        }
        return null;
    }

    private Loan findLoan(Gadget gadget) {
        if (loanList == null) return null;
        for (Loan loan : loanList) {
            if (loan.equals(gadget) && loan.isLent())
                return loan;
        }
        return null;
    }

    public void setGadgetList(List<Gadget> gadgetList) {
        this.gadgetList = gadgetList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    public void setLoanList(List<Loan> loanList) {
        this.loanList = loanList;
    }

    @Override
    public int getItemCount() {
        return gadgetList.size();
    }

    public  class GadgetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView title, manufacturer, condition, price, reservedAt;
        Button reserveButton, deleteReserveButton;


        GadgetViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.gadget_detail_cardview);
            title = (TextView) itemView.findViewById(R.id.gadget_detail_title);
            manufacturer = (TextView) itemView.findViewById(R.id.gadget_detail_manufacturer);
            condition = (TextView) itemView.findViewById(R.id.gadget_detail_condition);
            price = (TextView) itemView.findViewById(R.id.gadget_detail_price);
            reservedAt = (TextView) itemView.findViewById(R.id.gadget_detail_reserved);
            reserveButton = (Button) itemView.findViewById(R.id.gadget_detail_reserve_button);
            deleteReserveButton = (Button) itemView.findViewById(R.id.gadget_detail_del_reserve_button);

            if (reserveButton != null)
                reserveButton.setOnClickListener(this);
            if (deleteReserveButton != null)
                deleteReserveButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == reserveButton.getId()) {
                // TODO: Reserve
                Gadget gadget = gadgetList.get(getAdapterPosition());
                Log.d("Debug", "Reserve Gadget " + gadget.getName());
            } else if (v.getId() == deleteReserveButton.getId()) {
                // TODO: Delete reservation
            }
        }
    }
}
