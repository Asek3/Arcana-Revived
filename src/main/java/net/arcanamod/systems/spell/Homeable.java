package net.arcanamod.systems.spell;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

public interface Homeable {
    static <T extends Entity & Homeable> void startHoming(T toHome) {
        int s = 15; // size of box
        if (toHome.getHomeables().size() > 0){
            AxisAlignedBB box = new AxisAlignedBB(
                    toHome.getPosX()-s,toHome.getPosY()-s,toHome.getPosZ()-s,
                    toHome.getPosX()+s,toHome.getPosY()+s,toHome.getPosZ()+s
            );
            for (Class<? extends Entity> homeTarget : toHome.getHomeables()){
                List<Entity> entitiesWithinBox = toHome.world.getEntitiesWithinAABB((Class<? extends Entity>) homeTarget,box,Entity::isAlive);
                if (entitiesWithinBox.size() > 0){
                    for (Entity entity : entitiesWithinBox){
                        toHome.move(MoverType.PLAYER,getVecDistance(entity, toHome,10f));
                    }
                }
            }
        }
    }

    // JAVA 11 pls
    /*private */static <E extends Entity> Vector3d getVecDistance(E homing, E entity, float pDM){
        return new Vector3d(
                (homing.getPosX()-entity.getPosX())/pDM,
                0,
                (homing.getPosZ()-entity.getPosZ())/pDM
        );
    }

    List<Class<? extends Entity>> getHomeables();
}
