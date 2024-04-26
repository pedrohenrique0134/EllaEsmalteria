package com.app.ellaeamalteriasistemadelancamentos.repositorys

import android.annotation.SuppressLint
import com.app.ellaeamalteriasistemadelancamentos.models.Cliente
import com.app.ellaeamalteriasistemadelancamentos.models.Collaborators
import com.app.ellaeamalteriasistemadelancamentos.models.Lancamentos
import com.app.ellaeamalteriasistemadelancamentos.utils.UiState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.util.EventListener

class RepositoryImpl(
    val database: FirebaseFirestore
): Repository {
    @SuppressLint("SuspiciousIndentation")
    override suspend fun novoLancamento(
        nome: String,
        lancamentos: Lancamentos,
        result: (UiState<String>) -> Unit
    ) {
        val document = database.collection(nome)
            .document(lancamentos.entryOrExit.toString())
            .collection(lancamentos.data.toString())
            .document(lancamentos.id.toString())
        document.set(lancamentos)
            .addOnSuccessListener {
              val todos = database.collection(nome)
                  .document("Todas movimentacoes")
                  .collection(lancamentos.data.toString())
                  .document(lancamentos.id.toString())
                todos.set(lancamentos).addOnSuccessListener {
                    result.invoke(
                        UiState.Success(
                            "Sucess"
                        )
                    )
                }
            }.addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override suspend fun novoCliente(
        nome: String,
        cliente: Cliente,
        result: (UiState<String>) -> Unit
    ) {
        val document = database.collection("Clientes")
            .document(nome)
            document.set(cliente)
                .addOnSuccessListener {
                    result.invoke(
                        UiState.Success(
                            "Succes"
                        )
                    )
                }.addOnFailureListener {
                    result.invoke(
                        UiState.Failure(
                            it.localizedMessage
                        )
                    )
                }
    }

    override suspend fun novoCollaborator(
        collaborators: Collaborators,
        result: (UiState<String>) -> Unit
    ) {
        val document = database.collection("colaboradores").document(collaborators.nome!!)
        document.set(collaborators)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(
                        "Succes ao add collaborator"
                    )
                )
            }.addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }

    }

    override suspend fun getLancamentos(
        nome: String,
        entryOrExit: String,
        data: String,
        result: (UiState<ArrayList<Lancamentos>>) -> Unit
    ) {
        val document = database.collection(nome)
            .document(entryOrExit)
            .collection(data)
        document.addSnapshotListener { value, error ->
            val list: ArrayList<Lancamentos> = arrayListOf()
            val data = value?.toObjects(Lancamentos::class.java)
            if (data != null) {
                if (data.isNotEmpty()) {
                    list.addAll(data)
                    result.invoke(
                        UiState.Success(
                            list
                        )
                    )
                } else {
                    result.invoke(
                        UiState.Failure(
                            "List empty!"
                        )
                    )
                }
            }else{
                result.invoke(
                    UiState.Failure(
                        "Não existe lançaamentos pra esse mês."
                    )
                )
            }
        }


    }

    override suspend fun getCollaborator(
        result: (UiState<ArrayList<Collaborators>>) -> Unit
    ) {
        val document = database.collection("colaboradores")
        document.addSnapshotListener { value, error ->
            val list: ArrayList<Collaborators> = arrayListOf()
            val data  = value?.toObjects(Collaborators::class.java)
            if (data != null) {
                if (data.isEmpty()) {
                    result.invoke(
                        UiState.Failure(
                            "List empty!"
                        )
                    )
                } else {
                    list.addAll(data)
                    result.invoke(
                        UiState.Success(
                            list
                        )
                    )
                }
            }else{
                result.invoke(
                    UiState.Failure(
                        "Não temos usuario cadastrado!"
                    )
                )
            }
        }
    }

    override suspend fun getCliente(result: (UiState<ArrayList<Cliente>>) -> Unit) {
        val document = database.collection("Clientes")
        document.addSnapshotListener { value, error ->
            if (value != null){
                var list: ArrayList<Cliente> = arrayListOf()
                val dados = value.toObjects(Cliente::class.java)
                if (dados.isNotEmpty()){
                    list.addAll(dados)
                    result.invoke(
                        UiState.Success(
                            list
                        )
                    )
                }else{
                    result.invoke(
                        UiState.Failure(
                            "List Empty!"
                        )
                    )
                }
            }else{
                result.invoke(
                    UiState.Failure(
                        error?.localizedMessage
                    )
                )
            }
        }
    }
}