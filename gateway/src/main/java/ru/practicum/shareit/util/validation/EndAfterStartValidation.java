package ru.practicum.shareit.util.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EndAfterStartValidator.class)
        public @interface EndAfterStartValidation {
    String message() default "Время начала не может быть позже время окончания";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
