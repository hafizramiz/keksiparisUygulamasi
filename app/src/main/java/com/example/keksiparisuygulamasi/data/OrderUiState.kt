package com.example.keksiparisuygulamasi.data

data class OrderUiState(
    /** Selected cupcake quantity (1, 6, 12) */
    val quantity: Int = 0,
    /** Flavor of the cupcakes in the order (such as "Chocolate", "Vanilla", etc..) */
    val flavor: String = "",
    /** Selected date for pickup (such as "Jan 1") */
    val date: String = "",
    /** Total price for the order */
    val price: String = "Test Price",
    /** Available pickup dates for the order*/
    val pickupOptions: List<String> = listOf()
    )
