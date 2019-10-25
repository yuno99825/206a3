package application;

public enum SceneType {
    MENU("/resources/scenes/menu/Menu.fxml"),
    CHUNK_SELECTION("/resources/scenes/chunkselection/ChunkSelection.fxml"),
    MEDIA_SELECTION("/resources/scenes/MediaSelection.fxml"),
    CREATION_PREVIEW("/resources/scenes/CreationPreview.fxml"),
    QUIZ("/resources/scenes/QuizView.fxml");

    private String pathToFXML;

    SceneType(String pathToFXML) {
        this.pathToFXML = pathToFXML;
    }

    public String getPath() {
        return pathToFXML;
    }
}
