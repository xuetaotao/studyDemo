package com.example.studydemo.utils

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import java.io.Externalizable
import java.io.File
import java.io.ObjectInput
import java.io.ObjectOutput
import java.io.Serializable


open class Person(name: String, age: String) : Serializable {
    companion object {
        const val serialVersionUID: Long = 0x23242L
    }
}

class Person2(name: String, age: String) : Externalizable {
    override fun writeExternal(out: ObjectOutput?) {

    }

    override fun readExternal(`in`: ObjectInput?) {
    }
}

/**
 * Parcelable是Android SDK提供的，他是基于内存的，由于内存读写速度高于硬盘，因此Android中的跨进程对象的传递一般使用Parcelable
 */
class Pet() : Parcelable {
    var name: String? = null
    private var year: Double = 0.0

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        year = parcel.readDouble()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(year)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pet> {
        override fun createFromParcel(parcel: Parcel): Pet {
            return Pet(parcel)
        }

        override fun newArray(size: Int): Array<Pet?> {
            return arrayOfNulls(size)
        }
    }
}


class Student(name: String, age: String, score: String) : Person(name, age)


/**
 * [
 *     {
 *         "destinationName": "US",
 *         "iconInfo": "infous",
 *         "curOrderModelId": "23123_sadsa_dasd2",
 *         "provider": "Truth",
 *         "networks": [
 *             "aa",
 *             "bb",
 *             "cc"
 *         ],
 *         "apn": "apninfo"
 *     },
 *     {
 *         "destinationName": "CH",
 *         "iconInfo": "infoch",
 *         "curOrderModelId": "7897_sgas_27fs",
 *         "provider": "Truth",
 *         "networks": [
 *             "dd",
 *             "ee",
 *             "ff"
 *         ],
 *         "apn": "apninfoch"
 *     }
 * ]
 */
data class ESimModel(
    val apn: String?,
    val curOrderModelId: String?,
    val destinationName: String?,
    val iconInfo: String?,
    val networks: List<String?>?,
    val provider: String?
)

@Test
fun test() {
    val file = File("json.txt")
}

//Android Studio自带的org.json
private fun setESimModels(eSimModelList: List<ESimModel>?): String {
    val jsonArray = JSONArray()
    eSimModelList?.forEach { eSimModel ->
        val json = JSONObject()
        try {
            json.put("destinationName", eSimModel.destinationName)
            json.put("iconInfo", eSimModel.iconInfo)
            json.put("curOrderModelId", eSimModel.curOrderModelId)
            json.put("provider", eSimModel.provider)
            val networkJSONArray = JSONArray()
            eSimModel.networks?.forEach { network ->
                networkJSONArray.put(network)
            }
            json.put("networks", networkJSONArray)
            json.put("apn", eSimModel.apn)
            jsonArray.put(json)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return jsonArray.toString()
}


class GsonUtils {

    fun fromJSON(): String {
        val gson = Gson()
        return gson.toJson("aa")
    }
}