package ch.hsr.gadgeothek.service;

import java.util.List;

import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.domain.Loan;
import ch.hsr.gadgeothek.domain.Reservation;

public interface LibraryServiceCallback {
    void onCompletion(List<Gadget> loadedGadgets, List<Loan> loadedLoans, List<Reservation> loadedReservations);
    void onError(String message);
}
