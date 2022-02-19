package com.it75.anemone.dto;

import com.it75.anemone.entity.User;
import lombok.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private Long id;
    private String name;
    private Set<User> users;
}
