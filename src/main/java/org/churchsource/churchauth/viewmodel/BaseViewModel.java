package org.churchsource.churchauth.viewmodel;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode
public abstract class BaseViewModel<T> {

  private T id;

}
