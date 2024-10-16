package g56583.esi.atl.model.OO;

/**
 * The Observer interface should be implemented by any class that needs to be
 * notified of changes in an observable object. Observers implement the update method,
 * which is called by the observable object when a change occurs.
 */
public interface Observer {
    /**
     * This method is called when the observable object has been updated.
     *
     * @param update A string representing the update message.
     */
    void update(String update);
}
