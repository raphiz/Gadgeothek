package ch.hsr.gadgeothek.ui;

import ch.hsr.gadgeothek.constant.Tab;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.domain.Reservation;

public interface GadgetListCallback {
    GadgetItemAdapter getAdapter(Tab tab);
    void onGadgetListRefresh();
    void onReserveGadget(final Gadget gadget);
    void onDeleteReservation(final Reservation reservation, final GadgetItemAdapter adapter);
}
