package application;

public enum SceneType {
    MENU("/resources/scenes/Menu.fxml"),
    CREATION_TOOL("/resources/scenes/CreationTool.fxml"),
    IMAGE_SELECTION("/resources/scenes/ImageSelection.fxml"),
    CREATION_PREVIEW("/resources/scenes/CreationPreview.fxml"),
    VIDEO_PLAYER("/resources/scenes/VideoPlayer.fxml"),
    QUIZ("/resources/scenes/QuizView.fxml");

    private String pathToFXML;

    SceneType(String pathToFXML) {
        this.pathToFXML = pathToFXML;
    }

    public String getPath() {
        return pathToFXML;
    }
}
