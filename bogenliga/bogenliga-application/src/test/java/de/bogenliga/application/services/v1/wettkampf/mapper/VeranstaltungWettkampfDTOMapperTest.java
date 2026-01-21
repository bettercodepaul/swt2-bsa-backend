package de.bogenliga.application.services.v1.wettkampf.mapper;

import de.bogenliga.application.business.wettkampf.api.types.VeranstaltungWettkampfDO;
import de.bogenliga.application.services.v1.wettkampf.model.VeranstaltungWettkampfDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class VeranstaltungWettkampfDTOMapperTest {
    @Test
    public void TestVeranstaltungWettkampfDTOMapper(){
        VeranstaltungWettkampfDTO expected = new VeranstaltungWettkampfDTO(
                1L,
                "2026-07-15",
                1L,
                "String wettkampfStrasse",
                "String wettkampfPlz",
                "String wettkampfOrtsname",
                "String wettkampfOrtsinfo",
                "String wettkampfBeginn",
                2000L,
                1999L,
                42L,
                69L,
                "String veranstaltungName",
                2026L,
                1002L,
                "ln"
        );
        List<VeranstaltungWettkampfDO> input = new ArrayList<>();
        input.add(new VeranstaltungWettkampfDO(
                1L,
                "2026-07-15",
                1L,
                "String wettkampfStrasse",
                "String wettkampfPlz",
                "String wettkampfOrtsname",
                "String wettkampfOrtsinfo",
                "String wettkampfBeginn",
                2000L,
                1999L,
                42L,
                69L,
                "String veranstaltungName",
                2026L,
                1002L,
                "ln"
        ));

        List<VeranstaltungWettkampfDTO> result = input.stream()
                .map(VeranstaltungWettkampfDTOMapper.toDTO)
                .toList();

        assertEquals(result.get(0).getVeranstaltungLigaName(),expected.getVeranstaltungLigaName());


    }
    }
