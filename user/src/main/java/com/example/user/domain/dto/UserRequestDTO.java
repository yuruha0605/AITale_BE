package com.example.user.domain.dto;

import com.example.user.domain.entity.UserEntity;
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
public class UserRequestDTO {

    private String email ; 
    private String password; 
    private int age ;

    // factory method pattern (dto -> entity) : JPA 작업가능
    public UserEntity toEntity() {
        return UserEntity.builder()
                .email(this.email)
                .password(this.password)
                .age(this.age)
                .build() ;
    }
}
