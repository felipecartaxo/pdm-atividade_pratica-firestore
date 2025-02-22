package com.example.navegacao1.ui.telas

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.navegacao1.model.dados.Usuario
import com.example.navegacao1.model.dados.UsuarioDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TelaCadastro(
    modifier: Modifier = Modifier,
    onCadastroSuccess: () -> Unit,
    onCancelar: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var nome by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf<String?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp) // Padronizando a estilização semelhante ao da tela de login
    ) {
        Text(text = "Cadastro de Usuário")
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text(text = "Login") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text(text = "Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Alinhando os botões
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    if (nome.isNotEmpty() && senha.isNotEmpty()) {
                        scope.launch(Dispatchers.IO) {
                            val usuario = Usuario(nome = nome, senha = senha)
                            UsuarioDAO().adicionar(usuario) { usuarioAdicionado ->
                                if (usuarioAdicionado != null) {
                                    onCadastroSuccess()
                                } else {
                                    mensagemErro = "Erro ao cadastrar usuário!"
                                }
                            }
                        }
                    } else {
                        mensagemErro = "Preencha todos os campos!"
                    }
                }
            ) {
                Text(text = "Cadastrar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                modifier = Modifier.weight(1f),
                onClick = { onCancelar() }
            ) {
                Text(text = "Cancelar")
            }
        }
        mensagemErro?.let { erro ->
            LaunchedEffect(erro) {
                Toast.makeText(context, erro, Toast.LENGTH_SHORT).show()
                mensagemErro = null
            }
        }
    }
}