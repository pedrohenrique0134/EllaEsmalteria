package com.app.ellaeamalteriasistemadelancamentos.repositorys

import com.app.ellaeamalteriasistemadelancamentos.models.Cliente
import com.app.ellaeamalteriasistemadelancamentos.models.Collaborators
import com.app.ellaeamalteriasistemadelancamentos.models.Lancamentos
import com.app.ellaeamalteriasistemadelancamentos.utils.UiState

interface Repository {
    suspend fun novoLancamento(nome: String, lancamentos: Lancamentos, result: (UiState<String>) -> Unit)
    suspend fun novoCliente(nome: String, cliente: Cliente, result: (UiState<String>) -> Unit)
    suspend fun novoCollaborator(collaborators: Collaborators, result: (UiState<String>) -> Unit)
    suspend fun getLancamentos(nome: String, entryOrExit: String,data: String, result: (UiState<ArrayList<Lancamentos>>) -> Unit)
    suspend fun getCollaborator(result: (UiState<ArrayList<Collaborators>>) -> Unit)
    suspend fun getCliente(result: (UiState<ArrayList<Cliente>>) -> Unit)
}