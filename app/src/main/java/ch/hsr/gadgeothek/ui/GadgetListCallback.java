package ch.hsr.gadgeothek.ui;

import ch.hsr.gadgeothek.constant.Tab;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.domain.Reservation;
import ch.hsr.gadgeothek.ui.fragment.GadgetListFragment;

public interface GadgetListCallback {
    GadgetItemAdapter getAdapter(Tab tab);
    void onGadgetListRefresh(GadgetListFragment fragment);
    void onReserveGadget(final Gadget gadget);
    void onDeleteReservation(final Reservation reservation, final GadgetItemAdapter adapter);
}
