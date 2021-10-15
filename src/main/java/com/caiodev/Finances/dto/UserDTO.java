package com.caiodev.Finances.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDTO {

    private String email;
    private String nome;
    private String senha;

}
