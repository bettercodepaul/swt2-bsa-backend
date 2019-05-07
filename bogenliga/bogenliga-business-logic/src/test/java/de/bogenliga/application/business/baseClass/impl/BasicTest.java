package de.bogenliga.application.business.baseClass.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import de.bogenliga.application.common.component.dao.BasicDAO;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public class BasicTest<T> {
    private T expectedEntity;
    private HashMap<String, Object> valuesToMethodNames;

    /**
     * @param method the method to be invoked
     */
    @Mock
    private Method method;

    public void assertList(List<T> list) {
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
     * Helper method to test a list of entities containing one entitiy on null and empty, and calling assertEntity on
     * the first entity
     *
     * @param bEntities the list of entities
     */
    public void testMethod(List<T> bEntities) {
        assertList(bEntities);
        assertEntity(bEntities.get(0));
    }


    /**
     * Helper method to test an entity on equality to the expected value
     *
     * @param entity
     */
    public void testMethod(T entity) {
        assertEntity(entity);
    }


    /**
     * @param entity a business entity of of which all fields with get methods should contain the same value as the
     *               expectedEntity
     */
    public void assertEntity(T entity) {
        assertThat(expectedEntity).isNotNull();
        try {
            for (Method method : entity.getClass().getDeclaredMethods()) {
                if (method.getName().contains("get")) {
                    for (Method m : expectedEntity.getClass().getDeclaredMethods()) {

                        if (m.getName().equals(method.getName())) {

                            /**asserts that the method from the first entity () is equal to the
                             output of the expected entity (getPasseBE() from PasseBaseDAOTest)
                             is equal to the value set at first (e.g. PasseBaseDAOTest)
                             */
                            assertThat(method.invoke(entity)).isEqualTo(m.invoke(expectedEntity));
                            assertThat(method.invoke(entity)).isEqualTo(valuesToMethodNames.get(m.getName()));
                        }
                    }
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public void setValuesToMethodNames(HashMap<String, Object> valuesToMethodNames) {
        this.valuesToMethodNames = valuesToMethodNames;
    }


    public void setEntity(T expectedE) {
        this.expectedEntity = expectedE;
    }
}
