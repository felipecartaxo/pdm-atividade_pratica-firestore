package com.example.navegacao1.model.dados

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects

//classe que pega dados do Firestore
class UsuarioDAO {

    val db = FirebaseFirestore.getInstance()

    fun buscar(callback: (List<Usuario>) -> Unit) {
        db.collection("usuarios").get()
            .addOnSuccessListener { document ->
                val usuarios = document.toObjects<Usuario>()
                callback(usuarios)
            }
            .addOnFailureListener {
                callback(emptyList())
            }
    }

    fun buscarPorNome(nome: String, callback: (Usuario?) -> Unit) {
        db.collection("usuarios").whereEqualTo("nome", nome).get()
            .addOnSuccessListener { document ->
                if (!document.isEmpty) {
                    val usuario = document.documents[0].toObject<Usuario>()
                    callback(usuario)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun buscarPorId(id: String, callback: (Usuario?) -> Unit) {
        // Busca o documento pelo id
        db.collection("usuarios").document(id).get()
            .addOnSuccessListener { document ->
                // Se o documento existir, converte para um objeto Usuario
                if (document.exists()) {
                    val usuario = document.toObject<Usuario>()
                    callback(usuario)
                // Se o documento não existir, retorna null
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener {
                callback(null)
            }
    }


    fun adicionar(usuario: Usuario, callback: (Usuario?) -> Unit) {
        // Cria um novo documento, (id é gerado automaticamente pelo Firestore)
        db.collection("usuarios").add(usuario)
            .addOnSuccessListener { documentReference ->
                // Após adicionar, buscamos o documento para retornar os dados (incluindo o id se necessário)
                documentReference.get()
                    .addOnSuccessListener { document ->
                        val usuarioAdicionado = document.toObject<Usuario>()
                        callback(usuarioAdicionado)
                    }
                    .addOnFailureListener {
                        callback(null)
                    }
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}