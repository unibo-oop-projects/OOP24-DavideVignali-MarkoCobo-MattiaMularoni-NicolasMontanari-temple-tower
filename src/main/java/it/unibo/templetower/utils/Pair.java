package it.unibo.templetower.utils;

/**
 * A standard generic Pair class that holds two values of potentially different types.
 * This class is immutable and final.
 *
 * @param <X> the type of the first element
 * @param <Y> the type of the second element
 */
public final class Pair<X, Y> {

    private final X x;
    private final Y y;
    /**
     * Constructs a new Pair with the given values.
     *
     * @param x the first element
     * @param y the second element
     */
    public Pair(final X x, final Y y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the first element of the pair.
     *
     * @return the first element
     */
    public X getX() {
        return x;
    }

    /**
     * Returns the second element of the pair.
     *
     * @return the second element
     */
    public Y getY() {
        return y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair other = (Pair) obj;
        if (x == null) {
            if (other.x != null) {
                return false;
            }
        } else if (!x.equals(other.x)) {
            return false;
        }
        if (y == null) {
            if (other.y != null) {
                return false;
            }
        } else if (!y.equals(other.y)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pair [x=" + x + ", y=" + y + "]";
    }
}
