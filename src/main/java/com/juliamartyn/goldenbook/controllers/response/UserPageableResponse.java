package com.juliamartyn.goldenbook.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPageableResponse {
    private List<UserResponse> users;
    private long totalItems;
    private int totalPages;
    private int currentPage;
}
