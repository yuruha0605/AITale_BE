package com.aitale.user.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DifficultyRequestDTO {

    // user id는 URL에서 받으므로 제거
//    private Long userSystemId;
    private String difficulty;

}
