package com.dro.pfgmockfw.model.docker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoriesResponseDto {
    private List<String> repositories;
}
