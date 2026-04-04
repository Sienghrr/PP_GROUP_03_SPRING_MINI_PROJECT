package com.kshrd.habittracker.service.impl;


import com.kshrd.habittracker.model.AppUser;
import com.kshrd.habittracker.repository.AppUserRepository;
import com.kshrd.habittracker.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByEmail(email);

        if(appUser == null) {
            System.out.println("User not found");
        }
        return appUser;
    }
}
