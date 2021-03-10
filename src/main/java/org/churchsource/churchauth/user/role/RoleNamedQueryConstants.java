package org.churchsource.churchauth.user.role;

public class RoleNamedQueryConstants {

  public static final String NAME_FIND_ROLE_BY_ID = "Role.findRoleById";
  public static final String QUERY_FIND_ROLE_BY_ID = "SELECT r FROM Role r WHERE r.id = :id";

  public static final String NAME_FIND_ROLE_BY_ROLENAME = "Role.findRoleByRoleName";
  public static final String QUERY_FIND_ROLE_BY_ROLENAME = "SELECT r FROM Role r WHERE (r.name = :rolename) AND deleted = false";

  public static final String NAME_GET_ALL_ROLES = "Role.GetAllRoles";
  public static final String QUERY_GET_ALL_ROLES = "SELECT r FROM Role r "
          + "WHERE :includeDeleted = TRUE OR r.deleted = false "
          + "ORDER BY name ASC";

    public static final String NAME_GET_ALL_PRIVILEGES = "Privilege.GetAllPrivileges";
    public static final String QUERY_GET_ALL_PRIVILEGES = "SELECT p FROM Privilege p "
            + "WHERE :includeDeleted = TRUE OR p.deleted = false "
            + "ORDER BY name ASC";

}
