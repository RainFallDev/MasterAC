package de.photon.anticheataddition.util.datastructure.statistics;

import com.google.common.base.Preconditions;
import de.photon.anticheataddition.util.mathematics.DataUtil;
import de.photon.anticheataddition.util.mathematics.ModularInteger;
import lombok.Getter;

import java.util.Arrays;

/**
 * This class represents a statistic that has a fixed length.
 * The oldest element is overwritten.
 * <p>
 * Removing elements is not supported.
 * This class guarantees O(1) operations for add, sum and average.
 */
public final class MovingDoubleStatistics
{
    private final double[] data;
    private final ModularInteger index;
    @Getter private double sum = 0;
    @Getter private double average = 0;

    public MovingDoubleStatistics(int capacity)
    {
        Preconditions.checkArgument(capacity > 0, "Tried to create MovingDoubleStatistics with 0 or negative capacity.");
        this.data = new double[capacity];
        this.index = new ModularInteger(0, capacity);
    }

    public MovingDoubleStatistics(int capacity, double defaultValue)
    {
        this(capacity);
        Arrays.fill(this.data, defaultValue);
        this.sum = capacity * defaultValue;
        this.average = defaultValue;
    }

    /**
     * This method adds a new datapoint to the floating sum and average.
     */
    public void add(long value)
    {
        final int effIndex = index.getAndIncrement();
        // Remove the overwritten element from the sum
        sum -= data[effIndex];
        // Actually overwrite.
        data[effIndex] = value;
        // Add the new element to the sum.
        sum += value;

        // Update the average.
        average = sum / data.length;
    }

    /**
     * Method to reload the sum and average after floating-point-error accumulation.
     * O(n) runtime.
     */
    public void reloadData()
    {
        this.sum = DataUtil.sum(this.data);
        this.average = sum / data.length;
    }
}
