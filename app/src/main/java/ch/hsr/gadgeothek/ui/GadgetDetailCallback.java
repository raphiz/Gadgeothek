package ch.hsr.gadgeothek.ui;

import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.domain.Reservation;

public interface GadgetDetailCallback {
    void onReserve(Gadget gadget);
    void onReserveDelete(Reservation reservation);
}
