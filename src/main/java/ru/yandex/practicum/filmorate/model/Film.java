package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;


@Data
@RequiredArgsConstructor
public class Film {
    @EqualsAndHashCode.Exclude
    Long id;

    @NotNull
    @NonNull
    String name;
    @NotNull
    @NonNull
    String description;
    @NotNull
    @NonNull
    LocalDate releaseDate;
    @NotNull
    @NonNull
    Integer duration;
}
