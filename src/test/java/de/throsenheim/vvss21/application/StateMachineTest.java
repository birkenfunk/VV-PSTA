package de.throsenheim.vvss21.application;

import de.throsenheim.vvss21.domain.enums.EState;
import de.throsenheim.vvss21.domain.enums.ESymbol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StateMachineTest {

    StateMachine stateMachine = StateMachine.getStateMachine();

    @Test
    void nextState() {
        EState state = EState.WAIT_FOR_CLIENT;
        state = stateMachine.nextState(ESymbol.SENSOR_HELLO.toString(),state);
        assertEquals(EState.WAIT_FOR_ACKNOWLEDGE, state);
        state = stateMachine.nextState(ESymbol.ACKNOWLEDGE.toString(), state);
        assertEquals(EState.WAIT_FOR_MEASUREMENT, state);
        state = stateMachine.nextState(ESymbol.MEASUREMENT.toString(), state);
        assertEquals(EState.WAIT_FOR_MEASUREMENT, state);
        state = stateMachine.nextState(ESymbol.STATION_HELLO.toString(), state);
        assertEquals(EState.ERROR, state);
        state = stateMachine.nextState(ESymbol.TERMINATE.toString(), state);
        assertEquals(EState.TERMINATED, state);
    }
}