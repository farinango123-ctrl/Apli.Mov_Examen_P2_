package com.example.techaudit20.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

enum class AuditStatus {
    @SerializedName("Pendiente") PENDIENTE,
    @SerializedName("Operativo") OPERATIVO,
    @SerializedName("Dañado") DANIADO,
    @SerializedName("No Encontrado") NO_ENCONTRA
}

@Parcelize
@Entity(
    tableName = "equipos",
    foreignKeys = [
        ForeignKey(
            entity = Laboratorio::class,
            parentColumns = ["id"],
            childColumns = ["laboratorioId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AuditItem(
    @PrimaryKey
    @SerializedName("id")
    val id: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("ubicacion")
    val ubicacion: String,

    @SerializedName("fechaRegistro")
    val fechaRegistro: String,

    @SerializedName("estado")
    var estado: AuditStatus = AuditStatus.PENDIENTE,

    @SerializedName("notas")
    var notas: String = "",

    @SerializedName("laboratorioId")
    val laboratorioId: String,

    @SerializedName("fotoUri")
    val fotoUri: String = "https://picsum.photos/200"
) : Parcelable