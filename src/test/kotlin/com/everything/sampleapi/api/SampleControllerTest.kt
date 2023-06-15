package com.everything.sampleapi.api

import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@SpringBootTest
class SampleControllerTest {
    protected lateinit var mvc: MockMvc

    protected lateinit var mockMvcBuilder: DefaultMockMvcBuilder

    @Autowired
    protected val context: WebApplicationContext? = null

    @BeforeEach
    fun setUp() {
        mockMvcBuilder = MockMvcBuilders.webAppContextSetup(context!!)
                .addFilter(CharacterEncodingFilter("UTF-8", true))

        mvc = mockMvcBuilder.build()
    }

    @Test
    fun `sample 조회 true `() {
        mvc.perform(MockMvcRequestBuilders
                .get("/sample")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
                .andExpect(MockMvcResultMatchers.jsonPath("$.data", Matchers.`is`(true)))
    }
}