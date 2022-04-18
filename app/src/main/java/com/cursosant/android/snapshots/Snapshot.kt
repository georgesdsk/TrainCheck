package com.cursosant.android.snapshots

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

/****
 * Project: Snapshots
 * From: com.cursosant.android.snapshots
 * Created by Alain Nicolás Tello on 12/17/20 at 3:48 PM
 * Course: Android Practical with Kotlin from zero.
 * All rights reserved 2021.
 *
 * All my Courses(Only on Udemy):
 * https://www.udemy.com/user/alain-nicolas-tello/
 ***/
@IgnoreExtraProperties
data class Snapshot(@get:Exclude var id: String = "",
                    var title: String = "",
                    var photoUrl: String ="",
                    var likeList: Map<String, Boolean> = mutableMapOf())
