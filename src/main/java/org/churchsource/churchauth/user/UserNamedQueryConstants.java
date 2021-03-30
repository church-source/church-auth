package org.churchsource.churchauth.user;

public class UserNamedQueryConstants {

  public static final String NAME_GET_ALL_USERS = "User.getAllUsers";
  public static final String QUERY_GET_ALL_USERS = "SELECT u FROM CPUserDetails u "
          + "WHERE :includeDeleted = TRUE OR u.deleted = false "
          + "ORDER BY username ASC";

  public static final String NAME_FIND_USER_BY_ID = "User.findUserById";
  public static final String QUERY_FIND_USER_BY_ID = "SELECT u FROM CPUserDetails u WHERE u.id = :id";

  public static final String NAME_FIND_USER_BY_USERNAME = "User.findUserByUserName";
  public static final String QUERY_FIND_USER_BY_USERNAME = "SELECT u FROM CPUserDetails u WHERE (u.email = :username OR u.username = :username) AND deleted = false";

}
