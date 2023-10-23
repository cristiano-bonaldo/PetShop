package cvb.com.br.petshop.network.util

import cvb.com.br.petshop.network.model.ApiProduct
import cvb.com.br.petshop.network.model.ApiProductResult

object FakeApiProductResult {

    private val apiProduct01 = ApiProduct(1, "Escova para pet", "500gr", 1, "10,99", "https://www.petlove.com.br/images/products/231062/product/Escova_Furminator_New_C%C3%A3es_Pelo_Longo_2316474_2_G.jpg?1627741260")
    private val apiProduct02 = ApiProduct(2,"Tapete Higiênico Super Secão Citrus - 30 Unidades","200gr",30,"9,99", "https://www.petlove.com.br/images/products/260949/product/Tapete_Higi%C3%AAnico_Super_Sec%C3%A3o_Citrus_3104451_%281%29.jpg?1660325640")
    private val apiProduct03 = ApiProduct(3, "Petisco Pedigree Dentastix para Cães Adultos Porte Médio Sabor Carne", "1kg", 1, "20,99", "https://www.petlove.com.br/images/products/214121/product/Petisco_Pedigree_Dentastix_para_C%C3%A3es_Adultos_Ra%C3%A7as_M%C3%A9dias_-_7_Unidades_3104639-2_1.jpg?1627687506")
    private val apiProduct04 = ApiProduct(4, "Complexo Vitamínico Aminomix Pet", "1kg", 1, "120,99", "https://www.petlove.com.br/images/products/256039/product/7898053580470.png?1653509357")
    private val apiProduct05 = ApiProduct(5, "Cortador de Unha Guilhotina Médio", "1kg", 1, "59,99", "https://www.petlove.com.br/images/products/164111/product/7898904626302.jpg?1627551576")
    private val apiProduct06 = ApiProduct(6, "Antiparasitário Fenzol Pet Agener União com 6 unidades - 500 mg", "100gr", 1, "79,99", "https://www.petlove.com.br/images/products/232942/product/Antiparasit%C3%A1rio_Fenzol_Pet_Agener_Uni%C3%A3o_com_6_unidades_-_500_mg_31017361_2.jpg?1627747074")
    private val apiProduct07 = ApiProduct(7, "Ração Nutral Botia", "200gr", 1, "100,00", "https://www.petlove.com.br/images/products/191059/product/Botia_.jpg?1627615564")
    private val apiProduct08 = ApiProduct(8, "Ração Seca Pedigree para Cães Filhotes Raças Médias e Grandes", "10.1kg", 1, "100,00", "https://www.petlove.com.br/images/products/259806/product/Ra%C3%A7%C3%A3o_Pedigree_Carne_Frango_e_Cereais_C%C3%A3es_Filhotes_18_kg_2513333.png?1659451710")
    private val apiProduct09 = ApiProduct(9, "Ração Seca PremieR Pet Golden Salmão para Gatos Adultos Castrados", "3.1kg", 1, "200,00", "https://www.petlove.com.br/images/products/266053/product/Ra%C3%A7%C3%A3o_Seca_PremieR_Pet_Golden_Salm%C3%A3o_para_Gatos_Adultos_Castrados_-_10_1_Kg_31022435-3_1.jpg?1674076294")
    private val apiProduct10 = ApiProduct(10, "Conjunto Peitoral Style Mesh e Guia Rosa", "500gr", 1, "50,00", "https://www.petlove.com.br/images/products/235676/product/Kit_Style_meuMiAu_Unic%C3%B3rnio_Rosa_2637501.jpg?1627756265")

    private val listApiProduct = listOf(
        apiProduct01, apiProduct02,
        apiProduct03, apiProduct04,
        apiProduct05, apiProduct06,
        apiProduct07, apiProduct08,
        apiProduct09, apiProduct10
    )

    val apiProductResult = ApiProductResult(listApiProduct)
}