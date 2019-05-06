package de.bogenliga.application.business.baseClass.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.assertj.core.api.Assertions;
import de.bogenliga.application.common.component.dao.DataAccessObject;
import de.bogenliga.application.common.errorhandling.exception.BusinessException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public class BasicTest<T> {
    private T expectedEntity;


    public void assertList(List<T> list) {
        // assert result
        assertThat(list)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
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
     * @param entity an business entity of of which all fields with get methods should contain the same value as the
     *               expectedEntity
     */
    public void assertEntity(T entity) {
        assertThat(expectedEntity).isNotNull();
        try {
            for (Method method : entity.getClass().getDeclaredMethods()) {
                if (method.getName().contains("get")) {
                    for (Method m : expectedEntity.getClass().getDeclaredMethods()) {

                        if (m.getName().equals(method.getName())) {
                            System.out.println(method.getName());
                            System.out.println(method.invoke(entity));
                            System.out.println(m.getName());
                            System.out.println(m.invoke(expectedEntity));
                            assertThat(method.invoke(entity)).isEqualTo(m.invoke(expectedEntity));
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


    public void assertException() throws InvocationTargetException, IllegalAccessException {

        for (Method method : expectedEntity.getClass().getDeclaredMethods()) {
            if (method.getName().contains("find")) {
                int count = method.getParameterCount();
                switch (count) {
                    case 0:
                        assertExceptionNull(method);
                    case 1:
                        assertExceptionPerMethod(method,  -1L);
                    case 2:
                        assertExceptionPerMethod(method, -1L, -1L);
                    case 3:
                        assertExceptionPerMethod(method, -1L, -1L, -1L);
                    case 4:
                        assertExceptionPerMethod(method, -1L, -1L, -1L, -1L);
                    case 5:
                        assertExceptionPerMethod(method, -1L, -1L, -1L, -1L, -1L);
                    case 6:
                        assertExceptionPerMethod(method,  -1L, -1L, -1L, -1L, -1L, -1L);
                    case 7:
                        assertExceptionPerMethod(method,  -1L, -1L, -1L, -1L, -1L, -1L, -1L);
                }
            }
        }
    }


    /**
     * @param method
     * @param value
     */
    private void assertExceptionPerMethod(Method method,Long... value) throws InvocationTargetException, IllegalAccessException {
        assertExceptionNegative(method, value);
        assertExceptionNull(method, value);
    }


    /**
     * This tests if exceptions are thrown by component classes
     *
     * @param method the method to be invoked to throw exception
     * @param value  the params which differ for each method
     */
    private void assertExceptionNegative(Method method, Long... value) {
        assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> method.invoke(expectedEntity, value))
                .withMessageContaining("must not be negative")
                .withNoCause();
    }


    /**
     * @param method
     * @param value
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void assertExceptionNull(Method method,
                                    Long... value) throws InvocationTargetException, IllegalAccessException {
        // configure mocks
        when(method.invoke(expectedEntity, value)).thenReturn(null);

        // call test method
        Assertions.assertThatExceptionOfType(BusinessException.class)
                .isThrownBy(() -> method.invoke(expectedEntity, value))
                .withMessageContaining("NOT_FOUND")
                .withNoCause();
    }


    public void setEntity(T expectedE) {
        this.expectedEntity = expectedE;
    }
}
