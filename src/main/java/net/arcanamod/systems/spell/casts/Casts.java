package net.arcanamod.systems.spell.casts;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.arcanamod.systems.spell.casts.impl.ArmourCast;
import net.arcanamod.systems.spell.casts.impl.DeathCast;
import net.arcanamod.systems.spell.casts.impl.ExchangeCast;
import net.arcanamod.systems.spell.casts.impl.FabricCast;
import net.arcanamod.systems.spell.casts.impl.IceCast;
import net.arcanamod.systems.spell.casts.impl.JourneyCast;
import net.arcanamod.systems.spell.casts.impl.LifeCast;
import net.arcanamod.systems.spell.casts.impl.MiningCast;
import net.arcanamod.systems.spell.casts.impl.VacuumCast;
import net.minecraft.util.ResourceLocation;

public class Casts {
	public static final BiMap<ResourceLocation, ICast> castMap = HashBiMap.create();

	public static final ICast MINING_CAST = new MiningCast();
	public static final ICast EXCHANGE_CAST = new ExchangeCast();
	public static final ICast FABRIC_CAST = new FabricCast();
	public static final ICast VACUUM_CAST = new VacuumCast();
	public static final ICast ARMOUR_CAST = new ArmourCast();
	public static final ICast ICE_CAST = new IceCast();
	public static final ICast LIFE_CAST = new LifeCast();
	public static final ICast DEATH_CAST = new DeathCast();
	public static final ICast JOURNEY_CAST = new JourneyCast();
}
