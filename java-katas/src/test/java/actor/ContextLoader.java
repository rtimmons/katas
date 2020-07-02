package actor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

class ContextLoader {

    Context load(Map<String,Object> root) {
        List<ActorDef> defs = new LinkedList<>();
        ((List<Map<String,Object>>)root.get("actors")).forEach((Map<String,Object> def) -> {
            Object resolved = resolve(def);
            if (resolved instanceof ActorDef) {
                defs.add((ActorDef)resolved);
            }
        });
        List<IActor> actors = defs.stream()
                .map(ActorDef::resolve)
                .collect(toList());
        return new Context(actors);
    }

    Object resolve(Object input) {
        if (input == null) {
            return input;
        }
        if (input instanceof Map) {
            Map<String,Object> map = (Map<String,Object>)input;
            if (map.containsKey("type")) {
                System.out.println(String.format("Loading actor of type %s", map.get("type")));
                Map<String, Object> resolved = map.entrySet().stream().collect(toMap(
                        Map.Entry::getKey,
                        v -> this.resolve(v.getValue())
                ));
                return new ActorDef((String) map.get("type"), resolved);
            }
        }
        if (input instanceof List) {
            List<Map<String,Object>> list = (List<Map<String,Object>>)input;
            return list.stream().map(this::resolve);
        }
        return input;
    }

}
