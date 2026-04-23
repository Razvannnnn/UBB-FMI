package mpp2025.service;

public interface Subject {
    void addObserver(IObserver o);
    void removeObserver(IObserver o);
    void notifyObservers();
}
