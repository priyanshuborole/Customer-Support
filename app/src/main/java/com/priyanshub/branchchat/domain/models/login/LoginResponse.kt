package com.priyanshub.branchchat.domain.models.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("auth_token") val authToken: String
)
