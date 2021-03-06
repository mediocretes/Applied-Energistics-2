/* Example:

NBTTagCompound msg = new NBTTagCompound();
NBTTagCompound in = new NBTTagCompound();
NBTTagCompound out = new NBTTagCompound();

new ItemStack( Blocks.iron_ore ).writeToNBT( in );
new ItemStack( Items.iron_ingot ).writeToNBT( out );
msg.setTag( "in", in );
msg.setTag( "out", out );
msg.setInteger( "turns", 8 );

FMLInterModComms.sendMessage( "appliedenergistics2", "add-grindable", msg );

 -- or --
 
NBTTagCompound msg = new NBTTagCompound();
NBTTagCompound in = new NBTTagCompound();
NBTTagCompound out = new NBTTagCompound();
NBTTagCompound optional = new NBTTagCompound();

new ItemStack( Blocks.iron_ore ).writeToNBT( in );
new ItemStack( Items.iron_ingot ).writeToNBT( out );
new ItemStack( Blocks.gravel ).writeToNBT( optional );
msg.setTag( "in", in );
msg.setTag( "out", out );
msg.setTag( "optional", optional );
msg.setFloat( "chance", 0.5 );
msg.setInteger( "turns", 8 );

FMLInterModComms.sendMessage( "appliedenergistics2", "add-grindable", msg );

 */
package appeng.core.api.imc;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import appeng.api.AEApi;
import appeng.core.api.IIMCHandler;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;

public class IMCGrinder implements IIMCHandler
{

	@Override
	public void post(IMCMessage m)
	{
		NBTTagCompound msg = m.getNBTValue();
		NBTTagCompound inTag = (NBTTagCompound) msg.getTag( "in" );
		NBTTagCompound outTag = (NBTTagCompound) msg.getTag( "out" );

		ItemStack in = ItemStack.loadItemStackFromNBT( inTag );
		ItemStack out = ItemStack.loadItemStackFromNBT( outTag );

		int turns = msg.getInteger( "turns" );

		if ( in == null )
			throw new RuntimeException( "invalid input" );

		if ( out == null )
			throw new RuntimeException( "invalid output" );

		if ( msg.hasKey( "optional" ) )
		{
			NBTTagCompound optionalTag = (NBTTagCompound) msg.getTag( "optional" );
			ItemStack optional = ItemStack.loadItemStackFromNBT( optionalTag );

			if ( optional == null )
				throw new RuntimeException( "invalid optional" );

			float chance = msg.getFloat( "chance" );

			AEApi.instance().registries().grinder().addRecipe( in, out, optional, chance, turns );
		}
		else
			AEApi.instance().registries().grinder().addRecipe( in, out, turns );
	}

}
