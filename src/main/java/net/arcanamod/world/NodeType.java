package net.arcanamod.world;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.client.render.ArcanaParticles;
import net.arcanamod.client.render.NodeParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.*;

import static net.arcanamod.Arcana.arcLoc;

// Although IDEA complains about class loading deadlock, this only occurs under specific conditions.
// Handles type-specific things, such as behaviour and vis generation rates.
@SuppressWarnings("StaticInitializerReferencesSubClass")
public abstract class NodeType{
	
	// A diet registry, used for serialization and deserialization.
	public static final BiMap<ResourceLocation, NodeType> TYPES = HashBiMap.create(6);
	public static final Set<NodeType> GENERATED_TYPES = new HashSet<>(5);
	
	public static final NodeType
			NORMAL = new NormalNodeType(),
			BRIGHT = new BrightNodeType(),
			PALE = new PaleNodeType(),
			ELDRITCH = new EldritchNodeType(),
			HUNGRY = new HungryNodeType(),
			TAINTED = new TaintedNodeType();
	
	public static void init(){
		TYPES.put(arcLoc("normal"), NORMAL);
		TYPES.put(arcLoc("bright"), BRIGHT);
		TYPES.put(arcLoc("pale"), PALE);
		TYPES.put(arcLoc("eldritch"), ELDRITCH);
		TYPES.put(arcLoc("hungry"), HUNGRY);
		TYPES.put(arcLoc("tainted"), TAINTED);
		
		GENERATED_TYPES.add(NORMAL);
		GENERATED_TYPES.add(BRIGHT);
		GENERATED_TYPES.add(PALE);
		GENERATED_TYPES.add(ELDRITCH);
		GENERATED_TYPES.add(HUNGRY);
	}
	
	public void tick(IWorld world, INodeView nodes, Node node){
		if(world.isRemote()){
			//world.addParticle(ParticleTypes.CLOUD, node.getX(), node.getY(), node.getZ(), random.nextGaussian() / 6, random.nextGaussian() / 6, random.nextGaussian() / 6);
			world.addParticle(new NodeParticleData(node.nodeUniqueId(), node.type().texture(world, nodes, node), ArcanaParticles.NODE_PARTICLE.get()), node.getX(), node.getY(), node.getZ(), 0, 0, 0);
		}
	}
	
	public abstract ResourceLocation texture(IWorld world, INodeView nodes, Node node);
	public abstract Collection<ResourceLocation> textures();
	
	/**
	 * The aspects that a new node of this type will have.
	 */
	// default impl should handle normal nodes; maybe bright and pale ones.
	public Reference2IntMap<Aspect> genNodeAspects(BlockPos location, IWorld world, Random random){
		return new Reference2IntOpenHashMap<>();
	}
	
	public static class NormalNodeType extends NodeType{
		
		public ResourceLocation texture(IWorld world, INodeView nodes, Node node){
			return arcLoc("nodes/normal_node");
		}
		
		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/normal_node"));
		}
	}
	
	public static class BrightNodeType extends NodeType{
		
		public ResourceLocation texture(IWorld world, INodeView nodes, Node node){
			return arcLoc("nodes/bright_node");
		}
		
		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/brightest_node"));
		}
	}
	
	public static class PaleNodeType extends NodeType{
		
		public ResourceLocation texture(IWorld world, INodeView nodes, Node node){
			return arcLoc("nodes/fading_node");
		}
		
		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/fading_node"));
		}
	}
	
	public static class EldritchNodeType extends NodeType{
		
		public ResourceLocation texture(IWorld world, INodeView nodes, Node node){
			return arcLoc("nodes/eldritch_node");
		}
		
		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/eldritch_node"));
		}
	}
	
	public static class HungryNodeType extends NodeType{
		
		public ResourceLocation texture(IWorld world, INodeView nodes, Node node){
			return arcLoc("nodes/hungry_node");
		}
		
		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/hungry_node"));
		}
	}
	
	public static class TaintedNodeType extends NodeType{
		
		public ResourceLocation texture(IWorld world, INodeView nodes, Node node){
			return arcLoc("nodes/tainted_node");
		}
		
		public Collection<ResourceLocation> textures(){
			return Collections.singleton(arcLoc("nodes/tainted_node"));
		}
		
	}
}