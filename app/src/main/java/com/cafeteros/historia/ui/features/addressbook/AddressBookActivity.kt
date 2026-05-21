package com.cafeteros.historia.ui.features.addressbook

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.cafeteros.historia.CafeterosApplication
import com.cafeteros.historia.data.model.SavedAddress
import com.cafeteros.historia.data.repository.AddressRepository
import com.cafeteros.historia.data.repository.UserRepository
import com.cafeteros.historia.ui.theme.BrandColors
import com.cafeteros.historia.ui.theme.BrandSpacing
import com.cafeteros.historia.ui.theme.CafeterosTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Estado UI de la libreta de direcciones. */
data class AddressBookUiState(
    val addresses: List<SavedAddress> = emptyList(),
    val editing: SavedAddress? = null
)

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class AddressBookViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as CafeterosApplication
    private val userRepository: UserRepository = app.userRepository
    private val addressRepository: AddressRepository = app.addressRepository

    val addresses: StateFlow<List<SavedAddress>> = flowOf(userRepository.currentUid())
        .flatMapLatest { uid ->
            if (uid == null) flowOf(emptyList())
            else addressRepository.observeMyAddresses(uid)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _editing = MutableStateFlow<SavedAddress?>(null)
    val editing: StateFlow<SavedAddress?> = _editing.asStateFlow()

    private val _toast = MutableStateFlow<String?>(null)
    val toast: StateFlow<String?> = _toast.asStateFlow()

    fun startNew() {
        _editing.value = SavedAddress(label = "Casa")
    }

    fun startEdit(address: SavedAddress) {
        _editing.value = address
    }

    fun cancelEdit() {
        _editing.value = null
    }

    fun save(address: SavedAddress) {
        viewModelScope.launch {
            val uid = userRepository.currentUid() ?: return@launch
            runCatching { addressRepository.saveAddress(uid, address) }
                .onSuccess {
                    _toast.value = "Dirección guardada"
                    _editing.value = null
                }
                .onFailure { _toast.value = "No se pudo guardar la dirección" }
        }
    }

    fun delete(addressId: String) {
        viewModelScope.launch {
            val uid = userRepository.currentUid() ?: return@launch
            runCatching { addressRepository.deleteAddress(uid, addressId) }
                .onSuccess { _toast.value = "Dirección eliminada" }
                .onFailure { _toast.value = "No se pudo eliminar" }
        }
    }

    fun setDefault(addressId: String) {
        viewModelScope.launch {
            val uid = userRepository.currentUid() ?: return@launch
            runCatching { addressRepository.setDefault(uid, addressId) }
                .onSuccess { _toast.value = "Dirección predeterminada actualizada" }
        }
    }

    fun consumeToast() {
        _toast.value = null
    }
}

class AddressBookActivity : ComponentActivity() {

    private val viewModel: AddressBookViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeterosTheme {
                val addresses by viewModel.addresses.collectAsStateWithLifecycle()
                val editing by viewModel.editing.collectAsStateWithLifecycle()
                val toast by viewModel.toast.collectAsStateWithLifecycle()

                LaunchedEffect(toast) {
                    toast?.let {
                        Toast.makeText(this@AddressBookActivity, it, Toast.LENGTH_SHORT).show()
                        viewModel.consumeToast()
                    }
                }

                if (editing != null) {
                    AddressEditScreen(
                        initial = editing!!,
                        onCancel = viewModel::cancelEdit,
                        onSave = viewModel::save
                    )
                } else {
                    AddressBookScreen(
                        addresses = addresses,
                        onBack = ::finish,
                        onAdd = viewModel::startNew,
                        onEdit = viewModel::startEdit,
                        onDelete = viewModel::delete,
                        onSetDefault = viewModel::setDefault
                    )
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AddressBookActivity::class.java))
        }
    }
}

@Composable
private fun AddressBookScreen(
    addresses: List<SavedAddress>,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onEdit: (SavedAddress) -> Unit,
    onDelete: (id: String) -> Unit,
    onSetDefault: (id: String) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(BrandColors.AuthBackground)
        .systemBarsPadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Volver",
                    tint = BrandColors.TextPrimary
                )
            }
            Text(
                text = "Mis Direcciones",
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandColors.TextPrimary
                )
            )
            IconButton(onClick = onAdd) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Agregar dirección",
                    tint = BrandColors.FarmerPrimary
                )
            }
        }

        if (addresses.isEmpty()) {
            EmptyAddresses(modifier = Modifier.weight(1f), onAdd = onAdd)
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = BrandSpacing.lg,
                    vertical = BrandSpacing.sm
                ),
                verticalArrangement = Arrangement.spacedBy(BrandSpacing.sm)
            ) {
                items(addresses) { addr ->
                    AddressCard(
                        address = addr,
                        onEdit = { onEdit(addr) },
                        onDelete = { onDelete(addr.id) },
                        onSetDefault = { onSetDefault(addr.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyAddresses(modifier: Modifier = Modifier, onAdd: () -> Unit) {
    Column(
        modifier = modifier.fillMaxWidth().padding(BrandSpacing.lg),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(BrandColors.InputBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = BrandColors.CoffeeBrown,
                modifier = Modifier.size(56.dp)
            )
        }
        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Text(
            text = "Sin direcciones guardadas",
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = BrandColors.TextPrimary
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Agrega una dirección para que tus pedidos lleguen sin escribirla cada vez.",
            color = BrandColors.TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
        Spacer(modifier = Modifier.height(BrandSpacing.lg))
        Button(
            onClick = onAdd,
            modifier = Modifier.height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandColors.CoffeeBrown,
                contentColor = Color.White
            )
        ) {
            Icon(imageVector = Icons.Outlined.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text(text = "Agregar dirección", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun AddressCard(
    address: SavedAddress,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSetDefault: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrandColors.CardBackground, RoundedCornerShape(12.dp))
            .padding(BrandSpacing.md),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = address.label.ifBlank { "Dirección" },
                modifier = Modifier.weight(1f),
                color = BrandColors.TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif
            )
            if (address.isDefault) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFF1CD7B), RoundedCornerShape(50))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "PREDETERMINADA",
                        color = Color(0xFF6B4F1A),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
        Text(
            text = address.line,
            color = BrandColors.TextPrimary,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
        if (address.recipientName.isNotBlank() || address.recipientPhone.isNotBlank()) {
            Text(
                text = "👤 ${address.recipientName} · ${address.recipientPhone}",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp
            )
        }
        if (address.notes.isNotBlank()) {
            Text(
                text = "📝 ${address.notes}",
                color = BrandColors.TextSecondary,
                fontSize = 11.sp,
                fontStyle = FontStyle.Italic
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            if (!address.isDefault) {
                Text(
                    text = "Hacer predeterminada",
                    modifier = Modifier
                        .clickable(onClick = onSetDefault)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    color = BrandColors.FarmerPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Editar",
                    tint = BrandColors.TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Borrar",
                    tint = BrandColors.TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun AddressEditScreen(
    initial: SavedAddress,
    onCancel: () -> Unit,
    onSave: (SavedAddress) -> Unit
) {
    var label by remember { mutableStateOf(initial.label) }
    var line by remember { mutableStateOf(initial.line) }
    var recipient by remember { mutableStateOf(initial.recipientName) }
    var phone by remember { mutableStateOf(initial.recipientPhone) }
    var notes by remember { mutableStateOf(initial.notes) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(BrandColors.AuthBackground)
        .systemBarsPadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrandSpacing.sm, vertical = BrandSpacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCancel) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Cancelar",
                    tint = BrandColors.TextPrimary
                )
            }
            Text(
                text = if (initial.id.isBlank()) "Nueva dirección" else "Editar dirección",
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BrandColors.TextPrimary
                )
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrandSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(BrandSpacing.md)
        ) {
            Spacer(modifier = Modifier.height(BrandSpacing.sm))
            LabeledInput(label = "ETIQUETA", value = label, hint = "Casa, Oficina…", onChange = { label = it })
            LabeledInput(label = "DIRECCIÓN", value = line, hint = "Calle 5 # 12-34, Bogotá", onChange = { line = it }, lines = 2)
            LabeledInput(label = "NOMBRE DEL RECEPTOR", value = recipient, hint = "Tu nombre", onChange = { recipient = it })
            LabeledInput(label = "TELÉFONO", value = phone, hint = "300 123 4567", onChange = { phone = it.filter { c -> c.isDigit() || c == ' ' } })
            LabeledInput(label = "INSTRUCCIONES (opcional)", value = notes, hint = "Apto 401, llamar al portero…", onChange = { notes = it }, lines = 2)

            Spacer(modifier = Modifier.height(BrandSpacing.lg))

            Button(
                onClick = {
                    onSave(initial.copy(
                        label = label.trim(),
                        line = line.trim(),
                        recipientName = recipient.trim(),
                        recipientPhone = phone.trim(),
                        notes = notes.trim()
                    ))
                },
                enabled = line.trim().isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandColors.CoffeeBrown,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Guardar dirección", fontWeight = FontWeight.SemiBold)
            }
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(text = "Cancelar", color = BrandColors.TextPrimary)
            }
            Spacer(modifier = Modifier.height(BrandSpacing.lg))
        }
    }
}

@Composable
private fun LabeledInput(
    label: String,
    value: String,
    hint: String,
    onChange: (String) -> Unit,
    lines: Int = 1
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            color = BrandColors.TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BrandColors.InputBackground, RoundedCornerShape(12.dp))
                .padding(BrandSpacing.md)
        ) {
            if (value.isBlank()) {
                Text(
                    text = hint,
                    color = BrandColors.TextSecondary,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onChange,
                textStyle = TextStyle(
                    color = BrandColors.TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height((24 * lines).dp)
            )
        }
    }
}
