package org.churchsource.churchauth.user.role;

import lombok.*;
import org.churchsource.churchauth.model.ChurchAuthEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@NamedQueries({
        @NamedQuery(name = RoleNamedQueryConstants.NAME_FIND_ROLE_BY_ID,
                query = RoleNamedQueryConstants.QUERY_FIND_ROLE_BY_ID),
        @NamedQuery(name = RoleNamedQueryConstants.NAME_FIND_ROLE_BY_ROLENAME,
                query = RoleNamedQueryConstants.QUERY_FIND_ROLE_BY_ROLENAME),
        @NamedQuery(name = RoleNamedQueryConstants.NAME_GET_ALL_ROLES,
                query = RoleNamedQueryConstants.QUERY_GET_ALL_ROLES)
})

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Role extends ChurchAuthEntity<Long> {

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "RolePrivilege", joinColumns = @JoinColumn(name = "role", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "privilege", referencedColumnName = "id"))
    private List<Privilege> privileges;

    private String name;

    public Role(final String name) {
        super();
        this.name = name;
    }

    @Builder(builderMethodName = "aRole")
    public Role(Long id, Date created, Date modified, Boolean deleted, final String name, List<Privilege> privileges) {
        super(id, created, modified, deleted);
        this.name = name;
        this.privileges = privileges;
    }

}