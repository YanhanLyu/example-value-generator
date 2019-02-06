import io.swagger.inflector.examples.ExampleBuilder;
import io.swagger.inflector.examples.models.Example;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.util.Json;

import java.util.HashSet;
import java.util.Map;


public class SwaggerGenerator {
    public static void main(String [] args) {
        Swagger swagger = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");

        Map<String, Model> definitions = swagger.getDefinitions();
        Model pet = definitions.get("Pet");
        Example example = ExampleBuilder.fromModel("Pet", pet, definitions, new HashSet<String>());

        String jsonExample = Json.pretty(example);
        System.out.println(jsonExample);


    }
}

