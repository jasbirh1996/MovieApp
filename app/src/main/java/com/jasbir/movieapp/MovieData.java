package com.jasbir.movieapp;

/**
 * Created by Jasbir Singh..
 */

public class MovieData {
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String movieId;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelaeseData() {
        return relaeseData;
    }

    public void setRelaeseData(String relaeseData) {
        this.relaeseData = relaeseData;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getVote_avarage() {
        return vote_avarage;
    }

    public void setVote_avarage(String vote_avarage) {
        this.vote_avarage = vote_avarage;
    }

    public String getPlot_synopsis() {
        return plot_synopsis;
    }

    public void setPlot_synopsis(String plot_synopsis) {
        this.plot_synopsis = plot_synopsis;
    }

    private String title,relaeseData,poster,vote_avarage,plot_synopsis;

}
