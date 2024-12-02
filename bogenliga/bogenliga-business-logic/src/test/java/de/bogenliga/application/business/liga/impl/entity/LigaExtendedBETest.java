package de.bogenliga.application.business.liga.impl.entity;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * I'm testing the LigaExtendedBE Class
 *
 * @author Justin Klein, justin.klein@student.reutlingen-university.de
 */
public class LigaExtendedBETest {
    private static final long ID = 1337;
    private static final String LIGANAME = "Test Liga";
    private static final String DISZIPLINNAME = "Disziplin A";
    private static final String REGIONNAME = "Region X";
    private static final String UEBERGEORDNETELIGANAME = "Übergeordnete Liga";
    private static final String VERANTWORTLICHERNAME = "Verantwortlicher Y";

    @Test
    public void assertToString() {
        // Arrange
        final LigaExtendedBE underTest = getLigaExtendedBE();
        underTest.setLigaId(ID);
        underTest.setLigaName(LIGANAME);
        underTest.setDisziplinName(DISZIPLINNAME);
        underTest.setRegionName(REGIONNAME);
        underTest.setUebergeordneteLigaName(UEBERGEORDNETELIGANAME);
        underTest.setVerantwortlicherName(VERANTWORTLICHERNAME);

        // Act
        final String actual = underTest.toString();

        // Assert
        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains(LIGANAME)
                .contains(DISZIPLINNAME)
                .contains(REGIONNAME)
                .contains(UEBERGEORDNETELIGANAME)
                .contains(VERANTWORTLICHERNAME);
    }

    @Test
    public void assertToString_withoutName() {
        // Arrange
        final LigaExtendedBE underTest = getLigaExtendedBE();
        underTest.setLigaId(ID);
        underTest.setLigaName(null);  // LigaName is set to null
        underTest.setDisziplinName(null);  // DisziplinName is set to null
        underTest.setRegionName(null);  // RegionName is set to null
        underTest.setUebergeordneteLigaName(null);  // UebergeordneteLigaName is set to null
        underTest.setVerantwortlicherName(null);  // VerantwortlicherName is set to null

        // Act
        final String actual = underTest.toString();

        // Assert
        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains("null")  // checking that null values are represented as "null" in toString
                .contains("null")
                .contains("null")
                .contains("null")
                .contains("null");
    }

    @Test
    public void assertGetterSetter() {
        // Arrange
        final LigaExtendedBE underTest = new LigaExtendedBE();
        underTest.setDisziplinName(DISZIPLINNAME);
        underTest.setRegionName(REGIONNAME);
        underTest.setUebergeordneteLigaName(UEBERGEORDNETELIGANAME);
        underTest.setVerantwortlicherName(VERANTWORTLICHERNAME);

        // Act & Assert
        assertThat(underTest.getDisziplinName()).isEqualTo(DISZIPLINNAME);
        assertThat(underTest.getRegionName()).isEqualTo(REGIONNAME);
        assertThat(underTest.getUebergeordneteLigaName()).isEqualTo(UEBERGEORDNETELIGANAME);
        assertThat(underTest.getVerantwortlicherName()).isEqualTo(VERANTWORTLICHERNAME);
    }

    private LigaExtendedBE getLigaExtendedBE() {
        return new LigaExtendedBE();
    }

    @Test
    public void assertInheritedFields() {
        // Arrange
        final LigaExtendedBE underTest = new LigaExtendedBE();
        underTest.setLigaId(ID);
        underTest.setLigaName(LIGANAME);

        // Act & Assert
        assertThat(underTest.getLigaId()).isEqualTo(ID);
        assertThat(underTest.getLigaName()).isEqualTo(LIGANAME);
    }

    @Test
    public void assertConstructor_initializesAllFields() {
        // Arrange
        final Long ligaId = 1337L;
        final String ligaName = "Test Liga";
        final Long ligaDisziplinId = 1L;
        final Long ligaRegionId = 2L;
        final Long ligaUebergeordnetId = 3L;
        final Long ligaVerantwortlichId = 4L;
        final String ligaDetail = "Detail info";
        final String ligaFileBase64 = "fileBase64";
        final String ligaFileName = "fileName";
        final String ligaFileType = "fileType";
        final String disziplinName = "Disziplin A";
        final String regionName = "Region X";
        final String uebergeordneteLigaName = "Übergeordnete Liga";
        final String verantwortlicherName = "Verantwortlicher Y";

        // Act
        LigaExtendedBE underTest = new LigaExtendedBE(
                ligaId, ligaName, ligaDisziplinId, ligaRegionId,
                ligaUebergeordnetId, ligaVerantwortlichId, ligaDetail,
                ligaFileBase64, ligaFileName, ligaFileType,
                disziplinName, regionName, uebergeordneteLigaName, verantwortlicherName
        );

        // Assert
        assertThat(underTest.getLigaId()).isEqualTo(ligaId);
        assertThat(underTest.getLigaName()).isEqualTo(ligaName);
        assertThat(underTest.getDisziplinName()).isEqualTo(disziplinName);
        assertThat(underTest.getRegionName()).isEqualTo(regionName);
        assertThat(underTest.getUebergeordneteLigaName()).isEqualTo(uebergeordneteLigaName);
        assertThat(underTest.getVerantwortlicherName()).isEqualTo(verantwortlicherName);
    }

    @Test
    public void assertToString_withNullFields() {
        // Arrange
        final LigaExtendedBE underTest = new LigaExtendedBE();
        underTest.setLigaId(ID);
        underTest.setLigaName(null);
        underTest.setDisziplinName(null);
        underTest.setRegionName(null);
        underTest.setUebergeordneteLigaName(null);
        underTest.setVerantwortlicherName(null);

        // Act
        final String actual = underTest.toString();

        // Assert
        assertThat(actual)
                .isNotEmpty()
                .contains(Long.toString(ID))
                .contains("null")
                .contains("null")
                .contains("null")
                .contains("null")
                .contains("null");
    }


    @Test
    public void assertGetterSetter_withEmptyStrings() {
        // Arrange
        final LigaExtendedBE underTest = new LigaExtendedBE();
        underTest.setDisziplinName("");
        underTest.setRegionName("");
        underTest.setUebergeordneteLigaName("");
        underTest.setVerantwortlicherName("");

        // Act & Assert
        assertThat(underTest.getDisziplinName()).isEmpty();
        assertThat(underTest.getRegionName()).isEmpty();
        assertThat(underTest.getUebergeordneteLigaName()).isEmpty();
        assertThat(underTest.getVerantwortlicherName()).isEmpty();
    }


}
