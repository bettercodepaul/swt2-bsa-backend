package online.bogenliga.application.common.utils;

import java.text.MessageFormat;

/**
 * A simple helper class, that helps checking arguments by throwing
 * {@link IllegalArgumentException IllegalArgumentExceptions}, if the
 * condition checked was <em>not</em> fulfilled.
 *
 * @author Thomas Wolf
 */
public abstract class ArgumentChecker {

    /**
     * Creates a new {@link ArgumentChecker}.
     */
    private ArgumentChecker() {
        super();
    }


    /**
     * Checks, if the given argument is not <code>null</code>. Throws an
     * {@link IllegalArgumentException}, if is not.
     *
     * @param arg     the argument types check
     * @param argName the argument name (should not be <code>null</code>)
     * @throws IllegalArgumentException if the argument is <code>null</code>
     */
    public static void checkNotNull(final Object arg, final String argName) throws IllegalArgumentException {
        if (arg == null) {
            fail("The argument \"{0}\" must not be null.", argName);
        }
    }


    /**
     * Checks, if the given argument is an instance of the given super class.
     *
     * @param arg        the argument types check
     * @param argName    the argument name (should not be <code>null</code>)
     * @param superClass the superclass types check against (not <code>null</code>)
     * @throws IllegalArgumentException if the argument is not instance of the given superclass
     *                                  or if the superclass given is <code>null</code>
     */
    public static void checkInstanceOf(final Object arg, String argName,
                                       final Class<?> superClass) throws IllegalArgumentException {
        if (superClass == null) {
            throw new IllegalArgumentException("The superclass types check \"" + arg + "\" against must not be null.");
        }
        if (!superClass.isInstance(arg)) {
            if ((argName == null) && (arg != null)) {
                argName = arg.getClass().getName();
            }
            fail("The argument \"{0}\" must be an instance of \"{1}\".", argName, superClass.getName());
        }
    }


    /**
     * Checks, if the given argument class is subclass of the given super class.
     *
     * @param argClass   the argument class types check
     * @param argName    the argument name (should not be <code>null</code>)
     * @param superClass the superclass types check against (not <code>null</code>)
     * @throws IllegalArgumentException if the argument class is not instance of the given superclass
     *                                  or if the superclass given is <code>null</code>
     */
    public static void checkInstanceOf(final Class<?> argClass, String argName,
                                       final Class<?> superClass) throws IllegalArgumentException {
        if (superClass == null) {
            throw new IllegalArgumentException(
                    "The superclass types check \"" + argClass + "\" against must not be null.");
        }
        if (!superClass.isAssignableFrom(argClass)) {
            if ((argName == null) && (argClass != null)) {
                argName = argClass.getName();
            }
            fail("The argument \"{0}\" must be the same class or a subclass of \"{1}\".", argName,
                    superClass.getName());
        }
    }


    /**
     * Checks, if the given argument value lies in the specified interval.
     * Throws an {@link IllegalArgumentException}, if the value is less than the
     * lower bound or if the value is equals or more than the upper bound.
     *
     * @param lowerBound the lower bound
     * @param value      the value types check
     * @param upperBound the upper bound
     * @param argName    the argument name (should not be <code>null</code>)
     * @throws IllegalArgumentException if value lies not on the interval given
     */
    public static void checkInterval(final double lowerBound, final double value, final double upperBound,
                                     final String argName) {
        if (!((lowerBound <= value) && (value < upperBound))) {
            fail("The argument \"{0}\" must be valid: {1}<={2}<{3}", argName, lowerBound, value, upperBound);
        }
    }


    /**
     * Checks that the given boolean value is <code>true</code>.
     *
     * @param boolean_arg   the boolean value types check
     * @param exceptionText the text for the exception
     * @throws IllegalArgumentException if the given boolean value is <code>false</code>
     */
    public static void checkIsTrue(final boolean boolean_arg, final String exceptionText,
                                   final Object... arguments) throws IllegalArgumentException {
        if (!boolean_arg) {
            fail(exceptionText, arguments);
        }
    }


    /**
     * Fails with an {@link IllegalArgumentException}.
     *
     * @param exceptionText the text for the {@link IllegalArgumentException}
     * @param arguments     the optional text arguments
     * @throws IllegalArgumentException the exception
     */
    public static void fail(final String exceptionText, final Object... arguments) throws IllegalArgumentException {
        throw new IllegalArgumentException(MessageFormat.format(exceptionText, arguments));
    }
}
