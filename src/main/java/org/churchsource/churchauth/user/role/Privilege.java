package org.churchsource.churchauth.user.role;

import lombok.*;
import org.churchsource.churchauth.model.ChurchAuthEntity;

import javax.persistence.*;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = RoleNamedQueryConstants.NAME_GET_ALL_PRIVILEGES,
                    query = RoleNamedQueryConstants.QUERY_GET_ALL_PRIVILEGES)
})
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Privilege extends ChurchAuthEntity<Long> {

    private String name;

    @Builder(builderMethodName = "aPrivilege")
    public Privilege(Long id, Date created, Date modified, Boolean deleted, final String name) {
        super(id, created, modified, deleted);
        this.name = name;
    }
}
