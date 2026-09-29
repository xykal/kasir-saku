package id.kasirsaku.app.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.kasirsaku.app.model.CartLine
import id.kasirsaku.app.model.Product
import id.kasirsaku.app.model.SaleSummary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun price(value: Long) = "Rp" + value.toString().reversed().chunked(3).joinToString(".").reversed()

@Composable
fun SummaryStrip(total: Long, count: Int, low: Int) {
    Row(Modifier.fillMaxWidth().padding(bottom = 14.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SummaryCard("Penjualan hari ini", price(total), Modifier.weight(1f))
        SummaryCard("Transaksi", count.toString(), Modifier.weight(1f))
        SummaryCard("Stok menipis", low.toString(), Modifier.weight(1f))
    }
}

@Composable
private fun SummaryCard(label: String, value: String, modifier: Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text(label, color = MaterialTheme.colorScheme.secondary, fontSize = 11.sp, maxLines = 2, minLines = 2)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 17.sp, maxLines = 1, softWrap = false)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Catalog(products: List<Product>, query: String, onQuery: (String) -> Unit, category: String, onCategory: (String) -> Unit, onAdd: (Product, Int) -> Unit, modifier: Modifier = Modifier) {
    val categories = listOf("Semua") + products.map { it.category }.distinct()
    val visible = products.filter { (category == "Semua" || it.category == category) && it.name.contains(query, true) }
    Column(modifier) {
        OutlinedTextField(query, onQuery, Modifier.fillMaxWidth(), label = { Text("Cari produk") }, singleLine = true, shape = RoundedCornerShape(13.dp))
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(vertical = 7.dp), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            categories.forEach { item -> FilterChip(selected = item == category, onClick = { onCategory(item) }, label = { Text(item) }) }
        }
        if (visible.isEmpty()) Text("Produk tidak ditemukan.", Modifier.padding(16.dp), color = MaterialTheme.colorScheme.secondary)
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val columns = if (maxWidth >= 600.dp) 3 else 2
            val gap = 8.dp
            val cardWidth = (maxWidth - gap * (columns - 1)) / columns
            FlowRow(maxItemsInEachRow = columns, horizontalArrangement = Arrangement.spacedBy(gap), verticalArrangement = Arrangement.spacedBy(gap)) {
                visible.forEach { product -> ProductCard(product, cardWidth, { onAdd(product, 1) }) }
            }
        }
    }
}

@Composable
private fun ProductCard(product: Product, width: androidx.compose.ui.unit.Dp, onAdd: () -> Unit) {
    Card(onClick = onAdd, modifier = Modifier.width(width), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.padding(10.dp)) {
            val art = when {
                product.name.contains("lemon", true) -> "lemon"
                product.name.contains("air", true) || product.name.contains("water", true) -> "water"
                product.name.contains("roti", true) || product.name.contains("croissant", true) -> "bread"
                product.name.contains("brownie", true) || product.name.contains("cake", true) -> "dessert"
                product.category.equals("Minuman", true) -> "drink"
                else -> "box"
            }
            val tint = when (art) { "lemon" -> Color(0xFFF0EFCF); "bread", "dessert" -> Color(0xFFF1E8D9); "water" -> Color(0xFFE3ECEB); else -> Color(0xFFE9EEE6) }
            Surface(Modifier.fillMaxWidth().height(70.dp), color = tint, shape = RoundedCornerShape(12.dp)) {
                androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) { LineIcon(art, Modifier.size(34.dp)) }
            }
            Spacer(Modifier.height(8.dp))
            Text(product.name, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(if (product.kind == "Jasa") "Jasa" else "Stok ${product.stock}", color = MaterialTheme.colorScheme.secondary, fontSize = 11.sp)
            Row(Modifier.fillMaxWidth().padding(top = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(price(product.price), Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Button(onClick = onAdd, modifier = Modifier.size(48.dp), contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp), shape = RoundedCornerShape(12.dp)) { Text("+", fontSize = 21.sp) }
            }
        }
    }
}

@Composable
fun CartPanel(cart: List<CartLine>, onChange: (Product, Int) -> Unit, onCheckout: () -> Unit, modifier: Modifier = Modifier) {
    val total = cart.sumOf { it.product.price * it.quantity }
    Column(modifier) {
        Text("Keranjang", fontSize = 19.sp, fontWeight = FontWeight.Bold)
        Text("${cart.sumOf { it.quantity }} item dipilih", color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
        Spacer(Modifier.height(8.dp))
        if (cart.isEmpty()) Text("Pilih produk untuk mulai transaksi.", Modifier.padding(vertical = 18.dp), color = MaterialTheme.colorScheme.secondary)
        cart.forEach { line ->
            Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(line.product.name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(price(line.product.price * line.quantity), fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                }
                Button(onClick = { onChange(line.product, -1) }, modifier = Modifier.size(48.dp), contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)) { Text("−", fontSize = 18.sp) }
                Text("${line.quantity}", Modifier.width(28.dp), fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                Button(onClick = { onChange(line.product, 1) }, modifier = Modifier.size(48.dp), contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)) { Text("+", fontSize = 18.sp) }
            }
        }
        Spacer(Modifier.weight(1f, fill = false))
        Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total", fontWeight = FontWeight.Medium)
            Text(price(total), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Button(onClick = onCheckout, enabled = cart.isNotEmpty(), modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp)) { Text("Lanjut pembayaran") }
    }
}

@Composable
fun ProductList(products: List<Product>, modifier: Modifier = Modifier) {
    Column(modifier.verticalScroll(rememberScrollState())) {
        Text("Barang dan jasa tersimpan di perangkat ini.", color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp, modifier = Modifier.padding(bottom = 10.dp))
        products.forEach { product ->
            Card(Modifier.fillMaxWidth().padding(bottom = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(product.name, fontWeight = FontWeight.SemiBold)
                        Text("${product.category} · ${product.kind}", color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(price(product.price), fontWeight = FontWeight.Bold)
                        Text(if (product.kind == "Jasa") "—" else "Stok ${product.stock}", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SalesList(sales: List<SaleSummary>, modifier: Modifier = Modifier) {
    LazyColumn(modifier) {
        if (sales.isEmpty()) item { Text("Belum ada transaksi hari ini.", Modifier.padding(16.dp), color = MaterialTheme.colorScheme.secondary) }
        items(sales, key = { it.id }) { sale ->
            Card(Modifier.fillMaxWidth().padding(bottom = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(sale.id, fontWeight = FontWeight.SemiBold)
                        Text("${sale.itemCount} item · ${sale.method} · ${SimpleDateFormat("HH:mm", Locale("id", "ID")).format(Date(sale.timestamp))}", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                    }
                    Text(price(sale.total), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ReportPanel(total: Long, sales: List<SaleSummary>, modifier: Modifier = Modifier) {
    Column(modifier) {
        SummaryCard("Penjualan hari ini", price(total), Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        SummaryCard("Jumlah transaksi", sales.size.toString(), Modifier.fillMaxWidth())
        Spacer(Modifier.height(10.dp))
        SummaryCard("Rata-rata transaksi", price(if (sales.isEmpty()) 0 else total / sales.size), Modifier.fillMaxWidth())
    }
}

@Composable
fun AddProductDialog(onDismiss: () -> Unit, onSave: (String, String, Long, Int, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("Umum") }
    var priceText by remember { mutableStateOf("") }
    var stockText by remember { mutableStateOf("0") }
    var kind by remember { mutableStateOf("Barang") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah produk") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("Nama") }, singleLine = true)
                OutlinedTextField(group, { group = it }, Modifier.fillMaxWidth(), label = { Text("Kategori") }, singleLine = true)
                OutlinedTextField(priceText, { priceText = it.filter(Char::isDigit) }, Modifier.fillMaxWidth(), label = { Text("Harga (Rp)") }, singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Barang", "Jasa").forEach { value -> FilterChip(kind == value, { kind = value }, label = { Text(value) }) }
                }
                if (kind == "Barang") OutlinedTextField(stockText, { stockText = it.filter(Char::isDigit) }, Modifier.fillMaxWidth(), label = { Text("Stok awal") }, singleLine = true)
            }
        },
        confirmButton = { TextButton(onClick = { if (name.isNotBlank() && priceText.isNotBlank()) onSave(name, group, priceText.toLongOrNull() ?: 0L, stockText.toIntOrNull() ?: 0, kind) }) { Text("Simpan") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } },
    )
}

@Composable
fun CheckoutDialog(cart: List<CartLine>, total: Long, onDismiss: () -> Unit, onConfirm: (String, Long) -> Unit) {
    var method by remember { mutableStateOf("Tunai") }
    var cash by remember { mutableStateOf(total.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pembayaran") },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("${cart.sumOf { it.quantity }} item · Total ${price(total)}", fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    listOf("Tunai", "QRIS", "Transfer").forEach { value -> FilterChip(method == value, { method = value }, label = { Text(value) }) }
                }
                if (method == "Tunai") OutlinedTextField(cash, { cash = it.filter(Char::isDigit) }, Modifier.fillMaxWidth(), label = { Text("Uang diterima") }, singleLine = true)
                Text("QRIS/transfer hanya dicatat; belum memverifikasi pembayaran otomatis.", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
            }
        },
        confirmButton = { TextButton(onClick = { onConfirm(method, if (method == "Tunai") cash.toLongOrNull() ?: 0L else total) }) { Text("Selesaikan") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Kembali") } },
    )
}
