package de.bogenliga.application.business.baseClass.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.swing.text.html.parser.Entity;
import de.bogenliga.application.common.component.entity.BusinessEntity;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO [AL] class documentation
 *
 * @author Kay Scheerer
 */
public class BasicBETest<T extends BusinessEntity> {
    private T expectedBE;


    public void assertList(List<T> list) {
        // assert result
        assertThat(list)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }


    public void testMethod(List<T> bEntities) {
        assertList(bEntities);
        assertEntity(bEntities.get(0));
    }


    public void testMethod(T entity)
    {
        assertEntity(entity);
    }


    public void assertEntity(T entity) {
        assertThat(expectedBE).isNotNull();
        try {
            for (Method method : entity.getClass().getDeclaredMethods()) {
                if (method.getName().contains("get")) {
                    for (Method m : expectedBE.getClass().getDeclaredMethods()) {

                        if (m.getName().equals(method.getName())) {
                            System.out.println(method.getName());
                            System.out.println(method.invoke(entity));
                            System.out.println(m.getName());
                            System.out.println(m.invoke(expectedBE));
                            assertThat(method.invoke(entity)).isEqualTo(m.invoke(expectedBE));
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


    public void setBE(T expectedBE) {
        this.expectedBE = expectedBE;
    }
}
