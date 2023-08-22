package com.priyanshub.branchchat.domain.models.message

data class Message(
    val id: Int,
    val thread_id: Int,
    val user_id: Int? = 0,
    val agent_id: Int? = 0,
    val body: String? = null,
    val timestamp: String
)
