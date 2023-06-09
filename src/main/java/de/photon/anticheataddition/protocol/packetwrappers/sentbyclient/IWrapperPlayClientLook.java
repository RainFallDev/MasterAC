package de.photon.anticheataddition.protocol.packetwrappers.sentbyclient;

import de.photon.anticheataddition.protocol.packetwrappers.IWrapperPlayLook;

public interface IWrapperPlayClientLook extends IWrapperPlayLook
{
    @Override
    default float getYaw()
    {
        return this.getHandle().getFloat().read(0);
    }

    @Override
    default void setYaw(final float value)
    {
        this.getHandle().getFloat().write(0, value);
    }

    @Override
    default float getPitch()
    {
        return this.getHandle().getFloat().read(1);
    }

    @Override
    default void setPitch(final float value)
    {
        this.getHandle().getFloat().write(1, value);
    }
}
