package com.cafeteros.historia.ui.features.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cafeteros.historia.data.model.User
import com.cafeteros.historia.ui.features.auth.components.UserType
import com.cafeteros.historia.ui.theme.BrandColors

/**
 * Pantalla de gestión de usuarios para el rol Administrador.
 *
 * Contiene:
 *  - Filtros por rol (chips).
 *  - Lista de usuarios reactiva (`Flow<List<User>>` desde Room).
 *  - FAB de creación.
 *  - Diálogos para crear, eliminar y restablecer contraseña.
 *  - Snackbar para mensajes de feedback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(
    viewModel: UserManagementViewModel,
    onBack: () -> Unit
) {
    val users by viewModel.users.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var roleFilter by remember { mutableStateOf<UserType?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var pendingDelete by remember { mutableStateOf<User?>(null) }
    var pendingReset by remember { mutableStateOf<User?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.feedback) {
        uiState.feedback?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.consumeFeedback()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Gestión de usuarios",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BrandColors.CoffeeBrown,
                    titleContentColor = BrandColors.CreamWhite,
                    navigationIconContentColor = BrandColors.CreamWhite
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = BrandColors.CoffeeBrown,
                contentColor = BrandColors.CreamWhite,
                icon = { Icon(Icons.Outlined.PersonAdd, contentDescription = null) },
                text = { Text("Nuevo usuario") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BrandColors.AuthBackground
    ) { paddingValues ->
        UserManagementContent(
            paddingValues = paddingValues,
            users = users,
            roleFilter = roleFilter,
            onRoleFilterChange = { roleFilter = it },
            onDeleteRequest = { pendingDelete = it },
            onResetPasswordRequest = { pendingReset = it }
        )
    }

    if (showCreateDialog) {
        CreateUserDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { email, name, phone, password, userType ->
                viewModel.createUser(email, name, phone, password, userType)
                showCreateDialog = false
            }
        )
    }

    pendingDelete?.let { user ->
        ConfirmDeleteDialog(
            user = user,
            onConfirm = {
                viewModel.disableUser(user)
                pendingDelete = null
            },
            onDismiss = { pendingDelete = null }
        )
    }

    pendingReset?.let { user ->
        ResetPasswordDialog(
            user = user,
            onConfirm = {
                viewModel.sendPasswordReset(user)
                pendingReset = null
            },
            onDismiss = { pendingReset = null }
        )
    }
}

@Composable
private fun UserManagementContent(
    paddingValues: PaddingValues,
    users: List<User>,
    roleFilter: UserType?,
    onRoleFilterChange: (UserType?) -> Unit,
    onDeleteRequest: (User) -> Unit,
    onResetPasswordRequest: (User) -> Unit
) {
    val filtered = remember(users, roleFilter) {
        if (roleFilter == null) users else users.filter { it.userType == roleFilter }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        RoleFilterRow(selected = roleFilter, onChange = onRoleFilterChange)
        Text(
            text = "${filtered.size} de ${users.size} usuarios",
            fontFamily = FontFamily.SansSerif,
            fontSize = 12.sp,
            color = BrandColors.TextSecondary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        if (filtered.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered, key = { it.id }) { user ->
                    UserRow(
                        user = user,
                        onDelete = { onDeleteRequest(user) },
                        onResetPassword = { onResetPasswordRequest(user) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RoleFilterRow(selected: UserType?, onChange: (UserType?) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RoleFilterChip(label = "Todos", selected = selected == null) { onChange(null) }
        RoleFilterChip(label = "Compradores", selected = selected == UserType.COMPRADOR) {
            onChange(UserType.COMPRADOR)
        }
        RoleFilterChip(label = "Caficultores", selected = selected == UserType.CAFICULTOR) {
            onChange(UserType.CAFICULTOR)
        }
        RoleFilterChip(label = "Admins", selected = selected == UserType.ADMINISTRADOR) {
            onChange(UserType.ADMINISTRADOR)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoleFilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, fontSize = 12.sp) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = BrandColors.CoffeeBrown,
            selectedLabelColor = BrandColors.CreamWhite
        )
    )
}

@Composable
private fun UserRow(
    user: User,
    onDelete: () -> Unit,
    onResetPassword: () -> Unit
) {
    val (icon, accent) = when (user.userType) {
        UserType.COMPRADOR -> Icons.Outlined.ShoppingBag to BrandColors.ForestGreen
        UserType.CAFICULTOR -> Icons.Outlined.Eco to BrandColors.CoffeeBrown
        UserType.ADMINISTRADOR -> Icons.Outlined.AdminPanelSettings to BrandColors.RatingStar
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onResetPassword),
        shape = RoundedCornerShape(12.dp),
        color = BrandColors.CardBackground,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserAvatar(icon = icon, accent = accent)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.name.ifBlank { "(sin nombre)" },
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = BrandColors.TextPrimary
                )
                Text(
                    text = user.email,
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    color = BrandColors.TextSecondary
                )
                Text(
                    text = user.userType.title.removePrefix("Soy "),
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 11.sp,
                    color = accent,
                    fontWeight = FontWeight.SemiBold
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFC62828)
                )
            }
        }
    }
}

@Composable
private fun UserAvatar(icon: ImageVector, accent: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color = accent.copy(alpha = 0.15f), shape = RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = null,
                tint = BrandColors.TextSecondary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.size(12.dp))
            Text(
                text = "No hay usuarios con ese filtro",
                fontFamily = FontFamily.SansSerif,
                color = BrandColors.TextSecondary
            )
        }
    }
}

@Composable
private fun CreateUserDialog(
    onDismiss: () -> Unit,
    onConfirm: (email: String, name: String, phone: String, password: String, userType: UserType) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserType.COMPRADOR) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo usuario", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Nombre completo") },
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = email, onValueChange = { email = it },
                    label = { Text("Correo") },
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = phone, onValueChange = { phone = it },
                    label = { Text("Teléfono") },
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = password, onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                    singleLine = true
                )
                Text(
                    text = "Rol",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 12.sp,
                    color = BrandColors.TextSecondary
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    UserType.entries.forEach { type ->
                        RoleFilterChip(
                            label = type.title.removePrefix("Soy "),
                            selected = selectedRole == type
                        ) { selectedRole = type }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(email, name, phone, password, selectedRole) }
            ) { Text("Crear", color = BrandColors.CoffeeBrown) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = BrandColors.TextSecondary) }
        }
    )
}

@Composable
private fun ConfirmDeleteDialog(user: User, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Deshabilitar usuario", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold) },
        text = {
            Text(
                "¿Deshabilitar la cuenta de ${user.email}? El usuario no podrá " +
                        "iniciar sesión. Puedes reactivarla luego desde el panel.",
                fontFamily = FontFamily.SansSerif
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Deshabilitar", color = Color(0xFFC62828))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = BrandColors.TextSecondary) }
        }
    )
}

/**
 * Diálogo para enviar al usuario un correo con el link de restablecimiento.
 *
 * El admin nunca conoce ni elige la nueva contraseña — sólo dispara el envío.
 * Firebase Auth genera el link y el usuario establece la nueva clave desde
 * su bandeja de entrada.
 */
@Composable
private fun ResetPasswordDialog(user: User, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Restablecer contraseña",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                "Se enviará un correo a ${user.email} con un enlace para que el " +
                        "usuario establezca una nueva contraseña.",
                fontFamily = FontFamily.SansSerif,
                fontSize = 13.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Enviar correo", color = BrandColors.CoffeeBrown)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = BrandColors.TextSecondary) }
        }
    )
}
