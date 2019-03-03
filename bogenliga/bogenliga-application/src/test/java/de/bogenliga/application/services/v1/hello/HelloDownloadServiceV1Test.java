package de.bogenliga.application.services.v1.hello;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

/**
 * @author Andre Lehnert, eXXcellent solutions consulting & software gmbh
 */
@SuppressWarnings({"pmd-unit-tests:JUnitTestsShouldIncludeAssert", "squid:S2187"})
public class HelloDownloadServiceV1Test {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @InjectMocks
    private HelloDownloadServiceV1 underTest;


    @Test
    public void downloadPdf() {
        final ResponseEntity<InputStreamResource> actual = underTest.downloadPdf();
        Assertions.assertThat(actual).isNotNull();
    }


    @Test
    public void downloadWord() {
        final ResponseEntity<InputStreamResource> actual = underTest.downloadWord();
        Assertions.assertThat(actual).isNotNull();
    }


    @Test
    public void downloadExcel() {
        final ResponseEntity<InputStreamResource> actual = underTest.downloadExcel();
        Assertions.assertThat(actual).isNotNull();
    }


    @Test
    public void downloadCSV() {
        final ResponseEntity<InputStreamResource> actual = underTest.downloadCSV();
        Assertions.assertThat(actual).isNotNull();
    }


    @Test
    public void downloadMDB() {
        final ResponseEntity<InputStreamResource> actual = underTest.downloadMDB();
        Assertions.assertThat(actual).isNotNull();
    }
}