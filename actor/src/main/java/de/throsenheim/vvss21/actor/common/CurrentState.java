package de.throsenheim.vvss21.actor.common;

import de.throsenheim.vvss21.actor.domain.Status;

public class CurrentState {

    private static Status status = Status.OPEN;

    private CurrentState(){

    }

    public static Status getStatus() {
        return status;
    }

    public static void setStatus(Status status) {
        CurrentState.status = status;
    }
}
