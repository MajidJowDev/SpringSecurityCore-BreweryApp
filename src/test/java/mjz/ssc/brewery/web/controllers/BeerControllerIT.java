package mjz.ssc.brewery.web.controllers;

import mjz.ssc.brewery.repositories.BeerInventoryRepository;
import mjz.ssc.brewery.repositories.BeerRepository;
import mjz.ssc.brewery.repositories.CustomerRepository;
import mjz.ssc.brewery.services.BeerService;
import mjz.ssc.brewery.services.BreweryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest
public class BeerControllerIT {

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @MockBean
    BeerRepository beerRepository;

    @MockBean
    BeerInventoryRepository beerInventoryRepository;

    @MockBean
    BreweryService breweryService;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    BeerService beerService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    void initCreationForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/beers/new").with(httpBasic("user", "pass")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
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
}
