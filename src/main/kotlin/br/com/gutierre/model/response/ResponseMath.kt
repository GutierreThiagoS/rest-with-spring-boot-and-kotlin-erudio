package br.com.gutierre.model.response

import br.com.gutierre.model.request.RequestMath

data class ResponseMath(
    val result: Double,
    val request: RequestMath
)
