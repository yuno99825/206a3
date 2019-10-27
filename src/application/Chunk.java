package application;

/**
 * Represents an audio chunk created by the user.
 */
public class Chunk {
    private String text;
    private double stretch;
    private int pitch;

    public Chunk(String text, double speed, int pitch) {
        this.text = text;
        this.stretch = 1/speed;
        this.pitch = pitch;
    }

    public String getText() {
        return text;
    }

    public String getStretchCommand() {
        return "(Parameter.set 'Duration_Stretch " + stretch + ")";
    }

    public String getPitchCommand() {
        return "(set! duffint_params '((start " + pitch + ") (end " + (pitch - 15) + ")))\n" +
                "(Parameter.set 'Int_Target_Method Int_Targets_Default)";
    }
}
