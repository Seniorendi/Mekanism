package mekanism.common.integration.multipart;

import mcmultipart.api.container.IPartInfo;
import mcmultipart.api.multipart.IMultipart;
import mcmultipart.api.slot.EnumCenterSlot;
import mcmultipart.api.slot.IPartSlot;
import mekanism.common.MekanismBlocks;
import mekanism.common.tile.transmitter.TileEntitySidedPipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class MultipartTransmitter implements IMultipart
{
	@Override
	public IPartSlot getSlotForPlacement(World world, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ, EntityLivingBase placer) 
	{
		return EnumCenterSlot.CENTER;
	}

	@Override
	public IPartSlot getSlotFromWorld(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return EnumCenterSlot.CENTER;
	}
	
	@Override
	public void onAdded(IPartInfo part)
	{
		TileEntity tile = part.getTile().getTileEntity();
		
		if(tile instanceof TileEntitySidedPipe) 
		{
			((TileEntitySidedPipe)tile).onPartChanged(null);
		}
	}
	
	@Override
	public void onPartAdded(IPartInfo part, IPartInfo otherPart)
	{
		TileEntity tile = part.getTile().getTileEntity();
		
		if(tile instanceof TileEntitySidedPipe) 
		{
			tile.validate();
		}
	}
	
	@Override
    public void onPartChanged(IPartInfo part, IPartInfo otherPart)
	{
		TileEntity tile = part.getTile().getTileEntity();
		
        if(tile instanceof TileEntitySidedPipe) 
        {
        	((TileEntitySidedPipe)tile).onPartChanged(otherPart.getPart());
        }
    }
	
	@Override
	public void onPartHarvested(IPartInfo part, EntityPlayer player)
	{
		TileEntity tile = part.getTile().getTileEntity();
		
		if(tile instanceof TileEntitySidedPipe) 
		{
			IBlockState partState = part.getState();
			partState.getBlock().removedByPlayer(partState, part.getPartWorld(),
					part.getContainer().getPartPos(), player, true);
		}
		
		IMultipart.super.onPartHarvested(part, player);
	}
	
	@Override
	public Block getBlock() {
		return MekanismBlocks.Transmitter;
	}
}
