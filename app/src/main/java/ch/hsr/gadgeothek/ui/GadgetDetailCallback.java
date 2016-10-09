package ch.hsr.gadgeothek.ui;

import ch.hsr.gadgeothek.domain.Gadget;

public interface GadgetDetailCallback {
    void onReserveButtonClicked(Gadget gadget);
    void onReserveDeleteButtonClicked(Gadget gadget);
}
