package de.bogenliga.application.business.baseClass.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public class BasicTest<T, B> {
    private T expectedEntity;
    private HashMap<String, Object> valuesToMethodNames;


    public void assertList(List<B> list) {
        // assert result
        assertThat(list)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }


    public BasicTest(T expectedEntity, HashMap<String, Object> valuesToMethodNames) {
        this.expectedEntity = expectedEntity;
        this.valuesToMethodNames = valuesToMethodNames;
    }


    /**
     * Tests all methods of a class which contain "find" in their name invokes a method with just ones as parameters,
     * mock should always return the BE defined with mocks
     * @param component Takes a ComponentImplementation and checks all methods if the returing DO is equal to the expected BE that is returned by the BasicDAO
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void testAllFindMethodOfComponentImpl(Object component) throws InvocationTargetException, IllegalAccessException {
        for (Method m : component.getClass().getDeclaredMethods()) {
            if (m.getName().contains("find")) {
                int count = m.getParameterCount();
                Long[] arr = new Long[count];
                Arrays.fill(arr, 1L);
                Object o = m.invoke(component, arr);
                if (o instanceof List) {
                    testAllFieldsOnEqualToExpectedEntity((List<B>) o);
                } else {
                    testAllFieldsOnEqualToExpectedEntity((B) o);
                }
            }
        }
    }

    /**
     * Helper method to test a list of entities containing one entitiy on null and empty, and calling assertEntity on
     * the first entity
     *
     * @param bEntities the list of entities
     */
    public void testAllFieldsOnEqualToExpectedEntity(List<B> bEntities) throws InvocationTargetException, IllegalAccessException {
        assertList(bEntities);
        assertEntity(bEntities.get(0));
    }


    /**
     * Helper method to test an entity on equality to the expected value
     *
     * @param entity
     */
    public void testAllFieldsOnEqualToExpectedEntity(B entity) throws InvocationTargetException, IllegalAccessException {
        assertEntity(entity);
    }


    /**
     * @param entity a business entity of of which all fields with get methods should contain the same value as the
     *               expectedEntity
     */
    public void assertEntity(B entity) throws InvocationTargetException, IllegalAccessException {
        assertThat(expectedEntity).isNotNull();
        assertThat(entity).isNotNull();
        for (Method method : entity.getClass().getDeclaredMethods()) {
            if (method.getName().contains("get")) {
                for (Method m : expectedEntity.getClass().getDeclaredMethods()) {

                    if (m.getName().equals(method.getName())) {

                        /**asserts that the method from the first entity () is equal to the
                         output of the expected entity (getPasseBE() from PasseBaseDAOTest)
                         is equal to the value set at first (e.g. PasseBaseDAOTest)
                         */
                        Object actual = method.invoke(entity);
                        Object expected = m.invoke(expectedEntity);
                        assertThat(actual).isEqualTo(expected);
                        System.out.println("actual == expected =");
                        System.out.println(actual + " == " + expected + " =" + actual.equals(expected));
                        Object setFirst = valuesToMethodNames.get(m.getName());
                        assertThat(method.invoke(entity)).isEqualTo(setFirst);
                        System.out.println("actual == valuefromHashMap =");
                        System.out.println(actual + " == " + setFirst + " =" + actual.equals(setFirst));
                    }
                }
            }
        }
    }


    public void setValuesToMethodNames(HashMap<String, Object> valuesToMethodNames) {
        this.valuesToMethodNames = valuesToMethodNames;
    }


    public void setEntity(T expectedE) {
        this.expectedEntity = expectedE;
    }
}
