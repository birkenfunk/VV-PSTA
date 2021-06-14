package de.throsenheim.vvss21.application;

public interface IContactActor {

    /**
     * Contacts an Actor to change it's status
     * @param url URL of the actor
     * @param status New status of the actor
     */
    void contact(String url, String status);

}
