package com.devblo.admin.query.getAllUsers;

import com.devblo.common.IQueryHandler;
import com.devblo.common.result.Result;
import com.devblo.user.repository.IUserReadRepository;
import com.devblo.user.repository.UserSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllUsersQueryHandler implements IQueryHandler<GetAllUsersQuery, Result<List<UserSummary>>> {

    private final IUserReadRepository readRepository;

    @Override
    public Result<List<UserSummary>> handle(GetAllUsersQuery query) {
        List<UserSummary> users = readRepository.findAll();
        return Result.success(users);
    }
}
