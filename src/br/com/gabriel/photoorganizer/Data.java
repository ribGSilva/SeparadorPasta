package br.com.gabriel.photoorganizer;

import java.io.Serializable;
import java.nio.file.Path;

public class Data implements Serializable {
    private String date;
    private String subject;
    private String name;
    private Path path;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}
