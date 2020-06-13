package br.com.gabriel.renomeador;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RenameMain {
    public static final String FORMATTING_PATTERN = ".+\\..+\\..+\\..*";
    public static final String pattern = "(.*)(\\d+)(.*)";

    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        try {
            Path rootPath = Paths.get(path);
            System.out.println("Escaneando pasta: " + rootPath.getFileName().toString());
            Files.walk(rootPath)
                    .filter(Files::isRegularFile)
                    .filter(it -> it.getFileName().toString().matches(FORMATTING_PATTERN))
                    .map(it -> {
                        String[] split = it.getFileName().toString().split("\\.", 4);
                        return new Data() {{
                            setDate(split[0]);
                            setSubject(split[1]);
                            setName(split[2]);
                            setExtra(split[3]);
                            setPath(it);
                        }};
                    })
                    .sorted(Comparator.comparing(it -> it.getPath().getFileName().toString()))
                    .forEach(it -> {
                        alterDate(it);
                        alterName(it);
                        renameFile(it);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void alterDate(Data data) {
        String[] split = data.getDate().split("-");
        data.setDate(split[1] + "-" + split[0]);
    }

    public static void alterName(Data data) {
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(data.getName());

        if (m.find()) {
            data.setName(m.group(1));
            data.setExtra(m.group(2).concat(".").concat(data.getExtra()));
        }
    }

    public static void renameFile(Data data) {
        try {
            String newDir = data.getPath().getParent().toAbsolutePath().toString().concat("/renomeados");
            Path pathNewDir = Paths.get(newDir);
            if (Files.notExists(pathNewDir)) {
                Files.createDirectory(pathNewDir);
            }

            Path newPath = Paths.get(newDir.concat("/").concat(data.getDate())
                    .concat(".").concat(data.getSubject())
                    .concat(".").concat(data.getName())
                    .concat(".").concat(data.getExtra()));

            System.out.println(newPath.toAbsolutePath().toString());
            Files.copy(data.getPath(), newPath);
        } catch (FileAlreadyExistsException e) {
            System.err.println("Arquivo ja existe: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
