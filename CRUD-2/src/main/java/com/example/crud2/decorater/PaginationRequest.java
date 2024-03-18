package com.example.crud2.decorater;


import lombok.*;

@Data
public class PaginationRequest
{
    private PaginationDecorater pagination;
    private FilterDecorater filter;
    private SortDecoraters sort;

}
