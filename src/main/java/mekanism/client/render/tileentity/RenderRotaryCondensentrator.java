package mekanism.client.render.tileentity;

import mekanism.client.model.ModelRotaryCondensentrator;
import mekanism.client.render.GLSMHelper;
import mekanism.common.tile.TileEntityRotaryCondensentrator;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRotaryCondensentrator extends TileEntitySpecialRenderer<TileEntityRotaryCondensentrator> {

    private ModelRotaryCondensentrator model = new ModelRotaryCondensentrator();

    @Override
    public void render(TileEntityRotaryCondensentrator tileEntity, double x, double y, double z, float partialTick, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);
        bindTexture(MekanismUtils.getResource(ResourceType.RENDER, "RotaryCondensentrator.png"));
        GLSMHelper.rotate(tileEntity.facing);
        GlStateManager.rotate(180, 0, 0, 1);
        model.render(0.0625F);
        GlStateManager.popMatrix();
    }
}