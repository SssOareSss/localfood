package com.example.localfood.data

data class CarrinhoItem(
    val id: Int,
    val nome: String,
    val preco: Double,
    val quantidade: Int,
    val vendedorNome: String
)

object CartManager {
    private val _carrinhosPorVendedor = mutableMapOf<String, MutableList<CarrinhoItem>>()

    fun adicionarItem(nome: String, preco: Double, vendedorNome: String) {
        val carrinhoVendedor = _carrinhosPorVendedor.getOrPut(vendedorNome) { mutableListOf() }
        val itemExistente = carrinhoVendedor.find { it.nome == nome }

        if (itemExistente != null) {
            val index = carrinhoVendedor.indexOf(itemExistente)
            carrinhoVendedor[index] = itemExistente.copy(quantidade = itemExistente.quantidade + 1)
        } else {
            val novoItem = CarrinhoItem(
                id = carrinhoVendedor.size + 1,
                nome = nome,
                preco = preco,
                quantidade = 1,
                vendedorNome = vendedorNome
            )
            carrinhoVendedor.add(novoItem)
        }
    }

    fun removerItem(vendedorNome: String, itemId: Int) {
        val carrinho = _carrinhosPorVendedor[vendedorNome]
        carrinho?.removeIf { it.id == itemId }
        if (carrinho.isNullOrEmpty()) {
            _carrinhosPorVendedor.remove(vendedorNome)
        }
    }

    fun alterarQuantidade(vendedorNome: String, itemId: Int, novaQuantidade: Int) {
        val carrinho = _carrinhosPorVendedor[vendedorNome]
        val itemIndex = carrinho?.indexOfFirst { it.id == itemId }
        if (itemIndex != null && itemIndex >= 0) {
            if (novaQuantidade > 0) {
                carrinho[itemIndex] = carrinho[itemIndex].copy(quantidade = novaQuantidade)
            } else {
                removerItem(vendedorNome, itemId)
            }
        }
    }

    fun getCarrinhos(): Map<String, List<CarrinhoItem>> {
        return _carrinhosPorVendedor.mapValues { it.value.toList() }
    }

    fun limparTudo() {
        _carrinhosPorVendedor.clear()
    }
}
