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
        userFullViewModel.setCreated(user.getCreated());
        userFullViewModel.setForcePasswordChange(user.isForcePasswordChange());
        userFullViewModel.setIsEnabled(user.isEnabled());
        userFullViewModel.setIsLocked(!user.isAccountNonLocked());
        userFullViewModel.setIsExpired(!user.isAccountNonExpired());
        return userFullViewModel;
    }

    public CPUserDetails createUserEntity(UserBackingForm userBackingForm) {
        CPUserDetails cpUser = new CPUserDetails();
        if(userBackingForm.getForcePasswordChange() == null) {
           BeanUtils.copyProperties(userBackingForm, cpUser, "deleted", "password", "forcePasswordChange");
        } else {
            BeanUtils.copyProperties(userBackingForm, cpUser, "deleted", "password");
        }
        //TODO check that if field is null that enabled should be true. This may be quite a bug but I assume if the enabled field
        // is not sent in here, it should not automatically disable the user
        if(userBackingForm.getIsEnabled() == null || userBackingForm.getIsEnabled()) {
            cpUser.setEnabled(true);
        } else {
            cpUser.setEnabled(false);
        }
        //only set encode password if it is present
        if(!"".equals(userBackingForm.getPassword()) && userBackingForm.getPassword() != null) {
            cpUser.setPassword(passwordUtils.getEncodedPassword(userBackingForm.getPassword()));
        }
        return cpUser;
    }

}
