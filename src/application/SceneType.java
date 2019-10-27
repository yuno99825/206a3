package application;

/**
 * Represents the different types of scenes that can be shown on the main window.
 */
public enum SceneType {
    MENU("/resources/scenes/menu/Menu.fxml"),
    CHUNK_SELECTION("/resources/scenes/chunkselection/ChunkSelection.fxml"),
    MEDIA_SELECTION("/resources/scenes/mediaselection/MediaSelection.fxml"),
    CREATION_PREVIEW("/resources/scenes/CreationPreview.fxml"),
    QUIZ("/resources/scenes/quiz/QuizView.fxml"),
    QUIZ_STATS("/resources/scenes/quiz/QuizStats.fxml");

    private String pathToFXML;

    SceneType(String pathToFXML) {
        this.pathToFXML = pathToFXML;
    }

    public String getPath() {
        return pathToFXML;
    }
}
