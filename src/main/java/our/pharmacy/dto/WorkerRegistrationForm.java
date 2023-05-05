package our.pharmacy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record WorkerRegistrationForm(
        Long id,
        @Email
        String username,
        @NotEmpty
        @Size(min = 8, message = "password should have at least 8 characters")
        String password,
        @Size(min = 2, message = "Name should have at least 2 characters")
        String name,
        @NotEmpty
        @Size(min = 2, message = "Name should have at least 2 characters")
        String surname,
        @NotNull
        String patronymic,
        @Size(min = 10, max = 10)
        String passport
) {
}
