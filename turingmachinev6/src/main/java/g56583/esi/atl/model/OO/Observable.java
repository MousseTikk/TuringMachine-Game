package g56583.esi.atl.model.OO;

import java.util.ArrayList;
import java.util.List;

/**
 * The Observable class represents an object that can be observed by multiple
 * observers. When the observable object is updated, all registered observers
 * are notified of the change.
 */
public class Observable {
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer The observer to be added.
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Notifies all registered observers of an update.
     *
     * @param update A string representing the update message.
     */
    public void notifyObservers(String update) {
        for (Observer observer : observers) {
            observer.update(update);
        }
    }
}
