package work.team.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import work.team.client.UserClient;
import work.team.exception.NotFoundException;
import work.team.exception.TeamException;

@Service
@RequiredArgsConstructor
public class UserClientService {

    private final UserClient userClient;

    public void checkUser(final Long id, final String prompt) {
        if (id == null) {
            throw new TeamException(String.format("Значение %s не задано", prompt));
        }
        if (!userClient.existsById(id)) {
            throw new NotFoundException(String.format("Значение %s с id %s не найдено", prompt, id));
        }
    }

    public void checkRoleManager(final Long userId, String prompt){
        if (!userClient.isManager(userId)) {
            throw new TeamException(String.format("%s %s не имеет роли MANAGER", prompt, userId));
        }
    }
}