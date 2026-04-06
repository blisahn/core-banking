package com.devblo.admin.query.getAllUsers;

import com.devblo.common.IQuery;
import com.devblo.common.result.Result;
import com.devblo.user.repository.UserSummary;

import java.util.List;

public record GetAllUsersQuery() implements IQuery<Result<List<UserSummary>>> {
}
