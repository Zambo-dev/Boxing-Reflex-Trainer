package com.example.boxingreflextrainer;

// Timer callbacks functions
public interface TimerCallbacks {
    // Update MainActivity's Views
    void dataView(long min, long sec, int iterator, int round);
    // Reset MainActivity's Views
    void resetButtons();
}
