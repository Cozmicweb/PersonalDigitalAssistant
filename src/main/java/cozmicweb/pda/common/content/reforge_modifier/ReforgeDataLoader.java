package cozmicweb.pda.common.content.reforge_modifier;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import cozmicweb.pda.common.PDACommon;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import org.jspecify.annotations.NonNull;

import java.util.Map;

@EventBusSubscriber(modid = PDACommon.MOD_ID)
public class ReforgeDataLoader extends SimpleJsonResourceReloadListener<JsonObject> {
    private static final Codec<JsonObject> JSON_OBJECT_CODEC = Codec.PASSTHROUGH.comapFlatMap(
            dynamic -> {
                JsonElement element = (JsonElement) dynamic.getValue();
                if (element != null && element.isJsonObject()) {
                    return DataResult.success(element.getAsJsonObject());
                }
                return DataResult.error(() -> "Data file is not a valid JSON object");
            },
            jsonObject -> new Dynamic<>(JsonOps.INSTANCE, jsonObject)
    );

    public ReforgeDataLoader() {
        super(JSON_OBJECT_CODEC, FileToIdConverter.json("reforges"));
    }

    @Override
    protected void apply(@NonNull Map<Identifier, JsonObject> prepared, @NonNull ResourceManager manager, @NonNull ProfilerFiller profiler) {
        ReforgeModifierRegistry.load(prepared);
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddServerReloadListenersEvent event) {
        event.addListener(PDACommon.id("reforge_data_loader"), new ReforgeDataLoader());
    }
}