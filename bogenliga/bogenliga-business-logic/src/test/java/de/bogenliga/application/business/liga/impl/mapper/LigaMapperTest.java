package de.bogenliga.application.business.liga.impl.mapper;

import org.junit.Test;
import de.bogenliga.application.business.liga.api.types.LigaDO;
import de.bogenliga.application.business.liga.impl.entity.LigaBE;
import de.bogenliga.application.business.liga.impl.entity.LigaBEext;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * I'm testing the LigaMapper Class
 *
 * @author Justin Klein, justin.klein@student.reutlingen-university.de
 */
public class LigaMapperTest {

    private static final Long LIGAID = 1337L;
    private static final String LIGANAME = "Test Liga";
    private static final Long LIGAREGIONID = 10L;
    private static final Long LIGAUEBERGEORDNETID = 42L;
    private static final Long LIGAVERANTWORTLICH = 1L;
    private static final String LIGADETAIL = "Test Detail";
    private static final String LIGAFILEBASE64 = "base64encodedstring";
    private static final String LIGAFILENAME = "file.txt";
    private static final String LIGAFILETYPE = "text/plain";
    private static final String REGIONNAME = "Test Region";
    private static final String UEBERGEORDNETENAME = "Uebergeordnete Liga";
    private static final String VERANTWORTLICHERNAME = "Test Verantwortlicher";
    private static final Long DISZIPLINID = 99L;
    private static final String DISZIPLINNAME = "Disziplin Name";


    @Test
    public void toBE() {
        final LigaDO ligaDO = new LigaDO(
                LIGAID,
                LIGANAME,
                LIGAREGIONID,
                REGIONNAME,
                LIGAUEBERGEORDNETID,
                UEBERGEORDNETENAME,
                LIGAVERANTWORTLICH,
                VERANTWORTLICHERNAME,
                DISZIPLINID,
                LIGADETAIL,
                LIGAFILEBASE64,
                LIGAFILENAME,
                LIGAFILETYPE
        );

        final LigaBE actual = LigaMapper.toLigaBE.apply(ligaDO);

        assertThat(actual).isNotNull();
        assertThat(actual.getLigaId()).isEqualTo(LIGAID);
        assertThat(actual.getLigaName()).isEqualTo(LIGANAME);
        assertThat(actual.getLigaRegionId()).isEqualTo(LIGAREGIONID);
        assertThat(actual.getLigaUebergeordnetId()).isEqualTo(LIGAUEBERGEORDNETID);
        assertThat(actual.getLigaVerantwortlichId()).isEqualTo(LIGAVERANTWORTLICH);
        assertThat(actual.getLigaDetail()).isEqualTo(LIGADETAIL);
        assertThat(actual.getLigaFileBase64()).isEqualTo(LIGAFILEBASE64);
        assertThat(actual.getLigaFileName()).isEqualTo(LIGAFILENAME);
        assertThat(actual.getLigaFileType()).isEqualTo(LIGAFILETYPE);
    }

    @Test
    public void mapToLigaDO() {
        final LigaBEext extendedBE = new LigaBEext();
        extendedBE.setLigaId(LIGAID);
        extendedBE.setLigaName(LIGANAME);
        extendedBE.setLigaRegionId(LIGAREGIONID);
        extendedBE.setRegionName(REGIONNAME);
        extendedBE.setLigaUebergeordnetId(LIGAUEBERGEORDNETID);
        extendedBE.setUebergeordneteLigaName(UEBERGEORDNETENAME);
        extendedBE.setLigaVerantwortlichId(LIGAVERANTWORTLICH);
        extendedBE.setVerantwortlicherName(VERANTWORTLICHERNAME);
        extendedBE.setLigaDisziplinId(DISZIPLINID);
        extendedBE.setDisziplinName(DISZIPLINNAME);
        extendedBE.setLigaDetail(LIGADETAIL);
        extendedBE.setLigaFileBase64(LIGAFILEBASE64);
        extendedBE.setLigaFileName(LIGAFILENAME);
        extendedBE.setLigaFileType(LIGAFILETYPE);

        final LigaDO actual = LigaMapper.mapToLigaDO(extendedBE);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(LIGAID);
        assertThat(actual.getName()).isEqualTo(LIGANAME);
        assertThat(actual.getRegionId()).isEqualTo(LIGAREGIONID);
        assertThat(actual.getRegionName()).isEqualTo(REGIONNAME);
        assertThat(actual.getLigaUebergeordnetId()).isEqualTo(LIGAUEBERGEORDNETID);
        assertThat(actual.getLigaUebergeordnetName()).isEqualTo(UEBERGEORDNETENAME);
        assertThat(actual.getLigaVerantwortlichId()).isEqualTo(LIGAVERANTWORTLICH);
        assertThat(actual.getLigaVerantwortlichMail()).isEqualTo(VERANTWORTLICHERNAME);
        assertThat(actual.getDisziplinId()).isEqualTo(DISZIPLINID);
        assertThat(actual.getLigaDetail()).isEqualTo(LIGADETAIL);
        assertThat(actual.getLigaDoFileBase64()).isEqualTo(LIGAFILEBASE64);
        assertThat(actual.getLigaDoFileName()).isEqualTo(LIGAFILENAME);
        assertThat(actual.getLigaDoFileType()).isEqualTo(LIGAFILETYPE);
    }

    @Test
    public void toLigaDO_withAdditionalData() {
        final LigaBE ligaBE = new LigaBE();
        ligaBE.setLigaId(LIGAID);
        ligaBE.setLigaName(LIGANAME);
        ligaBE.setLigaRegionId(LIGAREGIONID);
        ligaBE.setLigaUebergeordnetId(LIGAUEBERGEORDNETID);
        ligaBE.setLigaVerantwortlichId(LIGAVERANTWORTLICH);
        ligaBE.setLigaDisziplinId(DISZIPLINID);
        ligaBE.setLigaDetail(LIGADETAIL);
        ligaBE.setLigaFileBase64(LIGAFILEBASE64);
        ligaBE.setLigaFileName(LIGAFILENAME);
        ligaBE.setLigaFileType(LIGAFILETYPE);

        final LigaBEext extendedBE = new LigaBEext();
        extendedBE.setDisziplinName(DISZIPLINNAME);
        extendedBE.setRegionName(REGIONNAME);
        extendedBE.setUebergeordneteLigaName(UEBERGEORDNETENAME);
        extendedBE.setVerantwortlicherName(VERANTWORTLICHERNAME);

        final LigaDO actual = LigaMapper.toLigaDO(ligaBE, extendedBE);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(LIGAID);
        assertThat(actual.getName()).isEqualTo(LIGANAME);
        assertThat(actual.getRegionId()).isEqualTo(LIGAREGIONID);
        assertThat(actual.getRegionName()).isEqualTo(REGIONNAME);
        assertThat(actual.getLigaUebergeordnetId()).isEqualTo(LIGAUEBERGEORDNETID);
        assertThat(actual.getLigaUebergeordnetName()).isEqualTo(UEBERGEORDNETENAME);
        assertThat(actual.getLigaVerantwortlichId()).isEqualTo(LIGAVERANTWORTLICH);
        assertThat(actual.getLigaVerantwortlichMail()).isEqualTo(VERANTWORTLICHERNAME);
        assertThat(actual.getDisziplinId()).isEqualTo(DISZIPLINID);
        assertThat(actual.getLigaDetail()).isEqualTo(LIGADETAIL);
        assertThat(actual.getLigaDoFileBase64()).isEqualTo(LIGAFILEBASE64);
        assertThat(actual.getLigaDoFileName()).isEqualTo(LIGAFILENAME);
        assertThat(actual.getLigaDoFileType()).isEqualTo(LIGAFILETYPE);
    }
}