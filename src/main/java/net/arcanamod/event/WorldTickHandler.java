package net.arcanamod.event;

import net.arcanamod.world.AuraView;
import net.arcanamod.world.ServerAuraView;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class WorldTickHandler{
		
	@SubscribeEvent
	public static void tickEnd(TickEvent.WorldTickEvent event) {
		if(event.phase == TickEvent.Phase.END) {
			World world = event.world;
			
			if(world instanceof ServerWorld) {
				ServerWorld serverWorld = (ServerWorld)world;
				AuraView view = new ServerAuraView(serverWorld);
				view.getAllNodes().forEach(node -> node.type().tick(serverWorld, view, node));
				if(event.world.getGameTime() % 6 == 0)
					view.tickTaintLevel();
			}
			
		}
	}
}