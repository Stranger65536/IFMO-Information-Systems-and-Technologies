import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Preconditions;
import generators.UserContextFactory;
import ontology.UserContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int ONTOLOGY_NUMBER = 3;

    public static void main(final String[] args) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();

        final List<UserContext> originalOntologies = new ArrayList<>(ONTOLOGY_NUMBER);

        for (int i = 0; i < ONTOLOGY_NUMBER; i++) {
            final String filename = "Ontology_" + i + ".json";
            final UserContext context = UserContextFactory.ABSTRACT_USER_CONTEXT.makeContext();
            originalOntologies.add(context);
            mapper.enable(SerializationFeature.INDENT_OUTPUT).writeValue(new File(filename), context);
        }

        try {
            final File currentDir = Paths.get(".").toFile();
            final File[] files = currentDir.listFiles(pathname ->
                    pathname.isFile() && pathname.getName().endsWith("json"));
            final List<UserContext> readOntologies = new ArrayList<>(ONTOLOGY_NUMBER);

            for (File file : files) {
                readOntologies.add(mapper.readValue(file, UserContext.class));
            }

            for (int i = 0; i < ONTOLOGY_NUMBER; i++) {
                Preconditions.checkArgument(originalOntologies.get(i).equals(readOntologies.get(i)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
