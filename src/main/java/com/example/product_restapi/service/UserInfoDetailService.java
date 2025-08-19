package com.example.product_restapi.service;
import com.example.product_restapi.entity.UserInfo;
import com.example.product_restapi.entity.UserInfoDetails;
import com.example.product_restapi.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserInfoDetailService implements UserDetailsService {

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = userInfoRepository.findByUsername(username);
        return userInfo.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public String addUser(UserInfo userInfo) {
        //Encode the password before saving
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        //If the user doesn't have any roles set, default to "USER"
        //Ensure the role is saved without the "ROLE_" prefix (e.g., "USER", "ADMIN")
        if (userInfo.getRoles() == null || userInfo.getRoles().isBlank()) {
            userInfo.setRoles("USER");
        }
        userInfoRepository.save(userInfo);
        return "User added to system successfully.";
    }
}
    /*
    public String addUser(UserInfo userInfo){
      userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
      userInfo.setRoles("USER"); //  "ADMIN" or "USER,ADMIN"
      userInfoRepository.save(userInfo);
      return "User added to system successfully.";
    }
     */
