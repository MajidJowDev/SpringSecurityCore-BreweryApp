package mjz.ssc.brewery.web.controllers.api;

import mjz.ssc.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
public class BeerRestControllerIT extends BaseIT {

    //testing the filter (not the memory store)
    @Test
    void deleteBeerTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
                .header("Api-Key", "springuser").header("Api-Secret", "springpass"))
                .andExpect(MockMvcResultMatchers.status().isOk());
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
