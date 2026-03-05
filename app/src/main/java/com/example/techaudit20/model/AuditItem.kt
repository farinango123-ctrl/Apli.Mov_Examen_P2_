package com.example.techaudit20.model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

enum class AuditStatus {
    PENDIENTE,
    OPERATIVO,
    DANIADO,
    NO_ENCONTRA
}

@Parcelize
@androidx.room.Entity(
    tableName = "equipos",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = Laboratorio::class,
            parentColumns = ["id"],
            childColumns = ["laboratorioId"],
            onDelete = androidx.room.ForeignKey.CASCADE // Si borras el lab, se borran sus equipos
        )
    ]
)
data class AuditItem(
    @androidx.room.PrimaryKey
    val id: String,
    val nombre: String,
    val ubicacion: String, // Puedes usar esto como detalle extra
    val fechaRegistro: String,
    var estado: AuditStatus = AuditStatus.PENDIENTE,
    var notas: String = "",
    val laboratorioId: String // Relación con el Padre
) : Parcelable