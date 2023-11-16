package com.mintos.accounting.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.stream.Collectors;

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

  //"id.asc|id.desc,type.asc|type.desc"
  private String sort;

  @JsonIgnore
  public Pageable getPageable() {
    if (sort != null && !sort.isEmpty()) {
      return PageRequest.of(offset, limit, getSorting());
    }
    return PageRequest.of(offset, limit, getDefaultSorting());
  }

  @JsonIgnore
  private Sort getDefaultSorting() {
    var order = new Sort.Order(Sort.Direction.DESC, CREATED_DATE);
    return Sort.by(order);
  }

  @JsonIgnore
  private Sort getSorting() {
    String[] sortParts = sort.split(QUERY_PARAMETER_LIST_DELIMITER_REGEX);

    return Arrays.stream(sortParts)
        .map(
            part -> {
              String[] orderParts = part.split(SORT_DIRECTION_DELIMITER_REGEX);
              String property = orderParts[0];
              Sort.Direction direction =
                  orderParts.length > 1 && orderParts[1].equalsIgnoreCase(SORT_ASCENDING_DIRECTION)
                      ? Sort.Direction.ASC
                      : Sort.Direction.DESC;
              return new Sort.Order(direction, property);
            })
        .collect(Collectors.collectingAndThen(Collectors.toList(), Sort::by));
  }
}
