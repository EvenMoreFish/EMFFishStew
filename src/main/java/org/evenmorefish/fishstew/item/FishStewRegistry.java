package org.evenmorefish.fishstew.item;

import com.oheers.fish.api.registry.EMFRegistry;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.TreeMap;

public class FishStewRegistry implements EMFRegistry<FishStewItem> {

    private static final FishStewRegistry instance = new FishStewRegistry();

    private final Map<String, FishStewItem> registry = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private FishStewRegistry() {}

    public static @NonNull FishStewRegistry getInstance() {
        return instance;
    }

    @Override
    public @NonNull Map<String, FishStewItem> getRegistry() {
        return Map.copyOf(registry);
    }

    @Override
    public @Nullable FishStewItem get(@NonNull String s) {
        return registry.get(s);
    }

    @Override
    public @NonNull FishStewItem getOrDefault(@NonNull String s, @NonNull FishStewItem fishStewItem) {
        return registry.getOrDefault(s, fishStewItem);
    }

    @Override
    public boolean unregister(@NonNull String s) {
        return registry.remove(s) != null;
    }

    @Override
    public boolean register(@NonNull FishStewItem value, boolean force) {
        if (!force && registry.containsKey(value.getKey())) {
            return false;
        }
        registry.put(value.getKey(), value);
        return true;
    }

    public int getSize() {
        return registry.size();
    }

    public void clear() {
        registry.clear();
    }

}
