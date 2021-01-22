package com.testtask.nauka.api.workers.dto;

import com.testtask.nauka.api.workers.data.Department;
import com.testtask.nauka.common.BaseDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor
@ApiModel("Department")
public class DepartmentDto extends BaseDto<DepartmentDto, Department> {

    @NotEmpty
    private String title;

    public DepartmentDto(Department entity) {
        super(entity);
        this.title = entity.getTitle();
    }

    @Override
    public Department toEntity() {
        return new Department(title);
    }
}
