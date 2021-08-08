package com.example.connectus

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val username: String, val profileimageurl: String) : Parcelable {
    constructor() : this("","","")
}