package tk.empee.gameManager.helpers;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Join array by reference
 */
public final class ArrayJoiner<T> {

    private final Collection<T> array;
    private final ArrayJoiner<T> joiner;

    public ArrayJoiner(Collection<T> array) {
        this(array, null);
    }
    public ArrayJoiner(Collection<T> array, ArrayJoiner<T> joiner) {

        this.array = array;
        this.joiner = joiner;

    }

    public void foreach(Consumer<T> consumer) {
        array.forEach(consumer);
        if(joiner != null) {
            joiner.foreach(consumer);
        }
    }

}
