package net.arcanamod.blocks.tiles;

import java.awt.Color;
import java.util.Collections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.AspectLabel;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.VisShareable;
import net.arcanamod.aspects.handlers.AspectBattery;
import net.arcanamod.aspects.handlers.AspectHandler;
import net.arcanamod.aspects.handlers.AspectHandlerCapability;
import net.arcanamod.aspects.handlers.VisUtils;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.JarBlock;
import net.arcanamod.blocks.pipes.TubeTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class JarTileEntity extends TileEntity implements ITickableTileEntity, VisShareable{
	private final JarBlock.Type jarType;
	public AspectBattery vis = new AspectBattery(/*1, 100*/);
	public AspectLabel label;
	private double lastVis;
	
	private double clientVis;
	private final double visAnimationSpeed = ArcanaConfig.JAR_ANIMATION_SPEED.get();
	
	private static final int MAX_PUSH = 4;
	
	public JarTileEntity(JarBlock.Type type){
		super(ArcanaTiles.JAR_TE.get());
		this.jarType = type;
		vis = new AspectBattery();
		vis.initHolders(100, 1);
		vis.getHolder(0).setVoids(type == JarBlock.Type.VOID);
	}
	
	/**
	 * @deprecated This is required when TileEntity is created. Please use JarTileEntity(JarBlock.Type).
	 */
	@Deprecated
	public JarTileEntity(){
		super(ArcanaTiles.JAR_TE.get());
		this.jarType = JarBlock.Type.BASIC;
	}
	
	@Override
	public void read(BlockState state, CompoundNBT compound){
		super.read(state, compound);
		vis.deserializeNBT(compound.getCompound("aspects"));
		clientVis = vis.getHolder(0).getStack().getAmount();
		if(compound.contains("label")){
			if(label == null)
				label = new AspectLabel(Direction.byIndex(compound.getInt("label")));
			else
				label.direction = Direction.byIndex(compound.getInt("label"));
			label.seal = AspectUtils.getAspect(compound, "seal");
		}
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound){
		CompoundNBT aspectsNbt = vis.serializeNBT();
		compound.put("aspects", aspectsNbt);
		if(label != null){
			compound.putInt("label", label.direction.getIndex());
			AspectUtils.putAspect(compound, "seal", label.seal);
		}
		return super.write(compound);
	}
	
	public Color getAspectColor(){
		if(this.getWorld().getBlockState(this.getPos().down()).getBlock() == ArcanaBlocks.ASPECT_TESTER.get())
			return getCreativeJarColor();
		else
			return !vis.getHolder(0).getStack().isEmpty() ? new Color(vis.getHolder(0).getStack().getAspect().getColorRange().get(2)) : Color.WHITE;
	}
	
	public int nextColor = 0;
	
	public Color getCreativeJarColor(){
		nextColor++;
		if(nextColor >= 800)
			nextColor = 0;
		
		final int ARRAY_SIZE = 100;
		double jump = 360.0 / (ARRAY_SIZE * 1.0);
		int[] colors = new int[ARRAY_SIZE];
		for(int i = 0; i < colors.length; i++)
			colors[i] = Color.HSBtoRGB((float)(jump * i), 1.0f, 1.0f);
		return new Color(colors[nextColor / 8]);
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap){
		if(cap == AspectHandlerCapability.ASPECT_HANDLER)
			return vis.getCapability(AspectHandlerCapability.ASPECT_HANDLER).cast();
		return LazyOptional.empty();
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
		return getCapability(cap);
	}
	
	public CompoundNBT getUpdateTag(){
		return write(new CompoundNBT());
	}
	
	@Override
	public boolean isVisShareable(){
		return true;
	}
	
	@Override
	public boolean isManual(){
		return false;
	}
	
	@Override
	public boolean isSecure(){
		return jarType == JarBlock.Type.SECURED;
	}
	
	@Override
	public void tick(){
		if(label != null && (label.seal == Aspects.EMPTY && label.seal != vis.getHolder(0).getStack().getAspect())){
			label.seal = vis.getHolder(0).getStack().getAspect();
			vis.getHolder(0).setWhitelist(Collections.singletonList(label.seal));
		}
		double newVis = vis.getHolder(0).getStack().getAmount();
		if(lastVis != newVis && world != null)
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
		lastVis = newVis;
		if(!getJarType().equals(JarBlock.Type.SECURED))
			vis.getHolder(0).getStack().getAspect().aspectTick(this);
		if(clientVis > newVis)
			clientVis = Math.max(clientVis - visAnimationSpeed, newVis);
		else if(clientVis < newVis)
			clientVis = Math.min(clientVis + visAnimationSpeed, newVis);
		
		TileEntity entity = world.getTileEntity(pos.up());
		if(entity instanceof TubeTileEntity)
			if(getJarType() == JarBlock.Type.VACUUM) // pull in
				VisUtils.moveAllAspects(AspectHandler.getFrom(entity), vis, MAX_PUSH);
			else if(getJarType() == JarBlock.Type.PRESSURE) // push out
				VisUtils.moveAllAspects(vis, AspectHandler.getFrom(entity), MAX_PUSH);
	}
	
	public double getClientVis(float partialTicks){
		float newVis = vis.getHolder(0).getStack().getAmount();
		if(clientVis > newVis)
			return Math.max(clientVis - (visAnimationSpeed * partialTicks), newVis);
		else if(clientVis < newVis)
			return Math.min(clientVis + (visAnimationSpeed * partialTicks), newVis);
		return clientVis;
	}
	
	public JarBlock.Type getJarType(){
		return jarType;
	}
	
	public ResourceLocation getPaperAspectLocation(){
		return new ResourceLocation((label != null ? label.seal : Aspects.EMPTY).toResourceLocation().toString()
				.replace(":", ":aspect/paper/paper_"));
	}
	
	public @Nullable
	Direction getLabelSide(){
		return label != null ? label.direction : null;
	}
}