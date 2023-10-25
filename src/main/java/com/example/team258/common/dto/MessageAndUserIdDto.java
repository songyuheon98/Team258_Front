package com.example.team258.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class MessageAndUserIdDto {
    private Long userId;
    private String msg;
}
