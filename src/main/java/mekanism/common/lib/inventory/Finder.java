package mekanism.common.lib.inventory;

import mekanism.common.lib.WildcardMatcher;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public interface Finder {

    public static final Finder ANY = stack -> true;

    public static Finder item(ItemStack itemType) {
        return stack -> ItemStack.areItemsEqual(itemType, stack);
    }

    public static Finder strict(ItemStack itemType) {
        return stack -> ItemHandlerHelper.canItemStacksStack(itemType, stack);
    }

    public static Finder tag(String tagName) {
        return stack -> {
            if (stack.isEmpty()) {
                return false;
            }
            return stack.getItem().getTags().stream().anyMatch(tag -> WildcardMatcher.matches(tagName, tag.toString()));
        };
    }

    public static Finder modID(String modID) {
        return stack -> {
            if (stack.isEmpty()) {
                return false;
            }
            return WildcardMatcher.matches(modID, stack.getItem().getRegistryName().getNamespace());
        };
    }

    public static Finder material(Material materialType) {
        return stack -> {
            if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem)) {
                return false;
            }
            return Block.getBlockFromItem(stack.getItem()).getDefaultState().getMaterial() == materialType;
        };
    }

    boolean modifies(ItemStack stack);
}