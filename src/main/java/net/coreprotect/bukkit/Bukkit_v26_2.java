package net.coreprotect.bukkit;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Tag;

import net.coreprotect.model.BlockGroup;

/**
 * Bukkit adapter implementation for Minecraft 26.2/26.3.
 */
public class Bukkit_v26_2 extends Bukkit_v1_21_11 {

    public Bukkit_v26_2() {
        initializeBlockGroups();
    }

    private void initializeBlockGroups() {
        BlockGroup.TRACK_TOP_BOTTOM.add(Material.SULFUR_SPIKE);

        /*
         * New 26.3 blocks (poplar wood set, copper bulbs, copper golem statues) are
         * picked up automatically through the vanilla block tags below, so we don't
         * have to hardcode every material here. We only re-propagate the tag-driven
         * groups into the derived BlockGroup sets, because those were populated in
         * BlockGroup.initialize() before this adapter was constructed.
         */

        // doors / buttons / pressure plates / trapdoors / fence gates (incl. POPLAR_*)
        addAll(Tag.DOORS.getValues(), BlockGroup.DOORS);
        addAll(Tag.BUTTONS.getValues(), BlockGroup.BUTTONS);
        addAll(Tag.PRESSURE_PLATES.getValues(), BlockGroup.PRESSURE_PLATES);
        addAll(Tag.FENCE_GATES.getValues(), BlockGroup.INTERACT_BLOCKS);
        addAll(Tag.FENCE_GATES.getValues(), BlockGroup.SAFE_INTERACT_BLOCKS);
        for (Material trapdoor : Tag.TRAPDOORS.getValues()) {
            if (trapdoor == Material.IRON_TRAPDOOR) {
                continue;
            }
            BlockGroup.INTERACT_BLOCKS.add(trapdoor);
            BlockGroup.SAFE_INTERACT_BLOCKS.add(trapdoor);
        }

        // signs / hanging signs (incl. POPLAR_SIGN, POPLAR_WALL_SIGN, POPLAR_HANGING_SIGN)
        for (Material sign : Tag.SIGNS.getValues()) {
            BlockGroup.SIGNS.add(sign);
            if (!Tag.WALL_SIGNS.isTagged(sign)) {
                BlockGroup.TRACK_TOP.add(sign);
            }
        }
        addAll(Tag.WALL_SIGNS.getValues(), BlockGroup.TRACK_SIDE);
        addAll(Tag.CEILING_HANGING_SIGNS.getValues(), BlockGroup.TRACK_BOTTOM);

        // saplings (incl. POPLAR_SAPLING) and flowers (incl. SHELF_MUSHROOM)
        addAll(Tag.SAPLINGS.getValues(), BlockGroup.TRACK_TOP);
        addAll(Tag.SAPLINGS.getValues(), BlockGroup.NON_ATTACHABLE);
        addAll(Tag.FLOWERS.getValues(), BlockGroup.TRACK_TOP);
        addAll(Tag.FLOWERS.getValues(), BlockGroup.NON_ATTACHABLE);

        // wooden shelves (incl. POPLAR_SHELF) as containers/interactables
        Set<Material> shelves = shelfMaterials();
        addAll(shelves, BlockGroup.CONTAINERS);
        addAll(shelves, BlockGroup.INTERACT_BLOCKS);

        // copper chests (incl. any new weathering variants)
        Set<Material> copperChests = copperChestMaterials();
        addAll(copperChests, BlockGroup.CONTAINERS);
        addAll(copperChests, BlockGroup.INTERACT_BLOCKS);

        // re-propagate the updated base groups into the derived tracking groups
        BlockGroup.TRACK_ANY.addAll(BlockGroup.BUTTONS);
        BlockGroup.TRACK_TOP.addAll(BlockGroup.DOORS);
        BlockGroup.TRACK_TOP.addAll(BlockGroup.PRESSURE_PLATES);
        BlockGroup.LOGGED_BY_LOWER_HALF.addAll(BlockGroup.DOORS);
        BlockGroup.NON_ATTACHABLE.addAll(BlockGroup.BUTTONS);

        // copper bulbs: interactive redstone source blocks
        addIfPresent("COPPER_BULB", BlockGroup.INTERACT_BLOCKS);
        addIfPresent("EXPOSED_COPPER_BULB", BlockGroup.INTERACT_BLOCKS);
        addIfPresent("WEATHERED_COPPER_BULB", BlockGroup.INTERACT_BLOCKS);
        addIfPresent("OXIDIZED_COPPER_BULB", BlockGroup.INTERACT_BLOCKS);
        addIfPresent("WAXED_COPPER_BULB", BlockGroup.INTERACT_BLOCKS);
        addIfPresent("WAXED_EXPOSED_COPPER_BULB", BlockGroup.INTERACT_BLOCKS);
        addIfPresent("WAXED_WEATHERED_COPPER_BULB", BlockGroup.INTERACT_BLOCKS);
        addIfPresent("WAXED_OXIDIZED_COPPER_BULB", BlockGroup.INTERACT_BLOCKS);
        addIfPresent("COPPER_BULB", BlockGroup.UPDATE_STATE);
        addIfPresent("EXPOSED_COPPER_BULB", BlockGroup.UPDATE_STATE);
        addIfPresent("WEATHERED_COPPER_BULB", BlockGroup.UPDATE_STATE);
        addIfPresent("OXIDIZED_COPPER_BULB", BlockGroup.UPDATE_STATE);
        addIfPresent("WAXED_COPPER_BULB", BlockGroup.UPDATE_STATE);
        addIfPresent("WAXED_EXPOSED_COPPER_BULB", BlockGroup.UPDATE_STATE);
        addIfPresent("WAXED_WEATHERED_COPPER_BULB", BlockGroup.UPDATE_STATE);
        addIfPresent("WAXED_OXIDIZED_COPPER_BULB", BlockGroup.UPDATE_STATE);

        // copper golem statues (new decorative block set in 26.3)
        addAll(Tag.COPPER_GOLEM_STATUES.getValues(), BlockGroup.INTERACT_BLOCKS);
        addAll(Tag.COPPER_GOLEM_STATUES.getValues(), BlockGroup.TRACK_TOP_BOTTOM);
        addAll(Tag.COPPER_GOLEM_STATUES.getValues(), BlockGroup.LOGGED_BY_LOWER_HALF);
    }

    private static void addAll(Iterable<Material> materials, Set<Material> target) {
        for (Material material : materials) {
            target.add(material);
        }
    }

    private static void addIfPresent(String name, Set<Material> target) {
        Material material = Material.getMaterial(name);
        if (material != null) {
            target.add(material);
        }
    }

}
