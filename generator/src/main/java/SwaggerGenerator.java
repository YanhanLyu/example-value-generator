import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.inflector.examples.ExampleBuilder;
import io.swagger.inflector.examples.models.Example;
import io.swagger.models.Model;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.util.Json;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.json.simple.JSONObject;

public class SwaggerGenerator {

    public String toYaml(JsonNode jsonNodeTree) throws JsonProcessingException, IOException{
        // save it as YAML
        String jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree);
        System.out.println(jsonAsYaml);
        return jsonAsYaml;
    }

    public void toSchema(JsonNode jsonNodeTree) {
        String s = "schemas: \n" + jsonNodeTree.get("name").toString().substring(1, jsonNodeTree.get("name").toString().length()-1) + ":\n\ttype: " + jsonNodeTree.get("typeName").toString().substring(1, jsonNodeTree.get("typeName").toString().length()-1) + "\n\tproperties: \n";
        Iterator<String> fieldNames = jsonNodeTree.get("values").fieldNames();
        while(fieldNames.hasNext()){
            String fieldName = fieldNames.next();
            s += "\t\t" + fieldName + ":\n\t\t\ttype: " + jsonNodeTree.get("values").get(fieldName).get("typeName").toString().substring(1,jsonNodeTree.get("values").get(fieldName).get("typeName").toString().length()-1) + "\n"
                    + "\t\t\twrapped: " + jsonNodeTree.get("values").get(fieldName).get("wrapped") + "\n";
        }
        System.out.println(s);
    }

    public static void main(String [] args) throws JsonProcessingException, IOException {
        Swagger swagger = new SwaggerParser().read("http://petstore.swagger.io/v2/swagger.json");

        Map<String, Model> definitions = swagger.getDefinitions();
        Model pet = definitions.get("Pet");
        Example example = ExampleBuilder.fromModel("Pet", pet, definitions, new HashSet<String>());
        String jsonExample = Json.pretty(example);
        // parse JSON
//        System.out.println(jsonExample);
        JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonExample);
        SwaggerGenerator sg = new SwaggerGenerator();
        sg.toSchema(jsonNodeTree);
//        sg.toYaml(jsonNodeTree);
    }
}

