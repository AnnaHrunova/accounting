package com.mintos.accounting.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
public class FilteredPageRequest {
    private static final String CREATED_DATE = "createdDate";
    private static final String QUERY_PARAMETER_LIST_DELIMITER_REGEX = ",";
    private static final String SORT_DIRECTION_DELIMITER_REGEX = "\\.";
    private static final String SORT_ASCENDING_DIRECTION = "asc";

    @Min(0)
    private int offset;

    @Min(1)
    private int limit;

    @JsonIgnore
    public Pageable getPageable() {
        return PageRequest.of(offset, limit, getDefaultSorting());
    }

    @JsonIgnore
    private Sort getDefaultSorting() {
        var order = new Sort.Order(Sort.Direction.DESC, CREATED_DATE);
        return Sort.by(order);
    }
}
