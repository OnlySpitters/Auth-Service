package com.service.auth.model.privilege;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.service.auth.model.common.BaseModel;
import com.service.auth.model.role.Role;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Privilege extends BaseModel {

    @NotEmpty
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    @JsonIgnore
    private List<Role> roles;
}
