package org.churchsource.churchauth.user.role;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/role")
@Slf4j
public class RoleController {

  @Autowired
  private RoleRepository roleRepository;

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ViewRole')")
  public Role getRole(@PathVariable Long id) {
    return roleRepository.findRoleById(id);
  }

  @GetMapping(params = "name")
  @PreAuthorize("hasAuthority('ViewRole')")
  public Role findRole(@RequestParam String name) {
    return roleRepository.findRoleByRoleName(name);
  }

  @GetMapping
  @PreAuthorize("hasAuthority('ViewRole')")
  public List<Role> getRoles() {
    return roleRepository.getAllRoles();
  }

  @RequestMapping(method = RequestMethod.POST)
  @CrossOrigin
  @PreAuthorize("hasAuthority('AddRole')")
  public Role addRole(@RequestBody Role role) {
    //TODO not using a backing form for now
    return roleRepository.save(role);
  }
}
