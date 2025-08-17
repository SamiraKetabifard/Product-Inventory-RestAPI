package com.example.product_restapi.service;
import com.example.product_restapi.entity.UserInfoDetails;
import com.example.product_restapi.entity.UserInfo;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<UserInfo> userInfo = userInfoRepository.findByUsername(username);
        return userInfo.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
        //return userInfo.map(user -> new UserInfoDetails(user))
        //.orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
    }
    public String addUser(UserInfo userInfo){
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfoRepository.save(userInfo);
        return "User added to system successfully.";
    }
    /*
    public String addUser(UserInfo userInfo){
      userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
      userInfo.setRoles("USER"); //  "ADMIN" or "USER,ADMIN"
      userInfoRepository.save(userInfo);
      return "User added to system successfully.";
    }
     */
}
