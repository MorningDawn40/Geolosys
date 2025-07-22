package com.oitsjustjose.geolosys.common.world;

import net.minecraft.world.level.ChunkPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChunkWhitelist {
    private static final Map<String, Set<Long>> loaded = new HashMap<>();

    public static void load(String name, Set<Long> chunks) {
        loaded.put(name, chunks);
    }

    public static void clear() {
        loaded.clear();
    }

    public static boolean isAllowed(String name, ChunkPos pos) {
        if (name == null || name.equalsIgnoreCase("none")) {
            return true;
        }
        Set<Long> set = loaded.get(name);
        return set != null && set.contains(pos.toLong());
    }
}
