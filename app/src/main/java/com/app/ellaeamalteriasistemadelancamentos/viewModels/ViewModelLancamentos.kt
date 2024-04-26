package com.app.ellaeamalteriasistemadelancamentos.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.ellaeamalteriasistemadelancamentos.models.Cliente
import com.app.ellaeamalteriasistemadelancamentos.models.Collaborators
import com.app.ellaeamalteriasistemadelancamentos.models.Lancamentos
import com.app.ellaeamalteriasistemadelancamentos.repositorys.Repository
import com.app.ellaeamalteriasistemadelancamentos.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelLancamentos @Inject constructor(
    val repository: Repository
): ViewModel() {

    private val _novoLance = MutableLiveData<UiState<String>>()
    val novoLance: LiveData<UiState<String>>
        get() = _novoLance

    private val _novoCliente = MutableLiveData<UiState<String>>()
    val novoCliente: LiveData<UiState<String>>
        get() = _novoCliente

    private val _novoCollaborator = MutableLiveData<UiState<String>>()
    val novoCollaborator: LiveData<UiState<String>>
        get() = _novoCollaborator

    private val _getCollaborator = MutableLiveData<UiState<ArrayList<Collaborators>>>()
    val getCollaborators: LiveData<UiState<ArrayList<Collaborators>>>
        get() = _getCollaborator

    private val _getLances = MutableLiveData<UiState<ArrayList<Lancamentos>>>()
    val getLances: LiveData<UiState<ArrayList<Lancamentos>>>
        get() = _getLances

    private val _getClientes = MutableLiveData<UiState<ArrayList<Cliente>>>()
    val getClientes: LiveData<UiState<ArrayList<Cliente>>>
        get() = _getClientes

    fun novoLance(
        nome: String,
        lancamentos: Lancamentos
    ){
        viewModelScope.launch {
            _novoLance.value = UiState.Loading
            repository.novoLancamento(nome,lancamentos){
                _novoLance.value = it
            }
        }
    }

    fun novoCollaBorator(
        collaborators: Collaborators
    ){
        viewModelScope.launch {
            _novoCollaborator.value = UiState.Loading
            repository.novoCollaborator(
                collaborators
            ){
                _novoCollaborator.value = it
            }
        }
    }

    fun novoCliente(
        nome: String,
        cliente: Cliente
    ){
        viewModelScope.launch {
            _novoCliente.value = UiState.Loading
            repository.novoCliente(
                nome,
                cliente
            ){
                _novoCliente.value = it
            }
        }
    }

    fun getCollaborator(){
        viewModelScope.launch {
            _getCollaborator.value = UiState.Loading
            repository.getCollaborator{
                _getCollaborator.value = it
            }
        }
    }

    fun getLances(
       nome: String,
       entryOrExit: String,
       data: String,
    ){
        viewModelScope.launch {
            _getLances.value = UiState.Loading
            repository.getLancamentos(
                nome,
                entryOrExit,
                data
            ){
                _getLances.value = it
            }
        }

    }

    fun getCliente(){
        viewModelScope.launch {
            _getClientes.value = UiState.Loading
            repository.getCliente {
                _getClientes.value = it
            }
        }
    }

}