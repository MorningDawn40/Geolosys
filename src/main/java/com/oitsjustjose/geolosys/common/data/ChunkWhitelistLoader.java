package com.oitsjustjose.geolosys.common.data;

import com.google.gson.*;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.world.ChunkWhitelist;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.ChunkPos;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChunkWhitelistLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public ChunkWhitelistLoader() {
        super(GSON, "whitelists");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectMap, ResourceManager manager, ProfilerFiller profiler) {
        ChunkWhitelist.clear();

        objectMap.forEach((resLoc, element) -> {
            try {
                JsonObject root = element.getAsJsonObject();
                JsonArray arr = root.getAsJsonArray("allowed_chunks");
                Set<Long> chunks = new HashSet<>();

                for (JsonElement e : arr) {
                    JsonArray pair = e.getAsJsonArray();
                    int x = pair.get(0).getAsInt();
                    int z = pair.get(1).getAsInt();
                    chunks.add(ChunkPos.asLong(x, z));
                }

                String name = resLoc.getPath().replace(".json", "");
                ChunkWhitelist.load(name, chunks);
                Geolosys.getInstance().LOGGER.info("Loaded chunk whitelist '{}', {} entries", name, chunks.size());

            } catch (Exception e) {
                Geolosys.getInstance().LOGGER.warn("Failed to load chunk whitelist '{}': {}", resLoc, e.getMessage());
            }
        });
    }
}
