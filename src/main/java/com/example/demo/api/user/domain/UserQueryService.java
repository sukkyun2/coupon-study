package com.example.demo.api.user.domain;

import com.example.demo.api.common.app.NoDataException;
import com.example.demo.api.user.domain.User;
import com.example.demo.api.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository userRepository;

    public User getUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(NoDataException::new);
    }
}
