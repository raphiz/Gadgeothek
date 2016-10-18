package ch.hsr.gadgeothek.service;

public interface SimpleLibraryServiceCallback<T> {
    void onCompletion(T input);
    void onError(String message);
}
