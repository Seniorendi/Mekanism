package mekanism.additions.client;

import java.util.Map;
import mekanism.additions.client.model.AdditionsModelCache;
import mekanism.additions.client.render.entity.RenderBabyCreeper;
import mekanism.additions.client.render.entity.RenderBabyEnderman;
import mekanism.additions.client.render.entity.RenderBalloon;
import mekanism.additions.client.render.entity.RenderObsidianTNTPrimed;
import mekanism.additions.common.MekanismAdditions;
import mekanism.additions.common.item.AdditionsSpawnEggItem;
import mekanism.additions.common.item.ItemBalloon;
import mekanism.additions.common.item.ItemWalkieTalkie;
import mekanism.additions.common.registries.AdditionsBlocks;
import mekanism.additions.common.registries.AdditionsEntityTypes;
import mekanism.additions.common.registries.AdditionsItems;
import mekanism.api.text.EnumColor;
import mekanism.client.ClientRegistrationUtil;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.StrayRenderer;
import net.minecraft.client.renderer.entity.WitherSkeletonRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MekanismAdditions.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AdditionsClientRegistration {

    private AdditionsClientRegistration() {
    }

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        new AdditionsKeyHandler();

        //Register entity rendering handlers
        ClientRegistrationUtil.registerEntityRenderingHandler(AdditionsEntityTypes.OBSIDIAN_TNT, RenderObsidianTNTPrimed::new);
        ClientRegistrationUtil.registerEntityRenderingHandler(AdditionsEntityTypes.BALLOON, RenderBalloon::new);
        ClientRegistrationUtil.registerEntityRenderingHandler(AdditionsEntityTypes.BABY_CREEPER, RenderBabyCreeper::new);
        ClientRegistrationUtil.registerEntityRenderingHandler(AdditionsEntityTypes.BABY_ENDERMAN, RenderBabyEnderman::new);
        ClientRegistrationUtil.registerEntityRenderingHandler(AdditionsEntityTypes.BABY_SKELETON, SkeletonRenderer::new);
        ClientRegistrationUtil.registerEntityRenderingHandler(AdditionsEntityTypes.BABY_STRAY, StrayRenderer::new);
        ClientRegistrationUtil.registerEntityRenderingHandler(AdditionsEntityTypes.BABY_WITHER_SKELETON, WitherSkeletonRenderer::new);

        ClientRegistrationUtil.setRenderLayer(RenderType.translucent(), AdditionsBlocks.TRANSPARENT_PLASTIC_BLOCKS.values());
        ClientRegistrationUtil.setRenderLayer(RenderType.translucent(), AdditionsBlocks.TRANSPARENT_PLASTIC_SLABS.values());
        ClientRegistrationUtil.setRenderLayer(RenderType.translucent(), AdditionsBlocks.TRANSPARENT_PLASTIC_STAIRS.values());
        event.enqueueWork(() -> ClientRegistrationUtil.setPropertyOverride(AdditionsItems.WALKIE_TALKIE, MekanismAdditions.rl("channel"), (stack, world, entity) -> {
            ItemWalkieTalkie item = (ItemWalkieTalkie) stack.getItem();
            return item.getOn(stack) ? item.getChannel(stack) : 0;
        }));
    }

    @SubscribeEvent
    public static void modelRegEvent(ModelRegistryEvent event) {
        AdditionsModelCache.INSTANCE.setup();
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        AdditionsModelCache.INSTANCE.onBake(event);
    }

    @SubscribeEvent
    public static void registerItemColorHandlers(ColorHandlerEvent.Item event) {
        registerBlockColorHandles(event.getBlockColors(), event.getItemColors(), AdditionsBlocks.GLOW_PANELS, AdditionsBlocks.PLASTIC_BLOCKS,
              AdditionsBlocks.SLICK_PLASTIC_BLOCKS, AdditionsBlocks.PLASTIC_GLOW_BLOCKS, AdditionsBlocks.REINFORCED_PLASTIC_BLOCKS, AdditionsBlocks.PLASTIC_ROADS,
              AdditionsBlocks.TRANSPARENT_PLASTIC_BLOCKS, AdditionsBlocks.PLASTIC_STAIRS, AdditionsBlocks.PLASTIC_SLABS, AdditionsBlocks.PLASTIC_FENCES,
              AdditionsBlocks.PLASTIC_FENCE_GATES, AdditionsBlocks.PLASTIC_GLOW_STAIRS, AdditionsBlocks.PLASTIC_GLOW_SLABS, AdditionsBlocks.TRANSPARENT_PLASTIC_STAIRS,
              AdditionsBlocks.TRANSPARENT_PLASTIC_SLABS);
        IItemColor balloonColorHandler = (stack, tintIndex) -> {
            Item item = stack.getItem();
            if (item instanceof ItemBalloon) {
                ItemBalloon balloon = (ItemBalloon) item;
                return MekanismRenderer.getColorARGB(balloon.getColor(), 1);
            }
            return -1;
        };
        for (ItemRegistryObject<ItemBalloon> balloon : AdditionsItems.BALLOONS.values()) {
            ClientRegistrationUtil.registerItemColorHandler(event.getItemColors(), balloonColorHandler, balloon);
        }
        registerSpawnEggColorHandler(event.getItemColors(), AdditionsItems.BABY_CREEPER_SPAWN_EGG, AdditionsItems.BABY_ENDERMAN_SPAWN_EGG,
              AdditionsItems.BABY_SKELETON_SPAWN_EGG, AdditionsItems.BABY_STRAY_SPAWN_EGG, AdditionsItems.BABY_WITHER_SKELETON_SPAWN_EGG);
    }

    @SafeVarargs
    private static void registerBlockColorHandles(BlockColors blockColors, ItemColors itemColors, Map<EnumColor, ? extends BlockRegistryObject<?, ?>>... blocks) {
        for (Map<EnumColor, ? extends BlockRegistryObject<?, ?>> blockMap : blocks) {
            for (BlockRegistryObject<?, ?> block : blockMap.values()) {
                ClientRegistrationUtil.registerIColoredBlockHandler(blockColors, itemColors, block);
            }
        }
    }

    @SafeVarargs
    private static void registerSpawnEggColorHandler(ItemColors colors, ItemRegistryObject<AdditionsSpawnEggItem>... spawnEggs) {
        for (ItemRegistryObject<AdditionsSpawnEggItem> spawnEgg : spawnEggs) {
            ClientRegistrationUtil.registerItemColorHandler(colors, (stack, tintIndex) -> spawnEgg.getItem().getColor(tintIndex), spawnEgg);
        }
    }
}