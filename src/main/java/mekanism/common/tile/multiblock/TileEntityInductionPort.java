package mekanism.common.tile.multiblock;

import javax.annotation.Nonnull;
import mekanism.api.IConfigurable;
import mekanism.common.MekanismLang;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.energy.ProxiedEnergyContainerHolder;
import mekanism.common.content.matrix.MatrixMultiblockData;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tile.base.SubstanceType;
import mekanism.common.util.CableUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.text.BooleanStateDisplay.InputOutput;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;

public class TileEntityInductionPort extends TileEntityInductionCasing implements IConfigurable {

    public TileEntityInductionPort() {
        super(MekanismBlocks.INDUCTION_PORT);
        delaySupplier = () -> 0;
    }

    @Nonnull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers() {
        //Don't allow inserting if we are on output mode, or extracting if we are on input mode
        return ProxiedEnergyContainerHolder.create(side -> !getActive(), side -> getActive(), side -> getMultiblock().getEnergyContainers(side));
    }

    @Override
    protected void onUpdateServer(MatrixMultiblockData multiblock) {
        super.onUpdateServer(multiblock);
        if (multiblock.isFormed() && getActive()) {
            CableUtils.emit(multiblock.getDirectionsToEmit(getBlockPos()), multiblock.getEnergyContainer(), this);
        }
    }

    @Override
    public boolean persists(SubstanceType type) {
        //Do not handle energy when it comes to syncing it/saving this tile to disk
        if (type == SubstanceType.ENERGY) {
            return false;
        }
        return super.persists(type);
    }

    @Override
    public ActionResultType onSneakRightClick(PlayerEntity player, Direction side) {
        if (!isRemote()) {
            boolean oldMode = getActive();
            setActive(!oldMode);
            player.sendMessage(MekanismUtils.logFormat(MekanismLang.INDUCTION_PORT_MODE.translate(InputOutput.of(oldMode, true))), Util.NIL_UUID);
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public int getRedstoneLevel() {
        return getMultiblock().getCurrentRedstoneLevel();
    }

    //Methods relating to IComputerTile
    @ComputerMethod
    private boolean getMode() {
        return getActive();
    }

    @ComputerMethod
    private void setMode(boolean output) {
        setActive(output);
    }
    //End methods IComputerTile
}