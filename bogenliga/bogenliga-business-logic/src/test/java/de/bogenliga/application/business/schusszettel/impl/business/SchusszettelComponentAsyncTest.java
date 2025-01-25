package de.bogenliga.application.business.schusszettel.impl.business;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import de.bogenliga.application.business.match.api.types.MatchDO;
import de.bogenliga.application.common.errorhandling.ErrorCode;
import de.bogenliga.application.common.errorhandling.exception.TechnicalException;

import static org.mockito.Mockito.*;

public class SchusszettelComponentAsyncTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private SchusszettelComponentImpl schusszettelComponentImpl;

    @InjectMocks
    private SchusszettelComponentAsync underTest;

    @Test
    public void generateSchusszettelPageAsync_shouldThrowTechnicalException() throws Exception {
        // Arrange
        MatchDO[] matchesBegegnung = new MatchDO[2];
        matchesBegegnung[0] = new MatchDO();
        matchesBegegnung[1] = new MatchDO();

        doThrow(new TechnicalException(ErrorCode.INTERNAL_ERROR, "Test Exception")).when(schusszettelComponentImpl).generateSchusszettelPage(any(), eq(matchesBegegnung));

        // Act & Assert
        CompletableFuture<ByteArrayOutputStream> future = underTest.generateSchusszettelPageAsync(matchesBegegnung, 1, 1, 7, 8);
        thrown.expect(ExecutionException.class);
        thrown.expectCause(org.hamcrest.Matchers.instanceOf(TechnicalException.class));
        future.get();
    }
}
