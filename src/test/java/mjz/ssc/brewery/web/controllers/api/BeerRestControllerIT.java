package mjz.ssc.brewery.web.controllers.api;

import mjz.ssc.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

//@WebMvcTest
@SpringBootTest
public class BeerRestControllerIT extends BaseIT {

    @Test
    void deleteBeerBadCreds() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
                        .header("Api-Key", "springuser").header("Api-Secret", "asdasdasd"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    //testing the filter (not the memory store)
    @Test
    void deleteBeerTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
                .header("Api-Key", "springuser").header("Api-Secret", "springpass"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteBeerHttpBasicTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
                        .with(httpBasic("springuser", "springpass")))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beerUpc/0631234200036"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
