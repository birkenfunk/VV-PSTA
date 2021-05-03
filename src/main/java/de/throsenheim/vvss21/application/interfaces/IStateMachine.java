package de.throsenheim.vvss21.application.interfaces;

import de.throsenheim.vvss21.domain.enums.EState;
import de.throsenheim.vvss21.domain.enums.ESymbol;

public interface IStateMachine {

    /**
     * Returns the next State of the Statemachine
     * @param input Received Symbol
     * @param currentState Current State
     * @return next State of the Statemachine
     */
    EState nextState(String input, EState currentState);

}
