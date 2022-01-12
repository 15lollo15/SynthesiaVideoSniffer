package midi;

import keyboard.Keyboard;
import keyboard.Keystroke;
import keyboard.Note;

import javax.sound.midi.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

public class MidiMaker implements PropertyChangeListener {

    public static final int VELOCITY = 100;

    private final Sequence sequence;
    private final Track track;

    public MidiMaker(Keyboard keyboard) throws InvalidMidiDataException {
        keyboard.addPropertyChangeListener(this);

        sequence = new Sequence(Sequence.PPQ, 16);
        track = sequence.createTrack();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Keystroke keystroke = (Keystroke) evt.getNewValue();

        try {
            MidiEvent midiEvent = getMidiEvent(keystroke);
            track.add(midiEvent);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    private MidiEvent getMidiEvent(Keystroke keystroke) throws InvalidMidiDataException {
        int midiCode = getKeyMidiCode(keystroke.note());

        ShortMessage message;
        if (keystroke.pressed())
            message = new ShortMessage(ShortMessage.NOTE_ON, 1, midiCode, VELOCITY);
        else
            message = new ShortMessage(ShortMessage.NOTE_OFF, 1, midiCode, VELOCITY);

        return new MidiEvent(message, keystroke.tick());
    }

    public int getKeyMidiCode(Note note) {
        return note.ordinal() + 9;
    }

    public void saveToMidi(File output) throws IOException {
        MidiSystem.write(sequence, MidiSystem.getMidiFileTypes()[0], output);
    }

}
