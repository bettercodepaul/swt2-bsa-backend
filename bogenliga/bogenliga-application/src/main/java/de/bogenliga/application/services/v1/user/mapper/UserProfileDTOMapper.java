package de.bogenliga.application.services.v1.user.mapper;


import de.bogenliga.application.business.user.api.types.UserProfileDO;
import de.bogenliga.application.common.service.mapping.DataTransferObjectMapper;
import de.bogenliga.application.services.v1.user.model.UserProfileDTO;

import java.util.function.Function;

/**
 * I map the {@link UserProfileDO} and {@link UserProfileDTO} objects
 *
 * @author Yann Philippczyk eXXcellent solutions consulting & software gmbh
 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html">
 * Oracle Function Package Overview</a>
 * @see <a href="https://www.baeldung.com/java-8-functional-interfaces">Functional Interfaces in Java 8</a>
 */
public final class UserProfileDTOMapper implements DataTransferObjectMapper {

    /**
     * I map the {@link UserProfileDTO} object to the {@link UserProfileDO} object
     */
    public static final Function<UserProfileDTO, UserProfileDO> toDO = dto -> {

        UserProfileDO userProfileDO = new UserProfileDO();
        userProfileDO.setId(dto.getId());
        userProfileDO.setEmail(dto.getEmail());
        userProfileDO.setVorname(dto.getVorname());
        userProfileDO.setNachname(dto.getNachname());
        userProfileDO.setGeburtsdatum(dto.getGeburtsdatum());
        userProfileDO.setNationalitaet(dto.getNationalitaet());
        userProfileDO.setMitgliedsnummer(dto.getMitgliedsnummer());
        userProfileDO.setVereinsId(dto.getVereinsId());

        return userProfileDO;
    };

    /**
     * I map the {@link UserProfileDO} object to the {@link UserProfileDTO} object
     */
    public static final Function<UserProfileDO, UserProfileDTO> toDTO = dob -> {

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId(dob.getId());
        userProfileDTO.setEmail(dob.getEmail());
        userProfileDTO.setVorname(dob.getVorname());
        userProfileDTO.setNachname(dob.getNachname());
        userProfileDTO.setGeburtsdatum(dob.getGeburtsdatum());
        userProfileDTO.setNationalitaet(dob.getNationalitaet());
        userProfileDTO.setMitgliedsnummer(dob.getMitgliedsnummer());
        userProfileDTO.setVereinsId(dob.getVereinsId());

        return userProfileDTO;
    };


    /**
     * Constructor
     */
    private UserProfileDTOMapper() {
        // empty private constructor
    }
}
