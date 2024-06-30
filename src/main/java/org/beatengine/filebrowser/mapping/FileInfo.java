package org.beatengine.filebrowser.mapping;

public class FileInfo {
    private boolean folder;
    private String name;
    private String size;
    private String creation;

    public FileInfo(boolean isFolder, String name, String size, String creation) {
        this.folder = isFolder;
        this.name = name;
        this.size = size;
        this.creation = creation;
    }

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCreation() {
        return creation;
    }

    public void setCreation(String creation) {
        this.creation = creation;
    }
}
