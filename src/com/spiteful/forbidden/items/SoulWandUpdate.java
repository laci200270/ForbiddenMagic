package com.spiteful.forbidden.items;

import com.spiteful.forbidden.Config;
import com.spiteful.forbidden.Compat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.IWandRodOnUpdate;
import thaumcraft.common.items.wands.ItemWandCasting;
import playerbeacons.common.PlayerBeacons;
import playerbeacons.tileentity.TileEntityPlayerBeacon;

public class SoulWandUpdate implements IWandRodOnUpdate {

	Aspect primals[] = Aspect.getPrimalAspects().toArray(new Aspect[0]);

	public void onUpdate(ItemStack itemstack, EntityPlayer player)
	{
		if(Compat.pb && player.ticksExisted % 150 == 0)
		{
			try
			{
				NBTTagCompound info = null;
				info = PlayerBeacons.beaconData.loadBeaconInformation(player.worldObj, player.username);
				
				if(info == null)
					return;
				
				int x = info.getInteger("x");
				int y = info.getInteger("y");
				int z = info.getInteger("z");
				
				TileEntityPlayerBeacon bacon = (TileEntityPlayerBeacon) player.worldObj.getBlockTileEntity(x, y, z);
				if(bacon != null)
				{
					if(Compat.tt)
					{
						if(player.worldObj.getBlockId(x - 1, y, z) == Compat.tabletID)
							 return;
						if(player.worldObj.getBlockId(x + 1, y, z) == Compat.tabletID)
							return;
						if(player.worldObj.getBlockId(x, y, z + 1) == Compat.tabletID)
							return;
						if(player.worldObj.getBlockId(x, y, z - 1) == Compat.tabletID)
							return;
					}
					int charge = 0;
					for(int j = 0;j < primals.length;j++)
					{
						if(((ItemWandCasting)itemstack.getItem()).getVis(itemstack, primals[j]) < ((ItemWandCasting)itemstack.getItem()).getMaxVis(itemstack))
						{
							((ItemWandCasting)itemstack.getItem()).addVis(itemstack, primals[j], 1, true);
							bacon.setCorruption(bacon.getCorruption() + 50, true);
							bacon.doCorruption(false);
							charge++;
						}
					}
					if(player.worldObj.rand.nextInt(100) < charge)
						bacon.doCorruption(true);
				
				}
			}
			catch(Exception e)
			{
				return;
			}
		}
	}
}