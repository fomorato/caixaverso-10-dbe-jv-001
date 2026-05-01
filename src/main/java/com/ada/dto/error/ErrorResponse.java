package com.ada.dto.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse {
    public String title;
    public Integer status;
    public List<ValidationError> violations = new ArrayList<>();
}
