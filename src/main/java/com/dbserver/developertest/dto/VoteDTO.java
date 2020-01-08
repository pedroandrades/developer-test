package com.dbserver.developertest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class VoteDTO {

    private String restaurantName;

    private String hungryProfessionalNickname;
}
