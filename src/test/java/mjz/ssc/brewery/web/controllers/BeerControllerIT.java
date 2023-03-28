package mjz.ssc.brewery.web.controllers;

import mjz.ssc.brewery.domain.Beer;
import mjz.ssc.brewery.repositories.BeerInventoryRepository;
import mjz.ssc.brewery.repositories.BeerRepository;
import mjz.ssc.brewery.repositories.CustomerRepository;
import mjz.ssc.brewery.services.BeerService;
import mjz.ssc.brewery.services.BreweryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

//@WebMvcTest // this annotation brings in minimum beans of web context, and can not load the Jpa UserDetails and only brings in an in-memory version, and excludes our implementation, so we need to replace this annotation
@SpringBootTest
public class BeerControllerIT extends BaseIT {

    @Autowired
    BeerRepository beerRepository;

    @DisplayName("Init New Form")
    @Nested
    class InitNewForm{

        @Test
        void initCreationFormAuth() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get("/beers/new").with(httpBasic("springuser", "springpass")))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(view().name("beers/createBeer"))
                    .andExpect(model().attributeExists("beer"));
        }

        @Test
        void initCreationFormNotAuth() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/beers/new"))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }
    }

    @DisplayName("Init Find Beer Form")
    @Nested
    class FindForm{
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("mjz.ssc.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeersFormAUTH(String user, String pwd) throws Exception{
            mockMvc.perform(MockMvcRequestBuilders.get("/beers/find")
                            .with(httpBasic(user, pwd)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(view().name("beers/findBeers"))
                    .andExpect(model().attributeExists("beer"));
        }

        @Test
        void findBeersWithAnonymous() throws Exception{
            mockMvc.perform(MockMvcRequestBuilders.get("/beers/find").with(anonymous()))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }
    }

    @DisplayName("Process Find Beer Form")
    @Nested
    class ProcessFindForm{
        @Test
        void findBeerForm() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/beers").param("beerName", ""))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("mjz.ssc.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void findBeerFormAuth(String user, String pwd) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/beers").param("beerName", "")
                            .with(httpBasic(user, pwd)))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @DisplayName("Get Beer By Id")
    @Nested
    class GetByID {
        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("mjz.ssc.brewery.web.controllers.BeerControllerIT#getStreamAllUsers")
        void getBeerByIdAUTH(String user, String pwd) throws Exception{
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(MockMvcRequestBuilders.get("/beers/" + beer.getId())
                            .with(httpBasic(user, pwd)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(view().name("beers/beerDetails"))
                    .andExpect(model().attributeExists("beer"));
        }

        @Test
        void getBeerByIdNoAuth() throws Exception{
            Beer beer = beerRepository.findAll().get(0);

            mockMvc.perform(MockMvcRequestBuilders.get("/beers/" + beer.getId()))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }
    }

/*
    @DisplayName("Init New Form")
    @Nested
    class InitNewForm {
        @Test
        void initCreationFormAuth() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get("/beers/new").with(httpBasic("springuser", "springpass")))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(view().name("beers/createBeer"))
                    .andExpect(model().attributeExists("beer"));
        }

        @Test
        void initCreationFormNotAuth() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/beers/new"))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        void initCreationForm() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/beers/new").with(httpBasic("user", "pass")))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
                    //.andExpect(view().name("beers/createBeer"))
                    //.andExpect(model().attributeExists("beer"));
        }

        @Test
        void initCreationFormWithSpringUser() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/beers/new").with(httpBasic("springuser", "springpass")))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(view().name("beers/createBeer"))
                    .andExpect(model().attributeExists("beer"));
        }

        @Test
        void initCreationFormWithAli() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/beers/new").with(httpBasic("ali", "test")))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
                   // .andExpect(view().name("beers/createBeer"))
                   // .andExpect(model().attributeExists("beer"));
        }
    }

    //we use this method for testing the security logic
    @WithMockUser("springuser") // we can use any name for the user
    @Test
    void findBeersTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/beers/find"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    //testing both security logic and authentication logic
    @Test
    void findBeersWithHttpBasicTest() throws Exception {
        //testing with http basic authentication
        mockMvc.perform(MockMvcRequestBuilders.get("/beers/find").with(httpBasic("springuser", "springpass"))) // here we have to use the username and password we set in app.properties
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }


    @Test
    void findBeersWithAnonymous() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/beers/find").with(anonymous()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

 */
}
