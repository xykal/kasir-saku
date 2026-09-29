package id.kasirsaku.app.model

data class Product(
    val id: Long,
    val name: String,
    val category: String,
    val price: Long,
    val stock: Int,
    val kind: String = "Barang",
)

data class CartLine(val product: Product, val quantity: Int)

data class SaleSummary(
    val id: String,
    val timestamp: Long,
    val itemCount: Int,
    val method: String,
    val total: Long,
)
