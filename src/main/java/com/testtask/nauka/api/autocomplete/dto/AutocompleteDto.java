package com.testtask.nauka.api.autocomplete.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel("Autocomplete")
public class AutocompleteDto<ID> {
    private ID id;
    private String title;

    public AutocompleteDto(ID id, String title) {
        this.id = id;
        this.title = title;
    }
}
