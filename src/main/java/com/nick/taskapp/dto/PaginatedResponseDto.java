package com.nick.taskapp.dto;

import java.util.List;
/*
 * Generic DTO for paginated API responses.
 *
 * Wraps a list of data along with pagination metadata
 * such as current page, size, and total counts.
 */
public class PaginatedResponseDto<T> {

    private List<T> data;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public PaginatedResponseDto() {}

    public PaginatedResponseDto(List<T> data, int page, int size, long totalElements, int totalPages) {
        this.data = data;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<T> getData() {
        return data;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }
}


/*
This DTO defines the structure of a paginated API response.
Instead of returning just a list of tasks, it returns:
the data (list of items)
pagination info (page, size, total count, etc.)


Pagination DTO
Encapsulates:
actual data
metadata about that data

Generics
PaginatedResponseDto<T>
Allows reuse for any type:
tasks
users
anything else later

Data + Metadata Separation
Separates:
data → actual results
metadata → info about the results
*/