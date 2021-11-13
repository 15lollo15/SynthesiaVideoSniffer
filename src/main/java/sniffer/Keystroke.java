package sniffer;

import midi.Note;

public record Keystroke(Note note, boolean pressed, int tick) {}
