package de.photon.anticheataddition.util.datastructure.batch;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import de.photon.anticheataddition.user.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * A thread safe class to save up a certain amount to elements which are then processed by {@link BatchProcessor}s.
 */
public class Batch<T>
{
    @NotNull private final EventBus eventBus;
    @NotNull private final User user;
    @NotNull private final T[] values;

    private int index = 0;
    // Volatile is ok here as we do not change the object itself and only care for the reference.
    @NotNull private T lastAdded;

    public Batch(EventBus eventBus, User user, int capacity, T dummyLastAdded)
    {
        Preconditions.checkArgument(capacity > 0, "Invalid batch size specified.");
        this.eventBus = Preconditions.checkNotNull(eventBus, "Tried to create batch with null EventBus.");
        this.user = Preconditions.checkNotNull(user, "Tried to create batch with null user.");
        this.values = (T[]) new Object[capacity];
        this.lastAdded = Preconditions.checkNotNull(dummyLastAdded, "Tried to create batch with null dummy.");
    }

    /**
     * This will add a datapoint to the {@link Batch}.
     */
    public synchronized void addDataPoint(T value)
    {
        Preconditions.checkNotNull(value, "Tried to add null value to batch");
        this.lastAdded = value;
        this.values[this.index++] = value;

        if (this.index >= this.values.length) {
            eventBus.post(new Snapshot<>(this));
            // Clear the batch.
            this.clear();
        }
    }

    /**
     * This will return the most recently added element.
     * As a {@link Batch} is always initialized with a non-null dummy element, this method will always return a non-null
     * value.
     */
    @NotNull
    public synchronized T peekLastAdded()
    {
        return lastAdded;
    }

    /**
     * Clears the {@link Batch} by setting the write-index to 0.
     * This will make any newly added data-points overwrite the currently present data.
     */
    public synchronized void clear()
    {
        // No synchronization is needed as we only perform one write operation.
        this.index = 0;
    }

    /**
     * Represents a snapshot of a {@link Batch}, e.g. for broadcasting.
     */
    public record Snapshot<T>(@NotNull User user, @NotNull @Unmodifiable List<T> values)
    {
        public Snapshot(User user, List<T> values)
        {
            this.user = user;
            this.values = List.copyOf(values);
        }

        public Snapshot(@NotNull Batch<T> batch)
        {
            this(batch.user, List.of(batch.values));
        }
    }
}