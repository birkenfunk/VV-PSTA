package de.throsenheim.vvss21.application;

import de.throsenheim.vvss21.application.interfaces.IStateMachine;
import de.throsenheim.vvss21.domain.enums.EState;
import de.throsenheim.vvss21.domain.enums.ESymbol;

import java.util.EnumMap;

public class StateMachine implements IStateMachine {

    private final EnumMap<EState, EnumMap<ESymbol, EState>> stateSymbolStateHashmap = new EnumMap<>(EState.class);//Map for the automat
    private static StateMachine stateMachine;

    private StateMachine(){
        initEnumMap();
    }

    /**
     * Initialises the stateSymbolStateHashmap
     */
    private void initEnumMap(){
        EnumMap<ESymbol, EState> symbolStateHashMap = new EnumMap<>(ESymbol.class);
        symbolStateHashMap.put(ESymbol.SENSOR_HELLO, EState.WAIT_FOR_ACKNOWLEDGE);
        stateSymbolStateHashmap.put(EState.WAIT_FOR_CLIENT, symbolStateHashMap);
        symbolStateHashMap = new EnumMap<>(ESymbol.class);
        symbolStateHashMap.put(ESymbol.ACKNOWLEDGE, EState.WAIT_FOR_MEASUREMENT);
        symbolStateHashMap.put(ESymbol.TERMINATE, EState.TERMINATED);
        symbolStateHashMap.put(ESymbol.MEASUREMENT, EState.TERMINATED);
        stateSymbolStateHashmap.put(EState.WAIT_FOR_ACKNOWLEDGE,symbolStateHashMap);
        symbolStateHashMap = new EnumMap<>(ESymbol.class);
        symbolStateHashMap.put(ESymbol.MEASUREMENT, EState.WAIT_FOR_MEASUREMENT);
        symbolStateHashMap.put(ESymbol.TERMINATE, EState.TERMINATED);
        stateSymbolStateHashmap.put(EState.WAIT_FOR_MEASUREMENT, symbolStateHashMap);
        symbolStateHashMap = new EnumMap<>(ESymbol.class);
        symbolStateHashMap.put(ESymbol.SENSOR_HELLO, EState.TERMINATED);
        symbolStateHashMap.put(ESymbol.ACKNOWLEDGE, EState.TERMINATED);
        symbolStateHashMap.put(ESymbol.MEASUREMENT, EState.TERMINATED);
        symbolStateHashMap.put(ESymbol.TERMINATE, EState.TERMINATED);
        stateSymbolStateHashmap.put(EState.TERMINATED, symbolStateHashMap);
        symbolStateHashMap = new EnumMap<>(ESymbol.class);
        symbolStateHashMap.put(ESymbol.TERMINATE, EState.TERMINATED);
        stateSymbolStateHashmap.put(EState.ERROR, symbolStateHashMap);
    }


    /**
     * Returns the next State of the Statemachine
     *
     * @param input        Received Symbol
     * @param currentState Current State
     * @return next State of the Statemachine
     */
    @Override
    public EState nextState(String input, EState currentState) {
        input = input.toUpperCase();
        ESymbol inputSymbol;
        try {
            inputSymbol = ESymbol.valueOf(input);
        }catch (IllegalArgumentException e){
            inputSymbol = ESymbol.UNKNOWN;
        }
        EState ret = stateSymbolStateHashmap.get(currentState).get(inputSymbol);
        if(ret == null)
            ret = EState.ERROR;
        return ret;
    }

    public static StateMachine getStateMachine() {
        if(stateMachine == null){
            stateMachine = new StateMachine();
        }
        return stateMachine;
    }
}
