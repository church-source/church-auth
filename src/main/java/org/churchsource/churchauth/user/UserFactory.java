package org.churchsource.churchauth.user;

import org.churchsource.churchauth.security.PasswordUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFactory {

    @Autowired
    private PasswordUtils passwordUtils;

    public UserFullViewModel createUserFullViewModelFromEntity(CPUserDetails user) {
        UserFullViewModel userFullViewModel = new UserFullViewModel();
        BeanUtils.copyProperties(user, userFullViewModel, "deleted", "created", "modified");
        //if property names vary then the above copyProperties will not work, so manually setting them below
        userFullViewModel.setIsEnabled(user.isEnabled());
        userFullViewModel.setIsLocked(!user.isAccountNonLocked());
        userFullViewModel.setIsExpired(!user.isAccountNonExpired());
        return userFullViewModel;
    }

    public CPUserDetails createUserEntity(UserBackingForm userBackingForm) {
        CPUserDetails cpUser = new CPUserDetails();
        BeanUtils.copyProperties(userBackingForm, cpUser, "deleted", "password", "forcePasswordChange");
        cpUser.setEnabled(true);
        //only set encode password if it is present
        if(!"".equals(userBackingForm.getPassword()) && userBackingForm.getPassword() != null) {
            cpUser.setPassword(passwordUtils.getEncodedPassword(userBackingForm.getPassword()));
        }
        return cpUser;
    }

}
