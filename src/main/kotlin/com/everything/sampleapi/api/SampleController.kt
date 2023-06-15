package com.everything.sampleapi.api

import com.everything.sampleapi.core.Response
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/")
class SampleController {

    @GetMapping("/sample")
    fun getSample(
            @RequestParam(required = false, defaultValue = "10")
            limit: Int
    ) = Response(
            data = true
    )
}