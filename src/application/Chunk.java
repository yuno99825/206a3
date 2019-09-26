package application;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

public class Chunk {
    private String text;
    private String voice;

    public Chunk(String text, String voice) {
        this.text = text;
        this.voice = voice;
    }

    public String getText() {
        return text;
    }

    public String getVoiceCommand() {
        switch (voice) {
            case "NZ Male":
                return "(voice_akl_nz_jdt_diphone)";
            case "NZ Female":
                return "(voice_akl_nz_cw_cg_cg)";
            default:
                return "(voice_kal_diphone)";
        }
    }
}
