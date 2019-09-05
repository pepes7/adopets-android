package com.example.adopets.model

open class Usuario(
    open var email: String = "",
    open var senha: String = "",
    open var dataNasc: String = "",
    open var foto: String = "",
    open var telefone: String = "",
    open var nome: String = "",
    open var bairro: String = "",
    open var rua: String = "",
    open var numero: String = "",
    open var complemento: String = "",
    open var cep: String = ""
)

data class Doador(override var email: String = "", override var senha: String = "",
             override var dataNasc: String = "", override var foto: String = "",
             override var telefone: String = "", override var nome: String = "", override var bairro: String = "",
             override var rua: String = "", override var numero: String = "", override var complemento: String = "",
             override var cep: String = ""): Usuario(email, senha, dataNasc, foto, telefone, nome, bairro, rua, numero, complemento, cep)

data class Adotante(override var email: String = "", override var senha: String = "",
               override var dataNasc: String = "", override var foto: String = "",
               override var telefone: String = "", override var nome: String = "", override var bairro: String = "",
               override var rua: String = "", override var numero: String = "", override var complemento: String = "", override var cep: String = ""): Usuario(email, senha, dataNasc, foto, telefone, nome, bairro, rua, numero, complemento, cep)

data class Voluntario(override var email: String = "", override var senha: String = "",
                 override var dataNasc: String = "", override var foto: String = "",
                 override var telefone: String = "", override var nome: String = "", override var bairro: String = "",
                 override var rua: String = "", override var numero: String = "", override var complemento: String = "",
                 override var cep: String = "", var pontuacao: Float = 0F): Usuario(email, senha, dataNasc, foto, telefone, nome, bairro, rua, numero, complemento, cep)

data class Contratante(override var email: String = "", override var senha: String = "",
                  override var dataNasc: String = "", override var foto: String = "",
                  override var telefone: String = "", override var nome: String = "", override var bairro: String = "",
                  override var rua: String = "", override var numero: String = "", override var complemento: String = "",
                  override var cep: String = ""): Usuario(email, senha, dataNasc, foto, telefone, nome, bairro, rua, numero, complemento, cep)

data class Processo(
    var codigo: Long = 0L,
    var motivo: String = "",
    var situacao: String = "",
    var dataCriacao: String = "",
    var dataFim: String = "",
    var doador: Doador = Doador()
)

data class AdotanteProcesso(
    var adotante: Adotante = Adotante(),
    var processo: Processo = Processo()
)

data class Servico(
    var codigo: Long = 0L,
    var tipo: String = "",
    var dataInicio: String = "",
    var dataFim: String = "",
    var descricao: String = "",
    var voluntario: Voluntario = Voluntario()
)

data class ContratanteServico(
    var contratante: Contratante = Contratante(),
    var servico: Servico = Servico()
)

data class Animal(
    var codigo: Long = 0L,
    var foto: String = "",
    var situacao: String = "",
    var raca: String = "",
    var descricao: String = "",
    var tamanho: String = "",
    var sexo: String = "",
    var necessidade: String = "",
    var tipo: String = "",
    var nome: String = "",
    var dataNasc: String = ""
)

data class AnimalProceso(
    var animal: Animal = Animal(),
    var processo: Processo = Processo()
)

data class AnimalServico(
    var animal: Animal = Animal(),
    var servico: Servico = Servico()
)
