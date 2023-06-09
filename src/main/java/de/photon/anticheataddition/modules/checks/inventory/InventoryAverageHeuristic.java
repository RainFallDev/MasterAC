package de.photon.anticheataddition.modules.checks.inventory;

import de.photon.anticheataddition.modules.ModuleLoader;
import de.photon.anticheataddition.modules.ViolationModule;
import de.photon.anticheataddition.util.violationlevels.ViolationLevelManagement;
import de.photon.anticheataddition.util.violationlevels.ViolationManagement;
import org.bukkit.event.Listener;

public final class InventoryAverageHeuristic extends ViolationModule implements Listener
{
    public static final InventoryAverageHeuristic INSTANCE = new InventoryAverageHeuristic();

    private InventoryAverageHeuristic()
    {
        super("Inventory.parts.AverageHeuristic");
    }

    @Override
    protected ModuleLoader createModuleLoader()
    {
        final var batchProcessor = new AverageHeuristicBatchProcessor(this);
        return ModuleLoader.builder(this)
                           .batchProcessor(batchProcessor)
                           .build();
    }

    @Override
    protected ViolationManagement createViolationManagement()
    {
        return ViolationLevelManagement.builder(this)
                                       .emptyThresholdManagement()
                                       .withDecay(160, 1).build();
    }
}
