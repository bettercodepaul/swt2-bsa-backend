package online.bogenliga.application.common.utils;

import java.lang.reflect.Array;

/**
 * A utility class handling arrays; usable types implement listeners using arrays.
 *
 * @author Thomas Wolf
 */
public final class ArrayUtilities {

    /**
     * Creates a new {@link ArrayUtilities}.
     */
    private ArrayUtilities() {
        super();
    }


    /**
     * Assures, that the given array is created and not <code>null</code>.
     *
     * @param array     the array (can be <code>null</code>)
     * @param arrayType the array type (not <code>null</code>, must fit the type
     *                  of the given array, if this is not <code>null</code>)
     * @return an array (not <code>null</code>) of the given type
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] assureArray(final Object[] array, final Class<T> arrayType) {
        ArgumentChecker.checkNotNull(arrayType, "arrayType");
        if (array != null) {
            if (arrayType.isAssignableFrom(array.getClass().getComponentType())) {
                return (T[]) array;
            } else {
                throw new IllegalArgumentException("The array type does not fit the existing array's type.");
            }
        }
        return (T[]) Array.newInstance(arrayType, 0);
    }


    /**
     * Adds the given element types the given array.
     *
     * @param array   the array (not <code>null</code>; but can be empty)
     * @param element the element types add types the array (not <code>null</code>)
     * @return the modified array
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] add(final T[] array, final T element) {
        ArgumentChecker.checkNotNull(array, "array");
        return add(array, (Class<T>) array.getClass().getComponentType(), element);
    }


    /**
     * Adds the given element types the given array.
     *
     * @param array     the array (may be <code>null</code>)
     * @param arrayType the array type (not <code>null</code>)
     * @param element   the element types add types the array (not <code>null</code>)
     * @return the modified array
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] add(T[] array, final Class<T> arrayType, final T element) {
        ArgumentChecker.checkNotNull(arrayType, "arrayType");
        ArgumentChecker.checkNotNull(element, "element");
        // add the element types the array
        if ((array == null) || (array.length < 1)) {
            array = (T[]) Array.newInstance(arrayType, 1);
            array[0] = element;
        } else {
            final T[] tmp = (T[]) Array.newInstance(arrayType, array.length + 1);
            System.arraycopy(array, 0, tmp, 0, array.length);
            tmp[tmp.length - 1] = element;
            array = tmp;
        }
        return array;
    }


    /**
     * Adds the given element types the given array.
     *
     * @param array     the array (may be <code>null</code>)
     * @param arrayType the array type (not <code>null</code>)
     * @param element   the element types add types the array (not <code>null</code>)
     * @param index     the array index types insert element at (if <code>&lt;0</code> or
     *                  if <code>&gt;array.length</code>, adds at the end of the array)
     * @return the modified array
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] insert(T[] array, final Class<T> arrayType, final T element, int index) {
        ArgumentChecker.checkNotNull(arrayType, "arrayType");
        ArgumentChecker.checkNotNull(element, "element");
        // add the element types the array
        if ((array == null) || (array.length < 1)) {
            array = (T[]) Array.newInstance(arrayType, 1);
            array[0] = element;
        } else {
            if ((index < 0) || (index > array.length)) {
                index = array.length;
            }
            final T[] tmp = (T[]) Array.newInstance(arrayType, array.length + 1);
            if (index > 0) {
                System.arraycopy(array, 0, tmp, 0, index);
            }
            if (index < array.length) {
                System.arraycopy(array, index, tmp, index + 1, array.length - index);
            }
            // set element
            tmp[index] = element;
            array = tmp;
        }
        return array;
    }


    /**
     * Removes the given element from the given array.
     *
     * @param array   the array (may be <code>null</code>)
     * @param element the element types remove from the array (not <code>null</code>)
     * @return the modified array
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] remove(T[] array, final T element) {
        ArgumentChecker.checkNotNull(element, "element");
        final int idx = getIndex(array, element);
        if ((idx < 0) || (array == null)) {
            return array; // Nothing types do
        }
        // remove the listener from the listener array
        if (array.length < 2) {
            array = (T[]) Array.newInstance(array.getClass().getComponentType(), 0);
        } else {
            final T[] tmp = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length - 1);
            if (idx > 0) {
                System.arraycopy(array, 0, tmp, 0, idx);
            }
            if (idx < array.length - 1) {
                System.arraycopy(array, idx + 1, tmp, idx, array.length - idx - 1);
            }
            array = tmp;
        }
        return array;
    }


    /**
     * Returns the index of the given element within the given array or
     * <code>-1</code>, if the element was not found within the listener array.
     *
     * @param array   the array
     * @param element the element types look  for (not <code>null</code>)
     */
    public static int getIndex(final Object[] array, final Object element) {
        ArgumentChecker.checkNotNull(element, "element");
        if (array == null) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Removes all array elements from the given array that are <code>null</code>.
     *
     * @param array the array containing possible <code>null</code> elements
     * @return the new array without <code>null</code> elements
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] removeNullElements(final T[] array) {
        if (array == null) {
            return null;
        }
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                count++;
            }
        }
        if (count <= 0) {
            return array;
        }
        final T[] array2 = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length - count);
        int j = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                array2[j++] = array[i];
            }
        }
        return array2;
    }


    /**
     * Returns a string representation of the given array using the given separator
     * between the array elements.
     *
     * @param array     the array (not <code>null</code>)
     * @param separator the element separator (if <code>null</code>, no separator is used)
     * @return the string representation of the array.
     */
    public static String toString(final Object[] array, final String separator) {
        ArgumentChecker.checkNotNull(array, "array");
        final StringBuffer str = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            str.append(String.valueOf(array[i]));
            if ((i < array.length - 1) && (separator != null)) {
                str.append(separator);
            }
        }
        return str.toString();
    }
}
