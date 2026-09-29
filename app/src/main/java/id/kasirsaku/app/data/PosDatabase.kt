package id.kasirsaku.app.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.kasirsaku.app.model.CartLine
import id.kasirsaku.app.model.Product
import id.kasirsaku.app.model.SaleSummary
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class PosDatabase(context: Context) : SQLiteOpenHelper(context, "kasir-saku.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE products (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, category TEXT NOT NULL, price INTEGER NOT NULL CHECK(price >= 0), stock INTEGER NOT NULL CHECK(stock >= 0), kind TEXT NOT NULL)")
        db.execSQL("CREATE TABLE sales (id TEXT PRIMARY KEY, timestamp INTEGER NOT NULL, method TEXT NOT NULL, subtotal INTEGER NOT NULL, total INTEGER NOT NULL, cash INTEGER NOT NULL, change_due INTEGER NOT NULL)")
        db.execSQL("CREATE TABLE sale_items (id INTEGER PRIMARY KEY AUTOINCREMENT, sale_id TEXT NOT NULL REFERENCES sales(id), product_id INTEGER NOT NULL, name TEXT NOT NULL, price INTEGER NOT NULL, quantity INTEGER NOT NULL CHECK(quantity > 0))")
        db.execSQL("CREATE INDEX sale_items_sale_id_idx ON sale_items(sale_id)")
        seedProducts(db)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit

    fun products(): List<Product> = readableDatabase.rawQuery(
        "SELECT id,name,category,price,stock,kind FROM products ORDER BY name COLLATE NOCASE", null,
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(Product(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3), cursor.getInt(4), cursor.getString(5)))
            }
        }
    }

    fun addProduct(name: String, category: String, price: Long, stock: Int, kind: String) {
        val values = ContentValues().apply {
            put("name", name.trim())
            put("category", category.trim().ifEmpty { "Umum" })
            put("price", price.coerceAtLeast(0))
            put("stock", if (kind == "Jasa") 0 else stock.coerceAtLeast(0))
            put("kind", kind)
        }
        writableDatabase.insertOrThrow("products", null, values)
    }

    fun todaySales(): List<SaleSummary> {
        val dayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }
        val start = dayStart.timeInMillis
        return readableDatabase.rawQuery(
            "SELECT s.id,s.timestamp,COALESCE(SUM(i.quantity),0),s.method,s.total FROM sales s LEFT JOIN sale_items i ON i.sale_id=s.id WHERE s.timestamp >= ? GROUP BY s.id ORDER BY s.timestamp DESC",
            arrayOf(start.toString()),
        ).use { cursor ->
            buildList {
                while (cursor.moveToNext()) add(SaleSummary(cursor.getString(0), cursor.getLong(1), cursor.getInt(2), cursor.getString(3), cursor.getLong(4)))
            }
        }
    }

    fun finishSale(lines: List<CartLine>, method: String, cash: Long): String {
        require(lines.isNotEmpty())
        val subtotal = lines.sumOf { it.product.price * it.quantity }
        require(method != "Tunai" || cash >= subtotal) { "Uang diterima kurang" }
        val id = "KS-${UUID.randomUUID().toString().take(8).uppercase(Locale.ROOT)}"
        val db = writableDatabase
        db.beginTransaction()
        try {
            lines.forEach { line ->
                if (line.product.kind == "Barang") {
                    db.rawQuery("SELECT stock FROM products WHERE id=?", arrayOf(line.product.id.toString())).use { cursor ->
                        require(cursor.moveToFirst() && cursor.getInt(0) >= line.quantity) { "Stok produk tidak cukup" }
                    }
                    db.execSQL("UPDATE products SET stock=stock-? WHERE id=?", arrayOf<Any>(line.quantity, line.product.id))
                }
            }
            val now = System.currentTimeMillis()
            val change = if (method == "Tunai") cash - subtotal else 0L
            db.insertOrThrow("sales", null, ContentValues().apply {
                put("id", id); put("timestamp", now); put("method", method)
                put("subtotal", subtotal); put("total", subtotal); put("cash", cash); put("change_due", change)
            })
            lines.forEach { line ->
                db.insertOrThrow("sale_items", null, ContentValues().apply {
                    put("sale_id", id); put("product_id", line.product.id); put("name", line.product.name)
                    put("price", line.product.price); put("quantity", line.quantity)
                })
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
        return id
    }

    private fun seedProducts(db: SQLiteDatabase) {
        val products = listOf(
            Seed("Kopi susu gula aren", "Minuman", 18000L, 18),
            Seed("Americano", "Minuman", 14000L, 12),
            Seed("Roti panggang", "Makanan", 16000L, 9),
            Seed("Croissant butter", "Makanan", 22000L, 7),
            Seed("Teh lemon", "Minuman", 13000L, 21),
            Seed("Air mineral", "Minuman", 6000L, 4),
        )
        products.forEach { item ->
            db.insertOrThrow("products", null, ContentValues().apply {
                put("name", item.name); put("category", item.category)
                put("price", item.price); put("stock", item.stock); put("kind", "Barang")
            })
        }
    }

    private data class Seed(val name: String, val category: String, val price: Long, val stock: Int)
}
