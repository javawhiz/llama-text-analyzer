package org.ase.llama_text_analyzer.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Valid
public class TextRequest {

    @NotNull
    @NotEmpty
    @Length(min = 10, max = 2000,
            message = "Please send message length between 10 & 2000 characters")
    private String text;
}
