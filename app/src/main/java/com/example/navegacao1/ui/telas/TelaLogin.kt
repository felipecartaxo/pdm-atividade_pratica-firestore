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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.navegacao1.model.dados.UsuarioDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val usuarioDAO: UsuarioDAO = UsuarioDAO()

@Composable
fun TelaLogin(
    modifier: Modifier = Modifier,
    onSigninClick: () -> Unit, // Callback para navegação para TelaPrincipal
    onCadastroClick: () -> Unit  // Callback para navegação para TelaCadastro
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Armazenando o login e senha ao digitar
    var login by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf<String?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(text = "Login")
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text(text = "Login") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = senha,
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { senha = it },
            label = { Text(text = "Senha") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Posicionando os botões lado a lado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        usuarioDAO.buscarPorNome(login, callback = { usuario ->
                            if (usuario != null && usuario.senha == senha) {
                                onSigninClick()
                            } else {
                                mensagemErro = "Login ou senha inválidos!"
                            }
                        })
                    }
                }
            ) {
                Text("Entrar")
            }
            Spacer(modifier = Modifier.height(0.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                modifier = Modifier.weight(1f),
                onClick = { onCadastroClick() }
            ) {
                Text("Cadastrar")
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