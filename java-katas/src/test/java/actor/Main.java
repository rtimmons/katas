package actor;

import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class Main {

    void main(final String fileName) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        ContextLoader loader = new ContextLoader();

        Map<String,Object> plan = (Map<String,Object>) yaml.load(new FileReader(fileName));
        Context context = loader.load(plan);

        context.run();
    }

    public static void main(String...args) throws FileNotFoundException {
        new Main().main(args[0]);
    }

}
