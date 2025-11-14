package com.alexki.schedule.mapper;

import com.alexki.schedule.dto.UserDto;
import com.alexki.schedule.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements BaseMapper<UserDto, User> {
    @Override
    public UserDto toDto(User entity) {
        return new UserDto(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole()
        );
    }

    @Override
    public User toEntity(UserDto dto) {
        return new User(
                dto.id(),
                dto.email(),
                dto.password(),
                dto.role()
        );
    }
}
