package com.dbserver.developertest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDTO {

    private String restaurantName;

    private String hungryProfessionalNickname;
}
