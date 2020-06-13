package br.com.gabriel.photoorganizer;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class ApplicationMain {
    public static final String FORMATTING_PATTERN = ".+\\..+\\..+\\..*";

    public static void main(String[] args) {

        System.out.println("Iniciando...");

        String path = System.getProperty("user.dir");
        try {
            Path rootPath = Paths.get(path).getParent();
            Files.walk(rootPath)
                    .filter(Files::isRegularFile)
                    .filter(it -> it.getFileName().toString().matches(FORMATTING_PATTERN))
                    .map(it -> {
                        String[] split = it.getFileName().toString().split("\\.", 4);
                        return new Data() {{
                            setDate(split[0]);
                            setSubject(split[1]);
                            setName(split[2]);
                            setPath(it);
                        }};
                    })
                    .sorted(Comparator.comparing(it -> it.getPath().getFileName().toString()))
                    .forEach(it -> {
                        try {
                            Path studentPath = Paths.get(path.concat("/").concat(it.getName()));
                            if (Files.notExists(studentPath)) {
                                Files.createDirectory(studentPath);
                            }
                            Path subjectPath = Paths.get(studentPath.toAbsolutePath().toString().concat("/").concat(it.getSubject()));
                            if (Files.notExists(subjectPath)) {
                                Files.createDirectory(subjectPath);
                            }
                            Path newFileLocation = Paths.get(subjectPath.toAbsolutePath().toString().concat("/").concat(it.getPath().getFileName().toString()));
                            Files.copy(it.getPath(), newFileLocation);
                        } catch (FileAlreadyExistsException e) {
                            System.err.println("Arquivo ja existe: " + e.getMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            System.out.println("Finalizado");

        } catch (IOException e) {
            System.err.println("Falha ao vasculhar pasta root: " + path);
        }

        System.out.println("Fim");
    }
}
