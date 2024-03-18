package com.example.crud2.decorater;

import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PageResponse<T> {

    Page<T> data;
    Response status;

}

