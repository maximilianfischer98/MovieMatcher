package com.example.moviematcher.navigationbar.friends

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FriendsModel(
    var username: String? ="" ) : Parcelable