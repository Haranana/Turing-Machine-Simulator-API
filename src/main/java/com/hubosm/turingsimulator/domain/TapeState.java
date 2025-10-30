package com.hubosm.turingsimulator.domain;


import java.util.Collection;
import java.util.Map;

// used in response to the app
// holds head and all data on given tape without blanks
// is sent per step
public record TapeState(int head, Map<Integer, String> tape) { }
