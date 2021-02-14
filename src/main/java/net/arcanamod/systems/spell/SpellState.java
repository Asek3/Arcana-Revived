package net.arcanamod.systems.spell;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.client.gui.FociForgeScreen;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.StartSpellModule;
import net.arcanamod.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/// HEAVY IN PROGRESS
/// This and FociForgeScreen are in progress
/// missteps and bad design are prominent and will be fixed
public class SpellState {

    private static final ResourceLocation SPELL_RESOURCES = new ResourceLocation(Arcana.MODID, "textures/gui/container/foci_forge_minigame.png");

    private static final float MIN_BOUND = -2048;
    private static final float MAX_BOUND = 2047;


    private float x = 0;
    private float y = 0;
    public boolean active = false;
    public SpellModule activeModule = null;
    public SpellModule mainModule = null;
    public List<SpellModule> isolated = new LinkedList<>();
    public Map<UUID, SpellModule> floating = new HashMap<>();

    private void move(float x, float y) {
        this.x = Math.min(Math.max(MIN_BOUND, this.x + x), MAX_BOUND);
        this.y = Math.min(Math.max(MIN_BOUND, this.y + y), MAX_BOUND);
    }

    // Return x mod 16 where -16 <= ret <= 0
    private static float getTopLeftBackgroundLocation(float val) {
        if (val < 0) {
            return val % 16;
        } else {
            return (val % 16) - 16;
        }
    }

    public void moduleSelected(int i) {
        Class<? extends SpellModule> Module = SpellModule.byIndex.get(i);
        if (Module == null) {
            // deselection logic
            activeModule = null;
        } else {
            if ((mainModule == null && !Module.isAssignableFrom(StartSpellModule.class))
                || mainModule != null && Module.isAssignableFrom(StartSpellModule.class)) {

                try {
                    activeModule = Module.getConstructor().newInstance();
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void mouseDownPlacement(double x, double y, int button, Aspect currentAspect) {
        if (activeModule != null) {
            activeModule.mouseDownPlacement((int)x, (int)y);
        }
    }

    public void drag(double x, double y, int button, double move_x, double move_y) {
        if (activeModule == null) {
            move((float)move_x, (float)move_y);
        }
    }

    public void mouseUpPlacement(double x, double y, int button, Aspect currentAspect) {
        if (activeModule != null) {
            boolean validPlacement = activeModule.mouseUpPlacement(this, (int)x, (int)y);
            if (validPlacement) {
                isolated.add(activeModule);
            }
            activeModule = null;
        }
    }

    private Stream<SpellModule> getBoundRecurse(SpellModule module) {
        if (module == null) {
            return Stream.empty();
        } else {
            return Stream.concat(Stream.of(module), module.getBoundModules().stream());
        }
    }

    private Stream<SpellModule> getPlacedModules() {
        Stream<SpellModule> stream = getBoundRecurse(mainModule);
        for (SpellModule module : isolated) {
            stream = Stream.concat(stream, getBoundRecurse(module));
        }
        return stream;
    }

    @Nullable
    private SpellModule getModuleAt(int x, int y) {
        return getPlacedModules().filter(module -> module.withinBounds(x, y)).findFirst().orElse(null);
    }

    public Stream<SpellModule> getCollidingModules(int x, int y, SpellModule module) {
        return getPlacedModules()
                .filter(other -> module != other)
                .filter(other -> other.collidesWith(module));
    }

    private boolean canPlace(int x, int y, SpellModule module) {
        boolean valid = true;
        SpellModule collision = getCollidingModules(x, y, module).findFirst().orElse(null);
        if (collision != null) {
            List<SpellModule> special = getCollidingModules(x, y, module)
                .filter(collide -> module.getSpecialPoint(collide) == null)
                .limit(2)
                .collect(Collectors.toList());
            if (special.size() == 0) {
                valid = false;
            } else {
                SpellModule specialMain;
                if (special.size() == 1) {
                    specialMain = special.get(0);
                } else {
                    // ignore previous result: get module underneath cursor
                    specialMain = getModuleAt(x, y);
                    if (specialMain == null || !specialMain.canConnectSpecial(module)) {
                        valid = false;
                    }
                }

                // check if a potential special placement candidate exists
                if (valid) {
                    Point specialPoint = specialMain.getSpecialPoint(module);
                    List<SpellModule> specialCollisions = getCollidingModules(specialPoint.x, specialPoint.y, module)
                        .limit(2)
                        .collect(Collectors.toList());
                    if (specialCollisions.size() > 1
                        || (specialCollisions.size() == 1
                            && specialCollisions.get(0) != specialMain)) {
                        valid = false;
                    }
                }
            }
        }

        return valid;
    }

    public boolean isValidDeletion(SpellModule module) {
        return false;
    }

    public void place(int x, int y, SpellModule module, boolean isRemote) {
        if(module != null && canPlace(x, y, module)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
            }
            // place module
        }
    }

    public void raise(int x, int y, boolean isRemote) {
        SpellModule raising = getModuleAt(x, y);
        if (raising != null && raising.canRaise(this)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
            }
            // raise module
        }
    }

    public void lower(int x, int y, int newX, int newY, boolean isRemote) {
        SpellModule lowering = getModuleAt(x, y);

        if (lowering != null && canPlace(x, y, lowering)) {
            if (x == newX && y == newY) {
                // send action to server
                // manage client-side state
            }
            // lower module
        }
    }

    public void connect(SpellModule a, SpellModule b, boolean isRemote) {
        if (a != null && b != null && a.canConnect(b)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
            }
            // connect the modules
        }
    }

    public void delete(int x, int y, boolean isRemote) {
        SpellModule deleting = getModuleAt(x, y);
        if (isValidDeletion(deleting)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
            }
            // delete module
        }
    }

    public void assign(int x, int y, Aspect aspect, boolean isRemote) {
        SpellModule assigning = getModuleAt(x, y);
        if (assigning != null && assigning.canAssign(x, y, aspect)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
            }
            // assign aspect to module
        }
    }





    public void render(int guiLeft, int guiTop, int spellLeft, int spellTop, int width, int height, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();

        mc.getTextureManager().bindTexture(FociForgeScreen.BG);
        if (mainModule != null) {
            // Start point

            for (int i = 1; i < 9; i++) {
                UiUtil.drawModalRectWithCustomSizedTexture(
                        guiLeft + FociForgeScreen.MODULE_X + FociForgeScreen.MODULE_DELTA * i,
                        guiTop + FociForgeScreen.MODULE_Y,
                        32 * i, 313, 32, 32, 397, 397);
            }
        }

        mc.getTextureManager().bindTexture(SPELL_RESOURCES);
        // Render selected module under mouse cursor
        if (activeModule != null) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            activeModule.renderUnderMouse(mouseX, mouseY);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }

        GL11.glPushMatrix();
        // move 0, 0 to spell window
        GL11.glTranslatef(spellLeft, spellTop, 0);

        // Scissors test: In this section, rendering outside this window does nothing.
        double gui_scale = mc.getMainWindow().getGuiScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        // TODO: De-magic '85' because I don't know what it is
        GL11.glScissor((int)(gui_scale * spellLeft), (int)(gui_scale * (spellTop + 85)), (int)(gui_scale * width), (int)(gui_scale * height));

        // draw background
        int bg_texX = (mainModule == null ? 16 : 0);
        float start_x = getTopLeftBackgroundLocation(this.x);
        float start_y = getTopLeftBackgroundLocation(this.y);
        for (float bg_x = start_x; bg_x < width + 16; bg_x += 16) {
            for (float bg_y = start_y; bg_y < height + 16; bg_y += 16) {
                UiUtil.drawTexturedModalRect((int)Math.floor(bg_x), (int)Math.floor(bg_y), bg_texX, 0, 16, 16);
            }
        }

        Queue<Pair<SpellModule, SpellModule>> renderQueue = new LinkedList<>();
        if (mainModule != null) {
            renderQueue.add(new Pair<>(null, mainModule));
        }
        for (SpellModule floater : isolated) {
           renderQueue.add(new Pair<>(null, floater));
        }

        while(!renderQueue.isEmpty()) {
            Pair<SpellModule, SpellModule> next = renderQueue.remove();
            SpellModule root = next.getFirst();
            SpellModule base = next.getSecond();
            if (root != null) {
                // render connector
            }
            base.renderInMinigame(mouseX, mouseY);
            for (SpellModule bound : base.getBoundModules()) {
                renderQueue.add(new Pair<>(base, bound));
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }
}
