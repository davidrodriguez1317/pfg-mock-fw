package com.dro.pfgmockfw.model.docker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoriesResponseDto {
    private List<String> repositories;
}
