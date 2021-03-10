package org.churchsource.churchauth.user.role;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/privilege")
@Slf4j
public class PrivilegeController {

  @Autowired
  private RoleRepository roleRepository;

  @GetMapping
  @PreAuthorize("hasAuthority('ViewRole')")
  public List<Privilege> getPrivileges() {
    return roleRepository.getAllPrivileges();
  }

}
