package solutions.skydev.pos.product_service.error.domain;

import solutions.skydev.pos.common.error.domain.ConflictException;

public class CategoryDuplicatedException extends ConflictException {
    public CategoryDuplicatedException() {
        super("Category already exists");
    }
}
