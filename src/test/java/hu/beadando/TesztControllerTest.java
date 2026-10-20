package hu.beadando;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TesztController.class)
@Import(NyeresegKalkulator.class)
class TesztControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /teszt – az űrlap betöltése az alapértékekkel")
    void testUrlap() throws Exception {
        mockMvc.perform(get("/teszt"))
                .andExpect(status().isOk())
                .andExpect(view().name("teszt"))
                .andExpect(model().attributeExists("kalkForm", "instrumentumok"))
                .andExpect(model().attribute("kalkForm", hasProperty("instrumentum", is("EUR_USD"))))
                .andExpect(model().attribute("kalkForm", hasProperty("irany", is("long"))))
                .andExpect(model().attribute("kalkForm", hasProperty("egyseg", is(1000))));
    }

    @Test
    @DisplayName("POST /teszt – Long nyereség (EUR_USD, 10000 egység)")
    void testLongNyereseg() throws Exception {
        mockMvc.perform(post("/teszt")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("instrumentum", "EUR_USD")
                        .param("irany", "long")
                        .param("egyseg", "10000")
                        .param("nyitoAr", "1.0850")
                        .param("zaroAr", "1.0925"))
                .andExpect(status().isOk())
                .andExpect(view().name("teszt"))
                .andExpect(model().attribute("eredmeny", is("75.00")))
                .andExpect(model().attribute("nyereseges", is(true)))
                .andExpect(model().attribute("pip", is("75.0")))
                .andExpect(model().attribute("szazalek", is("0.691")))
                .andExpect(model().attribute("deviza", is("USD")));
    }

    @Test
    @DisplayName("POST /teszt – Short veszteség JPY-párral (USD_JPY, 10000 egység)")
    void testShortVesztesegJpy() throws Exception {
        mockMvc.perform(post("/teszt")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("instrumentum", "USD_JPY")
                        .param("irany", "short")
                        .param("egyseg", "10000")
                        .param("nyitoAr", "150.00")
                        .param("zaroAr", "152.50"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("eredmeny", is("-25000.00")))
                .andExpect(model().attribute("nyereseges", is(false)))
                .andExpect(model().attribute("pip", is("250.0")))
                .andExpect(model().attribute("deviza", is("JPY")));
    }

    @Test
    @DisplayName("POST /teszt – 0 egység esetén hibaüzenet, eredmény nélkül")
    void testNullaEgysegHiba() throws Exception {
        mockMvc.perform(post("/teszt")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("instrumentum", "EUR_USD")
                        .param("irany", "long")
                        .param("egyseg", "0")
                        .param("nyitoAr", "1.0850")
                        .param("zaroAr", "1.0925"))
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("eredmeny"))
                .andExpect(model().attribute("error", is("Az egység csak pozitív egész szám lehet!")));
    }

    @Test
    @DisplayName("POST /teszt – határérték: apró veszteség nem jelenik meg -0.00-ként")
    void testNincsNegativNulla() throws Exception {
        mockMvc.perform(post("/teszt")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("instrumentum", "EUR_USD")
                        .param("irany", "long")
                        .param("egyseg", "1")
                        .param("nyitoAr", "0.0000001")
                        .param("zaroAr", "0.000000009"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("eredmeny", is("0.00")))
                .andExpect(model().attribute("nyereseges", is(true)))
                .andExpect(model().attribute("pip", is("0.0")));
    }
}