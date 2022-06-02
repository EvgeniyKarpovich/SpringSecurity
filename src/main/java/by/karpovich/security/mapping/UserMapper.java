package by.karpovich.security.mapping;


import by.karpovich.security.api.dto.UserDto;
import by.karpovich.security.jpa.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfCreation", ignore = true)
    @Mapping(target = "dateOfChange", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "password", ignore = true)
    User mapFromDto(UserDto dto);

    UserDto mapFromEntity(User model);

    List<User> mapFromListDto(List<UserDto> modelList);

    List<UserDto> mapFromListEntity(List<User> dtoList);
}
