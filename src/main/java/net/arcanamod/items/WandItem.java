package net.arcanamod.items;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.CrucibleBlock;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Core;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WandItem extends MagicDeviceItem{
	
	public WandItem(Properties properties){
		super(properties);
	}

	@Override
	public boolean canCraft() {
		return true;
	}

	@Override
	public boolean canUseSpells() {
		return true;
	}

	@Override
	public String getDeviceName() {
		return "item.arcana.wand.variant.wand";
	}

	@Override
	protected float getVisModifier() {
		return 1;
	}

	@Override
	protected float getDifficultyModifier() {
		return 1;
	}

	@Override
	protected float getComplexityModifier() {
		return 1;
	}

	public static ItemStack withCapAndCore(String cap, String core){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("cap", cap);
		nbt.putString("core", core);
		ItemStack stack = new ItemStack(ArcanaItems.WAND.get(), 1);
		stack.setTag(nbt);
		return stack;
	}

	public static ItemStack withCapAndCore(ResourceLocation cap, ResourceLocation core){
		return withCapAndCore(cap.toString(), core.toString());
	}

	public static ItemStack withCapAndCore(Cap cap, Core core){
		return withCapAndCore(cap.getId(), core.getId());
	}

	public ActionResultType onItemUse(ItemUseContext context){
		return convert(context.getWorld(), context.getPos(), context.getPlayer());
	}
	
	public static ActionResultType convert(World world, BlockPos pos, @Nullable PlayerEntity player){
		BlockState state = world.getBlockState(pos);
		if(state.getBlock() == Blocks.CAULDRON){
			world.setBlockState(pos, ArcanaBlocks.CRUCIBLE.get().getDefaultState().with(CrucibleBlock.FULL, state.get(CauldronBlock.LEVEL) >= 2));
			world.playSound(player, pos, SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1, 1);
			for(int i = 0; i < 20; i++)
				world.addParticle(ParticleTypes.END_ROD, pos.getX() + world.rand.nextDouble(), pos.getY() + world.rand.nextDouble(), pos.getZ() + world.rand.nextDouble(), 0, 0, 0);
			return ActionResultType.SUCCESS;
		}
		if(state.getBlock() == Blocks.CRAFTING_TABLE){
			world.setBlockState(pos, ArcanaBlocks.ARCANE_CRAFTING_TABLE.get().getDefaultState());
			world.playSound(player, pos, SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1, 1);
			for(int i = 0; i < 20; i++)
				world.addParticle(ParticleTypes.END_ROD, (pos.getX() - .1f) + world.rand.nextDouble() * 1.2f, (pos.getY() - .1f) + world.rand.nextDouble() * 1.2f, (pos.getZ() - .1f) + world.rand.nextDouble() * 1.2f, 0, 0, 0);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	public int getUseDuration(ItemStack stack){
		return 72000;
	}
	
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items){
		if(isInGroup(group)){
			// iron/wooden, silver/dair, gold/greatwood, thaumium/silverwood, void/arcanium
			items.add(withCapAndCoreForCt("iron_cap", "wood_wand"));
			items.add(withCapAndCoreForCt("silver_cap", "dair_wand"));
			items.add(withCapAndCoreForCt("gold_cap", "greatwood_wand"));
			items.add(withCapAndCoreForCt("thaumium_cap", "silverwood_wand"));
			items.add(withCapAndCoreForCt("void_cap", "arcanium_wand"));
		}
	}
	
	public static ItemStack withCapAndCoreForCt(String cap, String core){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("cap", "arcana:" + cap);
		nbt.putString("core", "arcana:" + core);
		ItemStack stack = new ItemStack(ArcanaItems.WAND.get(), 1);
		stack.setTag(nbt);
		return stack;
	}

}