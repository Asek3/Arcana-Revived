package net.arcanamod.items;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.systems.spell.Spell;
import net.arcanamod.systems.spell.casts.Cast;
import net.arcanamod.systems.spell.casts.Casts;
import net.arcanamod.systems.spell.casts.ICast;
import net.arcanamod.systems.taint.Taint;
import net.arcanamod.util.RayTraceUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VisManipulatorsItem extends Item{
	
	public VisManipulatorsItem(Properties properties){
		super(properties);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context){
			onItemUseFirst(null, context);
			return ActionResultType.SUCCESS;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		if (context.getWorld().getBlockState(context.getPos()).getBlock() == Blocks.SPAWNER) {
			AtomicInteger i = new AtomicInteger();
			AtomicInteger j = new AtomicInteger();
			Taint.getTaintedEntities().forEach(entityType -> {
				Entity e = entityType.create(context.getWorld());
				e.setPosition(context.getPos().getX()-j.get(),context.getPos().getY()+2,context.getPos().getZ()+ i.get());
				//e.setNoGravity(true);
				e.addTag("NoAI");
				e.setMotion(0,0,0);
				context.getWorld().addEntity(e);
				i.addAndGet(2);
				if (i.get()>=10){
					i.set(0);
					j.addAndGet(1);
				}
			});
			return ActionResultType.SUCCESS;
		}else if (context.getWorld().getBlockState(context.getPos()).getBlock() == ArcanaBlocks.ASPECT_TESTER.get()) {
			ItemStack toSet = new ItemStack(ArcanaItems.DEFAULT_FOCUS.get(), 1);
			int r = random.nextInt(1);
			if (r == 0) {
				toSet.setTag(Spell.Samples.createBasicSpell().toNBT(new CompoundNBT()));
			}
			toSet.getOrCreateTag().putInt("style", random.nextInt(36));
			context.getPlayer().addItemStackToInventory(toSet);
		}
		return super.onItemUseFirst(stack, context);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		BlockPos pos = RayTraceUtils.getTargetBlockPos(playerIn,worldIn,80);
		if (worldIn.getBlockState(pos).getBlock()== ArcanaBlocks.SMOKEY_GLASS.get()){
			((Cast)Casts.ICE_CAST).createAOEBlast(playerIn,worldIn,pos.up(),8,true, false, 3);
		}else if (worldIn.getBlockState(pos).getBlock()== ArcanaBlocks.HARDENED_GLASS.get()){
			((Cast)Casts.ICE_CAST).createAOEBlast(playerIn,worldIn,pos.up(),8,true, false, 1);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	public ICast getSpell(){
		return null;
	}
}