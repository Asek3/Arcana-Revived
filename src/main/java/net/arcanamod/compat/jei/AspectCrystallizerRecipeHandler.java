package net.arcanamod.compat.jei;

import java.util.ArrayList;
import java.util.List;

import net.arcanamod.aspects.Aspect;

public class AspectCrystallizerRecipeHandler {
	public static List<AspectCrystallizerRecipeHandler> RECIPES = new ArrayList<>();
	public Aspect aspect;
	
	public AspectCrystallizerRecipeHandler(Aspect aspect){
		this.aspect = aspect;
		
		if (RECIPES.stream().noneMatch(m -> m.toString().equals(this.toString())))
			RECIPES.add(this);
	}
}