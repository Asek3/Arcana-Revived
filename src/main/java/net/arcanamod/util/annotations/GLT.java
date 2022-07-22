package net.arcanamod.util.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * \@GenLootTable
 */
public @interface GLT {
	/**
	 * replacement() Item as ResourceLocation.toString()
	 */
	String replacement() default "";
}
