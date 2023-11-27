package Modelo
import android.os.Parcel
import android.os.Parcelable

data class Lista(var nombre: String, var mes: Int, var dia: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeInt(mes)
        parcel.writeInt(dia)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Lista> {
        override fun createFromParcel(parcel: Parcel): Lista {
            return Lista(parcel)
        }

        override fun newArray(size: Int): Array<Lista?> {
            return arrayOfNulls(size)
        }
    }
}