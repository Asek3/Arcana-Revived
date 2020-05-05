package net.arcanamod.items.attachment;

import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.ItemAttachment;
import net.arcanamod.wand.EnumAttachmentType;

import java.util.ArrayList;
import java.util.List;

/**
 * Wand Cap Attachment
 *
 * @author Merijn
 * @see ItemAttachment
 * @see Focus
 */
public class Cap extends ItemAttachment{
	
	private int id;
	private int level;
	
	public static Cap DEFAULT = ArcanaItems.IRON_CAP;
	public static List<Cap> CAPS = new ArrayList<>();
	
	public Cap(String name){
		super(name);
		CAPS.add(this);
	}
	
	@Override
	public EnumAttachmentType getType(){
		return EnumAttachmentType.CAP;
	}
	
	@Override
	public int getID(){
		return id;
	}
	
	public int getLevel(){
		return level;
	}
	
	public Cap setId(int id){
		this.id = id;
		return this;
	}
	
	public Cap setLevel(int level){
		this.level = level;
		return this;
	}
}