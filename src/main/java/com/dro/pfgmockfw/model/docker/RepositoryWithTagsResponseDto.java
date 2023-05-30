package com.dro.pfgmockfw.model.docker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryWithTagsResponseDto {
    private String name;
    private List<String> tags;
}
