package de.photon.anticheataddition.util.minecraft.tps;

public sealed interface TPSProvider permits ModernTPSProvider
{
    TPSProvider INSTANCE = new ModernTPSProvider();

    /**
     * Gets the current TPS of the server.
     */
    double getTPS();

    /**
     * Checks if the current TPS are higher than min.
     */
    default boolean atLeastTPS(double min)
    {
        return this.getTPS() >= min;
    }
}
