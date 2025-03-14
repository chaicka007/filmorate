package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class User {
    @EqualsAndHashCode.Exclude
    Integer id;
    @NotNull
    @NonNull
    @NotBlank
    @EqualsAndHashCode.Exclude
    String login;
    @EqualsAndHashCode.Exclude
    String name;
    @NotNull
    @NonNull
    @EqualsAndHashCode.Exclude
    LocalDate birthday;

    @Email
    @NotNull
    @NonNull
    @NotBlank
    String email;
}
