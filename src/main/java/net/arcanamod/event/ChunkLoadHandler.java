package net.arcanamod.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.ConcurrentHashMultiset;

import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ChunkLoadHandler {

	public static Collection<Consumer<World>> onTick = ConcurrentHashMultiset.create();
	
	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {
		World world = (World) event.getWorld();
		if(!onTick.isEmpty()){
			List<Consumer<World>> temp = new ArrayList<>(onTick);
			temp.forEach(consumer -> consumer.accept(world));
			onTick.removeAll(temp);
		}
	}
	
}
