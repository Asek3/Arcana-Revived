package net.arcanamod.util.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GIM {
	
	Type value();

	/**
	 * source() as ResourceLocation.toString()
	 */
	String source() default "";

	enum Type{
		ITEM,
		BLOCK_REF
	}
}
