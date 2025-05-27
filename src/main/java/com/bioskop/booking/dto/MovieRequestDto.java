package com.bioskop.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class MovieRequestDto {
    @NotBlank(message = "Title wajib diisi")
    private String title;

    @NotBlank(message = "Rating wajib diisi")
    private String rating;

    @NotNull(message = "Duration wajib diisi")
    private Integer duration;

    @NotBlank(message = "Genre wajib diisi")
    private String genre;

    @NotNull(message = "urlImage wajib diisi")
    private MultipartFile urlImage;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public MultipartFile getUrlImage() { return urlImage; }
    public void setUrlImage(MultipartFile urlImage) { this.urlImage = urlImage; }
}
