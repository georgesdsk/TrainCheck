package com.finde.android.traincheck.Fragments.stats

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Log.INFO
import android.util.Log.WARN
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.anychart.chart.common.dataentry.DataEntry
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.finde.android.traincheck.Entities.Athlet
import com.finde.android.traincheck.R
import com.finde.android.traincheck.DAL.FireBaseReferencies
import com.finde.android.traincheck.ViewModel.GrupoSeleccionado
import com.finde.android.traincheck.ViewModel.VmEstadisticas
import com.finde.android.traincheck.databinding.FragmentStatsBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.util.*
import java.util.logging.Level.INFO
import kotlin.collections.HashMap


class StatsFragment : Fragment() {

    private lateinit var navigation: NavController
    private val grupoSeleccionado: GrupoSeleccionado by viewModels()

    private val formacion: MutableCollection<Athlet> = arrayListOf()
    private var altoRendimiento: MutableCollection<Athlet> = arrayListOf()
    private var mapaSumatorio = HashMap<String, List<Int>>()
    private lateinit var mBinding: FragmentStatsBinding
    private val vmEstadisticas: VmEstadisticas by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentStatsBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveAthlets()
        setUiInformation()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveAthlets() {
        GlobalScope.launch(Dispatchers.IO) {
            altoRendimiento = loadList()
            Log.d("ALTORENDIMIETNO", altoRendimiento.toString())
            withContext(Dispatchers.Main) {
                createStatsSumation(altoRendimiento)
            }
        }
    }

    private fun setUiInformation() {
        var photo = FireBaseReferencies.mFirebaseAuth.currentUser?.photoUrl

       if (photo != null) {
            Glide.with(this)
                .load(photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .into(mBinding.imageButton)
        }

        mBinding.tvName.text = FirebaseAuth.getInstance().currentUser?.displayName
        mBinding.tvEmail.text = FirebaseAuth.getInstance().currentUser?.email
        navigation = Navigation.findNavController(mBinding.root)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        mBinding.btnEncuesta.setOnClickListener {
            navigation.navigate(R.id.actionEncuesta)
        }
        mBinding.btnLogout.setOnClickListener {
            singOut()
        }
        mBinding.esfuerzo.setOnClickListener {
            vmEstadisticas.name = "Esfuerzo"
            navigation.navigate(R.id.action_statsFragment_to_resultsGraphFragment)
        }

        mBinding.resultados.setOnClickListener {
            vmEstadisticas.name = "Resultados"
            navigation.navigate(R.id.action_statsFragment_to_resultsGraphFragment)
        }

        mBinding.motivacion.setOnClickListener {
            vmEstadisticas.name = "Motivación"
            navigation.navigate(R.id.action_statsFragment_to_resultsGraphFragment)
        }

        mBinding.cansancio.setOnClickListener {
            vmEstadisticas.name = "Cansancio"
            navigation.navigate(R.id.action_statsFragment_to_resultsGraphFragment)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadList(): MutableCollection<Athlet> {
        val snapshots =
            FireBaseReferencies.mAtletasRef.get()
                .await()
            formacion.clear()
            altoRendimiento.clear()
        snapshots.children.forEach {
            val atleta = it.getValue(Athlet::class.java)
            if (atleta != null) {

                if (atleta.group == "Formacion") {
                    formacion.add(atleta)
                } else {
                    altoRendimiento.add(atleta)
                }
            }
        }
        return altoRendimiento
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createStatsSumation(altoRendimiento: MutableCollection<Athlet>) {
        mapaSumatorio = crearHashMap()

        altoRendimiento.forEach() { atleta ->
            mapaSumatorio.keys.forEach { key ->

                try {
                    if (atleta.listStats.containsKey(key)) {
                        var lista = mutableListOf<Int>()
                        //Log.d("LISTADO_STATS", atleta.listStats.toString())
                        for (i in 0 until atleta.listStats[key]!!.size) {
                            Log.d("Vuelta " + i, mapaSumatorio[key]?.size!!.toString())

                            var final =
                                if (mapaSumatorio[key]?.size!! < i+1) atleta.listStats[key]!!.get(i) else atleta.listStats[key]!!.get(
                                    i
                                ) + mapaSumatorio[key]!!.get(i)

                            lista.add(i, final)
                        }
                        mapaSumatorio.put(key, lista)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        Log.d("crear sumasion11111", mapaSumatorio.toString())

        var sortedMap =  mapaSumatorio.toSortedMap()
        mapaSumatorio =  sortedMap.toMap(mapaSumatorio)

        Log.d("crear sumasion2", mapaSumatorio.toString())

        vmEstadisticas.mapaSumatorio.postValue( sortedMap)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun crearHashMap(): HashMap<String, List<Int>> {
        val tamanio: Int = 14

        var fecha = LocalDate.of(2022, 6, 4)
        //fehcita.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        for (i in 0..tamanio) {
            val fechaInsertar = fecha.minusDays(i.toLong())

            //var fechaFormateada = fecha.format(formatters)
            mapaSumatorio[fechaInsertar.toString()] = arrayListOf(
                /* Random().nextInt(6 - 1) + 1,
                 Random().nextInt(6 - 1) + 1,
                 Random().nextInt(6 - 1) + 1,
                 Random().nextInt(6 - 1) + 1,
                 Random().nextInt(6 - 1) + 1*/
            )

        }
        Log.d("crear hash mapa", mapaSumatorio.toString())
        return mapaSumatorio
    }


    private fun singOut() {
        context?.let {
            AuthUI.getInstance().signOut(it)
                .addOnCompleteListener {
                    Toast.makeText(context, "Hasta pronto...", Toast.LENGTH_SHORT).show()
                }
        }
    }


}