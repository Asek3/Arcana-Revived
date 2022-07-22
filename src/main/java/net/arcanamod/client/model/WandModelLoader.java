package net.arcanamod.client.model;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WandModelLoader implements IModelLoader<WandModelGeometry>{
	public void onResourceManagerReload(IResourceManager resourceManager){}
	
	public WandModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
		return new WandModelGeometry(null, null, null, null);
	}
}
