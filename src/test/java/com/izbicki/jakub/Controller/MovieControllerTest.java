package com.izbicki.jakub.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.izbicki.jakub.Entity.Actor;
import com.izbicki.jakub.Entity.Cast;
import com.izbicki.jakub.Entity.Movie;
import com.izbicki.jakub.MovieType;
import com.izbicki.jakub.Service.ActorService;
import com.izbicki.jakub.Service.CastService;
import com.izbicki.jakub.Service.MovieService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @Mock
    private CastService castService;

    @Mock
    private ActorService actorService;

    @InjectMocks
    private MovieController movieController;

    private MockMvc mvc;

    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(movieController).build();
    }

    @Test
    public void testGetAllMovies() throws Exception {

        List<Movie> movieList = Arrays.asList(
                new Movie("title1", "desc1", MovieType.newest, 10f, true),
                new Movie("title2", "desc2", MovieType.newest, 10f, true),
                new Movie("title3", "desc3", MovieType.newest, 10f, true));

        when(movieService.selectAll()).thenReturn(movieList);

        String uri = "/movies";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        verify(movieService, times(1)).selectAll();

        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
    }

    @Test
    public void testGetMovie() throws Exception {

        Movie movie = new Movie("title1", "desc1", MovieType.newest, 10f, true);
        movie.setId(1L);

        when(movieService.selectMovie(anyLong())).thenReturn(movie);

        String uri = "/movies/1";

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        verify(movieService, times(1)).selectMovie(anyLong());

        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);

        ObjectMapper mapper2 = new ObjectMapper();

        Movie createdMovie = mapper2.readValue(content, Movie.class);

        Assert.assertNotNull("failure - expected entity not null",
                createdMovie);
        Assert.assertNotNull("failure - expected id attribute not null",
                createdMovie.getId());
        Assert.assertEquals("failure - expected text attribute match",
                movie.getTitle(), createdMovie.getTitle());
    }

    @Test
    public void testCreateMovie() throws Exception {

        Movie movie = new Movie("title1", "desc1", MovieType.newest, 10f, true);
        movie.setId(1L);

        when(movieService.insert(anyString(), anyString(), anyObject(), anyFloat())).thenReturn(movie);

        String uri = "/movies/insert";

        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.put(uri)
                        .param("title", "title1")
                        .param("desc", "desc1")
                        .accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        verify(movieService, times(1))
                .insert(anyString(), anyString(), anyObject(), anyFloat());

        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);

        ObjectMapper mapper2 = new ObjectMapper();

        Movie createdMovie = mapper2.readValue(content, Movie.class);

        Assert.assertNotNull("failure - expected entity not null",
                createdMovie);
        Assert.assertNotNull("failure - expected id attribute not null",
                createdMovie.getId());
        Assert.assertEquals("failure - expected text attribute match",
                movie.getTitle(), createdMovie.getTitle());
    }

    @Test
    public void testUpdateMovie() throws Exception {

        Movie movie = new Movie("newTitle", "newDesc", MovieType.newest, 10f, true);
        movie.setId(1L);

        when(movieService.update(anyLong(), anyString(), anyString())).thenReturn(movie);

        String uri = "/movies/update/1";

        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .param("title", "newTitle")
                        .param("desc", "newDesc")
                        .accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        verify(movieService, times(1)).update(anyLong(), anyString(), anyString());

        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);

        ObjectMapper mapper2 = new ObjectMapper();

        Movie createdMovie = mapper2.readValue(content, Movie.class);

        Assert.assertNotNull("failure - expected entity not null",
                createdMovie);
        Assert.assertNotNull("failure - expected id attribute not null",
                createdMovie.getId());
        Assert.assertEquals("failure - expected text attribute match",
                movie.getTitle(), createdMovie.getTitle());
    }

    @Test
    public void testRemoveMovie() throws Exception {

        List<Movie> movieList = Arrays.asList(
                new Movie("title1", "desc1", MovieType.newest, 10f, true),
                new Movie("title2", "desc2", MovieType.newest, 10f, true),
                new Movie("title3", "desc3", MovieType.newest, 10f, true));

        when(movieService.remove(anyLong())).thenReturn(movieList);

        String uri = "/movies/remove/4";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        verify(movieService, times(1)).remove(anyLong());

        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
    }

    @Test
    public void testGetMovieActors() throws Exception {

        List<Actor> actorList = Arrays.asList(
                new Actor("actorName1"),
                new Actor("actorName2"),
                new Actor("actorName3"));

        when(castService.getActorsForMovie(anyLong())).thenReturn(actorList);

        String uri = "/movies/1/actors";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        verify(castService, times(1)).getActorsForMovie(anyLong());

        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
    }

    @Test
    public void testAddActorToMovie() throws Exception {

        Movie movie = new Movie("title1", "desc1", MovieType.newest, 10f, true);
        Actor actor = new Actor("name1");
        Cast cast = new Cast(movie, actor);

        when(castService.insert(anyLong(), anyLong())).thenReturn(cast);

        String uri = "/movies/1/addActor/1";

        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = mvc
                .perform(MockMvcRequestBuilders.put(uri)
                        .param("title", "title1")
                        .param("desc", "desc1")
                        .accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        verify(castService, times(1)).insert(anyLong(), anyLong());

        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);

        ObjectMapper mapper2 = new ObjectMapper();

        Cast createdCast = mapper2.readValue(content, Cast.class);

        Assert.assertNotNull("failure - expected entity not null",
                createdCast);
        Assert.assertEquals("failure - expected text attribute match",
                cast.getMovie().getTitle(), createdCast.getMovie().getTitle());
        Assert.assertEquals("failure - expected text attribute match",
                cast.getActor().getName(), createdCast.getActor().getName());
    }

    @Test
    public void testRemoveActorFromMovie() throws Exception {

        Movie movie = new Movie("title1", "desc1", MovieType.newest, 10f, true);
        Actor actor = new Actor("name1");
        Actor actor2 = new Actor("name2");
        List<Cast> castList = Arrays.asList(new Cast(movie, actor), new Cast(movie, actor2));

        when(castService.remove(anyLong(), anyLong())).thenReturn(castList);

        String uri = "/movies/1/removeActor/1";

        MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        String content = result.getResponse().getContentAsString();
        int status = result.getResponse().getStatus();

        verify(castService, times(1)).remove(anyLong(), anyLong());

        Assert.assertEquals("failure - expected HTTP status 200", 200, status);
        Assert.assertTrue("failure - expected HTTP response body to have a value", content.trim().length() > 0);
    }
}
