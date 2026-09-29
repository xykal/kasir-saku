package id.kasirsaku.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.kasirsaku.app.data.PosDatabase
import id.kasirsaku.app.model.CartLine
import id.kasirsaku.app.model.Product
import id.kasirsaku.app.model.SaleSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val tabs = listOf("Kasir", "Produk", "Riwayat", "Laporan")
private fun rupiah(value: Long) = "Rp" + value.toString().reversed().chunked(3).joinToString(".").reversed()

@Composable
fun KasirSakuApp() {
    val context = LocalContext.current
    val db = remember { PosDatabase(context.applicationContext) }
    val scope = rememberCoroutineScope()
    var products by remember { mutableStateOf(emptyList<Product>()) }
    var sales by remember { mutableStateOf(emptyList<SaleSummary>()) }
    val cart = remember { mutableStateListOf<CartLine>() }
    var tab by remember { mutableStateOf("Kasir") }
    var query by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Semua") }
    var addProduct by remember { mutableStateOf(false) }
    var checkout by remember { mutableStateOf(false) }
    var receiptNotice by remember { mutableStateOf("") }
    val wide = androidx.compose.ui.platform.LocalConfiguration.current.screenWidthDp >= 840

    fun refresh() {
        scope.launch {
            val result = withContext(Dispatchers.IO) { db.products() to db.todaySales() }
            products = result.first
            sales = result.second
        }
    }
    fun changeCart(product: Product, delta: Int) {
        val index = cart.indexOfFirst { it.product.id == product.id }
        val current = if (index >= 0) cart[index].quantity else 0
        val next = current + delta
        if (next <= 0) { if (index >= 0) cart.removeAt(index); return }
        if (product.kind == "Barang" && next > product.stock) return
        val line = CartLine(product, next)
        if (index >= 0) cart[index] = line else cart.add(line)
    }
    LaunchedEffect(Unit) { refresh() }

    val total = cart.sumOf { it.product.price * it.quantity }
    val todayTotal = sales.sumOf { it.total }
    Scaffold(
        bottomBar = {
            Column {
                if (!wide && tab == "Kasir" && cart.isNotEmpty()) {
                    Button(onClick = { checkout = true }, modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp).heightIn(min = 54.dp)) {
                        Text("Keranjang · ${cart.sumOf { it.quantity }} item     ${rupiah(total)}")
                    }
                }
                NavigationBar(containerColor = Color.White) {
                    tabs.forEach { item ->
                        val icon = when (item) { "Kasir" -> "cashier"; "Produk" -> "box"; "Riwayat" -> "history"; else -> "chart" }
                        NavigationBarItem(
                            selected = tab == item,
                            onClick = { tab = item },
                            icon = { LineIcon(icon, Modifier.size(22.dp)) },
                            label = { Text(item, fontSize = 12.sp) },
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { inset ->
        Column(Modifier.fillMaxSize().padding(inset).padding(horizontal = 16.dp)) {
            Row(Modifier.fillMaxWidth().padding(top = 14.dp, bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Kedai Sore  ·  Mode offline", color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp)
                    Text(tab, fontSize = 25.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
                if (tab == "Kasir" || tab == "Produk") {
                    Button(onClick = { addProduct = true }, modifier = Modifier.heightIn(min = 48.dp)) { Text("＋ Produk") }
                }
            }
            when (tab) {
                "Kasir" -> {
                    if (wide) {
                        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                                SummaryStrip(todayTotal, sales.size, products.count { it.kind == "Barang" && it.stock <= 5 })
                                Catalog(products, query, { query = it }, category, { category = it }, ::changeCart, Modifier.fillMaxWidth())
                            }
                            Card(Modifier.width(330.dp).padding(bottom = 14.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                                CartPanel(cart, ::changeCart, { checkout = true }, Modifier.fillMaxSize().padding(14.dp))
                            }
                        }
                    } else {
                        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                            SummaryStrip(todayTotal, sales.size, products.count { it.kind == "Barang" && it.stock <= 5 })
                            Catalog(products, query, { query = it }, category, { category = it }, ::changeCart, Modifier.fillMaxWidth())
                            Spacer(Modifier.height(18.dp))
                        }
                    }
                }
                "Produk" -> ProductList(products, Modifier.fillMaxSize())
                "Riwayat" -> SalesList(sales, Modifier.fillMaxSize())
                else -> ReportPanel(todayTotal, sales, Modifier.fillMaxSize())
            }
        }
    }
    if (addProduct) AddProductDialog(
        onDismiss = { addProduct = false },
        onSave = { name, group, price, stock, kind ->
            scope.launch {
                withContext(Dispatchers.IO) { db.addProduct(name, group, price, stock, kind) }
                addProduct = false
                refresh()
            }
        },
    )
    if (checkout) CheckoutDialog(
        cart = cart.toList(), total = total,
        onDismiss = { checkout = false },
        onConfirm = { method, cash ->
            scope.launch {
                try {
                    val id = withContext(Dispatchers.IO) { db.finishSale(cart.toList(), method, cash) }
                    cart.clear()
                    checkout = false
                    receiptNotice = "Transaksi $id tersimpan. Cetak struk thermal menyusul setelah uji printer."
                    refresh()
                } catch (error: IllegalArgumentException) {
                    receiptNotice = error.message ?: "Transaksi gagal"
                }
            }
        },
    )
    if (receiptNotice.isNotBlank()) AlertDialog(
        onDismissRequest = { receiptNotice = "" },
        title = { Text("Info transaksi") },
        text = { Text(receiptNotice) },
        confirmButton = { TextButton(onClick = { receiptNotice = "" }) { Text("Oke") } },
    )
}
