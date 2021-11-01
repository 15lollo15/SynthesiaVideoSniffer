package sniffer;

import midi.Note;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;

public class Keyboard {

    public static final int DEFAULT_KEYBOARD_SIZE = 88;
    public static final int DEFAULT_VELOCITY = 100;

    private final Note startNote;
    private final int keyboardSize;
    private final boolean[] isPressed;

    public Keyboard() {
        this(DEFAULT_KEYBOARD_SIZE, Note.A0);
    }

    public Keyboard(int keyboardSize, Note startNote) {
        this.keyboardSize = keyboardSize;
        this.startNote = startNote;
        this.isPressed = new boolean[keyboardSize];
    }

    public MidiEvent pressKey(int keyIndex, int tick) {
        return pressKey(keyIndex, tick, DEFAULT_VELOCITY);
    }

    public MidiEvent pressKey(int keyIndex, int tick, int velocity) {
        if (!isPressed[keyIndex]) {
            isPressed[keyIndex] = true;
            return sendEvent(ShortMessage.NOTE_ON, keyIndex, tick, velocity);
        }
        return null;
    }

    public MidiEvent releaseKey(int keyIndex, int tick) {
        return releaseKey(keyIndex, tick, DEFAULT_VELOCITY);
    }

    public MidiEvent releaseKey(int keyIndex, int tick, int velocity) {
        if (isPressed[keyIndex]) {
            isPressed[keyIndex] = false;
            return sendEvent(ShortMessage.NOTE_OFF, keyIndex, tick, velocity);
        }
        return null;
    }

    private MidiEvent sendEvent(int messageCode, int keyIndex, int tick, int velocity) {
        int realMidiCode = startNote.getMidiCode() + keyIndex;
        ShortMessage message = null;
        try {
            message = new ShortMessage(messageCode, 1, realMidiCode, velocity);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

        return new MidiEvent(message, tick);
    }

}