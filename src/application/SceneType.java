package application;

public enum SceneType {
    MENU("");

    private String filePath;

    SceneType(String filePath) {
        this.filePath = filePath;
    }

    public String getPath() {
        return filePath;
    }
}
