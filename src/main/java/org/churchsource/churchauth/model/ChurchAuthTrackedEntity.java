package org.churchsource.churchauth.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@MappedSuperclass
@ToString
public class ChurchAuthTrackedEntity implements Serializable {

  private static final long serialVersionUID = 6867167030691894233L;

  @JsonIgnore
  @CreationTimestamp
  private Date created;

  @JsonIgnore
  @UpdateTimestamp
  private Date modified;

  @JsonIgnore
  @Column(nullable = false)
  private Boolean deleted = Boolean.FALSE;
}