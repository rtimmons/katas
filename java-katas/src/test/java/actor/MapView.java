package actor;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

class MapView {
    final ImmutableMap<String,Object> delegate;

    public MapView(Map<String, Object> delegate) {
        this.delegate = ImmutableMap.copyOf(delegate);
    }

    public void forEach(BiConsumer<String,Object> consumer) {
        this.delegate.forEach(consumer);
    }

    public Set<Map.Entry<String,Object>> entrySet() {
        return this.delegate.entrySet();
    }
}
