package ch.hsr.gadgeothek.ui;

import android.widget.ArrayAdapter;

import ch.hsr.gadgeothek.constant.Tab;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.ui.fragment.GadgetListFragment;

public interface GadgetListCallback {
    void onGadgetClicked(Gadget gadget);
    ArrayAdapter<Gadget> getAdapter(Tab tab);
    void onGadgetListRefresh(GadgetListFragment fragment);
}
