package app.web.pavelk.transfer1;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class Transfer1 {


    private static final String CONFIG_TEMP = "config_temp_4352453253";
    public static final String SEPARATOR = "\\";

    public static void main(String[] args) throws IOException {
        Path pathCode = Paths.get("C:\\code");
        Path pathTemp = Paths.get("C:\\temp");
        toTemp(pathCode, pathTemp);
//        fromTemp(pathTemp);
    }

    private static void fromTemp(Path pathTemp) throws IOException {
        List<Info> listInfos = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(pathTemp)) {
            walk.forEach(f -> {
                if (f.getFileName().toString().equals(CONFIG_TEMP)) {
                    try {
                        byte[] bytes = Files.readAllBytes(f);
                        String jsonString = new String(bytes);
                        JSONObject jsonObject = new JSONObject(jsonString);
                        Info build = Info.builder()
                                .id(jsonObject.get("id").toString())
                                .name(jsonObject.get("name").toString())
                                .pathOld(jsonObject.get("pathOld").toString())
                                .pathCurrent(f.getParent().toString())
                                .build();
                        listInfos.add(build);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        listInfos.forEach(f -> {
            try {
                Files.move(Paths.get(f.getPathCurrent() + SEPARATOR + f.getName()), Paths.get(f.getPathOld()),
                        StandardCopyOption.REPLACE_EXISTING);
                Files.delete(Paths.get(f.getPathCurrent() + SEPARATOR + CONFIG_TEMP));
                Files.delete(Paths.get(f.getPathCurrent()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    private static void toTemp(Path pathCode, Path pathTemp) throws IOException {
        if (!Files.exists(pathTemp)) {
            Files.createDirectory(pathTemp);
        }
        List<Info> listInfos = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(pathCode)) {
            walk.forEach(f -> {
                if (isStatus(f)) {

                    Info info = new Info();
                    info.setId(UUID.randomUUID().toString());
                    info.setName(f.getFileName().toString());
                    info.setPathOld(f.toString());
                    listInfos.add(info);
                }
            });
        }

        listInfos.forEach(f -> {
            try {
                Path pathCurrent = Paths.get(pathTemp + SEPARATOR + f.getId());
                Files.createDirectory(pathCurrent);
                Files.move(Paths.get(f.getPathOld()), Paths.get(pathCurrent + SEPARATOR + f.getName()),
                        StandardCopyOption.REPLACE_EXISTING);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", f.getId());
                jsonObject.put("name", f.getName());
                jsonObject.put("pathOld", f.getPathOld());
                jsonObject.put("self", pathCurrent);
                Files.write(Paths.get(pathCurrent + SEPARATOR + CONFIG_TEMP), jsonObject.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public static final String BUILD = "build";
    public static final String TARGET = "target";
    public static final String NODE_MODULES = "node_modules";
    public static final String G = ".gradle";
    public static final String I = ".idea";
    public static final String A = ".angular";
    public static final String D = "dist";
    public static final String F = ".firebase";

    private static boolean isStatus(Path f) {
        return (f.getFileName().toString().equals(NODE_MODULES) && !f.getParent().toString().contains(NODE_MODULES))
               || (f.getFileName().toString().equals(BUILD) && !f.getParent().toString().contains(NODE_MODULES)
                   && !f.getParent().toString().contains(BUILD) && !f.getParent().toString().contains(TARGET))
               || (f.getFileName().toString().equals(TARGET) && !f.getParent().toString().contains(BUILD)
                   && !f.getParent().toString().contains(NODE_MODULES) && !f.getParent().toString().contains(TARGET))
               || (f.getFileName().toString().equals(G) && !f.getParent().toString().contains(NODE_MODULES) && !f.getParent().toString().contains(TARGET) && !f.getParent().toString().contains(BUILD))
               || (f.getFileName().toString().equals(I) && !f.getParent().toString().contains(NODE_MODULES) && !f.getParent().toString().contains(TARGET) && !f.getParent().toString().contains(BUILD))
               || (f.getFileName().toString().equals(A) && !f.getParent().toString().contains(NODE_MODULES) && !f.getParent().toString().contains(TARGET) && !f.getParent().toString().contains(BUILD))
               || (f.getFileName().toString().equals(D) && !f.getParent().toString().contains(NODE_MODULES) && !f.getParent().toString().contains(TARGET) && !f.getParent().toString().contains(BUILD))
               || (f.getFileName().toString().equals(F) && !f.getParent().toString().contains(NODE_MODULES) && !f.getParent().toString().contains(TARGET) && !f.getParent().toString().contains(BUILD))

                ;
    }
}
