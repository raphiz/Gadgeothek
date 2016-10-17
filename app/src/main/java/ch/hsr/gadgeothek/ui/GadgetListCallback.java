package ch.hsr.gadgeothek.ui;

import android.widget.ArrayAdapter;

import ch.hsr.gadgeothek.constant.Tab;
import ch.hsr.gadgeothek.domain.Gadget;

public interface GadgetListCallback {
    void onGadgetClicked(Gadget gadget);
    ArrayAdapter<Gadget> getAdapter(Tab tab);
}
