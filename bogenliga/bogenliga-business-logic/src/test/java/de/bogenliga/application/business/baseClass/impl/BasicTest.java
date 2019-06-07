package de.bogenliga.application.business.baseClass.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Basic Test class to test to entities on equality by checking every get-Method on same return value
 *
 * @author Kay Scheerer
 */
public class BasicTest<T, B> {
    private T expectedEntity;
    private HashMap<String, Object> valuesToMethodNames;
    private static Logger LOG;


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
        LoggerFactory.getLogger(expectedEntity.getClass());
        LOG = LoggerFactory.getLogger(expectedEntity.getClass());
    }


    /**
     * Tests all methods of a class which contain "find" in their name invokes a method with just ones as parameters,
     * mock should always return the BE defined with mocks
     *
     * @param component Takes a ComponentImplementation or DAO and checks all methods which contain "find" whether the
     *                  returned DO is equal to the expected BE that is returned by the BasicDAO
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void testAllFindMethods(
            Object component) throws InvocationTargetException, IllegalAccessException {
        for (Method m : component.getClass().getDeclaredMethods()) {
            if (m.getName().contains("find")) {
                int count = m.getParameterCount();
                Long[] arr = new Long[count];
                Arrays.fill(arr, 1L);
                Object o = m.invoke(component, arr);
                if (o instanceof List) {
                    testAllFieldsOnEqualToExpectedEntity((List<B>) o);
                } else if (((B) o) != null) {
                    testAllFieldsOnEqualToExpectedEntity((B) o);
                } else {
                    LOG.debug("BasicDAO didn't return an Object of the second generic Type defined," +
                            "check if you have the mock configured to return on singleEntity and selectEntityList");
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
    public void testAllFieldsOnEqualToExpectedEntity(
            List<B> bEntities) throws InvocationTargetException, IllegalAccessException {
        assertList(bEntities);
        assertEntity(bEntities.get(0));
    }


    /**
     * Helper method to test an entity on equality to the expected value
     *
     * @param entity
     */
    public void testAllFieldsOnEqualToExpectedEntity(
            B entity) throws InvocationTargetException, IllegalAccessException {
        assertEntity(entity);
    }


    /**
     * @param entity a business entity of of which all fields with get methods should contain the same value as the
     *               expectedEntity
     */
    public void assertEntity(B entity) throws InvocationTargetException, IllegalAccessException {
        assertThat(expectedEntity).isNotNull();
        assertThat(entity).isNotNull();
        LOG.debug("Testing class (actual entity given by method parameter): " + entity.getClass().getSimpleName());
        LOG.debug("Testing class (expected entity to be compared given by constructor): " + expectedEntity.getClass().getSimpleName());
        for (Method method : entity.getClass().getDeclaredMethods()) {
            ArrayList<Method> arr = new ArrayList<>(Arrays.asList(expectedEntity.getClass().getDeclaredMethods()));
            String mName = method.getName();
            if (mName.contains("get")) {
                LOG.debug("Method being tested: " + mName);
                Optional<Method> optionalM = arr.stream()
                        .filter((x) -> x.getName().equals(mName))
                        .findFirst();
                if (!optionalM.isPresent()) {
                    LOG.debug("Expected entity doesn't implement the method: " + mName);
                }
                assertThat(optionalM.isPresent()).isTrue();

                if (!valuesToMethodNames.containsKey(mName)) {
                    LOG.debug(
                            "Method is not given in the Hashmap, please insert the name of the method: " + method.getName() + " in the Hashmap given to" +
                                    "BasicTest class constructor");
                    assertThat(valuesToMethodNames.containsKey(mName)).isTrue();
                }
                Method m = optionalM.get();

                /**asserts that the method from the first entity () is equal to the
                 output of the expected entity (getPasseBE() from PasseBaseDAOTest)
                 is equal to the value set at first (e.g. PasseBaseDAOTest)
                 */
                Object actual = method.invoke(entity);
                Object expected = m.invoke(expectedEntity);
                assertThat(actual).isEqualTo(expected);
                Object setFirst = valuesToMethodNames.get(mName);

                assertThat(method.invoke(entity)).isEqualTo(setFirst);
                if (actual == null) {
                    LOG.debug("The parameter returned by getter : " + m.getName() + " is null. ");
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
