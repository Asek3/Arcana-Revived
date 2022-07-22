package net.arcanamod.datagen;

import static com.google.common.collect.Maps.newHashMap;
import static net.arcanamod.ArcanaVariables.arcLoc;

import java.util.Map;

import net.arcanamod.Arcana;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ArcanaDataGenerators{
	
	public static ResourceLocation DAIR = new ResourceLocation(Arcana.MODID, "block/dair_planks");
	public static ResourceLocation DEAD = new ResourceLocation(Arcana.MODID, "block/dead_planks");
	public static ResourceLocation EUCALYPTUS = new ResourceLocation(Arcana.MODID, "block/eucalyptus_planks");
	public static ResourceLocation HAWTHORN = new ResourceLocation(Arcana.MODID, "block/hawthorn_planks");
	public static ResourceLocation GREATWOOD = new ResourceLocation(Arcana.MODID, "block/greatwood_planks");
	public static ResourceLocation SILVERWOOD = new ResourceLocation(Arcana.MODID, "block/silverwood_planks");
	public static ResourceLocation TRYPOPHOBIUS = new ResourceLocation(Arcana.MODID, "block/trypophobius_planks");
	public static ResourceLocation WILLOW = new ResourceLocation(Arcana.MODID, "block/willow_planks");
	
	public static ResourceLocation ARCANE_STONE = new ResourceLocation(Arcana.MODID, "block/arcane_stone");
	public static ResourceLocation ARCANE_STONE_BRICKS = new ResourceLocation(Arcana.MODID, "block/arcane_stone_bricks");
	public static ResourceLocation DUNGEON_BRICKS = new ResourceLocation(Arcana.MODID, "block/dungeon_bricks");
	public static ResourceLocation CRACKED_DUNGEON_BRICKS = new ResourceLocation(Arcana.MODID, "block/cracked_dungeon_bricks");
	public static ResourceLocation MOSSY_DUNGEON_BRICKS = new ResourceLocation(Arcana.MODID, "block/mossy_dungeon_bricks");
	
	public static Map<String, ResourceLocation> LIVING_WOODS = newHashMap();
	public static Map<String, ResourceLocation> WOODS = newHashMap();
	
	public static Map<String, ResourceLocation> STONES = newHashMap();
	
	static{
		LIVING_WOODS.put("dair", DAIR);
		LIVING_WOODS.put("eucalyptus", EUCALYPTUS);
		LIVING_WOODS.put("hawthorn", HAWTHORN);
		LIVING_WOODS.put("greatwood", GREATWOOD);
		LIVING_WOODS.put("silverwood", SILVERWOOD);
		LIVING_WOODS.put("willow", WILLOW);
		LIVING_WOODS.put("tainted_oak", arcLoc("block/tainted_oak_planks"));
		LIVING_WOODS.put("tainted_birch", arcLoc("block/tainted_birch_planks"));
		LIVING_WOODS.put("tainted_spruce", arcLoc("block/tainted_spruce_planks"));
		LIVING_WOODS.put("tainted_dair", arcLoc("block/tainted_dair_planks"));
		LIVING_WOODS.put("tainted_eucalyptus", arcLoc("block/tainted_eucalyptus_planks"));
		LIVING_WOODS.put("tainted_hawthorn", arcLoc("block/tainted_hawthorn_planks"));
		LIVING_WOODS.put("tainted_greatwood", arcLoc("block/tainted_greatwood_planks"));
		//LIVING_WOODS.put("tainted_silverwood", arcLoc("block/tainted_silverwood_planks"));
		LIVING_WOODS.put("tainted_willow", arcLoc("block/tainted_willow_planks"));
		WOODS.putAll(LIVING_WOODS);
		WOODS.put("trypophobius", TRYPOPHOBIUS);
		WOODS.put("dead", DEAD);
		
		STONES.put("arcane_stone", ARCANE_STONE);
		STONES.put("arcane_stone_bricks", ARCANE_STONE_BRICKS);
		STONES.put("dungeon_bricks", DUNGEON_BRICKS);
		STONES.put("cracked_dungeon_bricks", CRACKED_DUNGEON_BRICKS);
		STONES.put("mossy_dungeon_bricks", MOSSY_DUNGEON_BRICKS);
	}
	
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event){
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper efh = event.getExistingFileHelper();
		if(event.includeClient()){
			generator.addProvider(new BlockModels(generator, efh));
			generator.addProvider(new Blockstates(generator, efh));
			generator.addProvider(new ItemModels(generator, efh));
		}
		if(event.includeServer()){
			generator.addProvider(new LootTables(generator));
		}
	}
}