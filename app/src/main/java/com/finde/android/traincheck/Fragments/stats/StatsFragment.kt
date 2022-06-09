package com.finde.android.traincheck.Fragments.stats

import android.os.Build
import android.os.Bundle
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
import com.finde.android.traincheck.Entities.Athlet
import com.finde.android.traincheck.R
import com.finde.android.traincheck.ViewModel.FireBaseReferencies
import com.finde.android.traincheck.ViewModel.GrupoSeleccionado
import com.finde.android.traincheck.ViewModel.TriggerDatosCargados
import com.finde.android.traincheck.ViewModel.VmEstadisticas
import com.finde.android.traincheck.databinding.FragmentStatsBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.util.*


class StatsFragment : Fragment() {

    private lateinit var navigation: NavController
    private val grupoSeleccionado: GrupoSeleccionado by viewModels()
    private val datosCargados: TriggerDatosCargados by viewModels()

    private val data: MutableList<DataEntry> = ArrayList<DataEntry>()
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

        GlobalScope.launch(Dispatchers.IO) {
            altoRendimiento = loadList()
            withContext(Dispatchers.Main) {
                createStatsSumation(altoRendimiento)
            }
        }

        setUiInformation()

        val formacion: MutableCollection<Athlet> = arrayListOf()
        val altoRendimiento: MutableCollection<Athlet> = arrayListOf()


    }

    private fun setUiInformation() {
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
            navigation.navigate(R.id.action_statsFragment_to_resultsGraphFragment) }

        mBinding.resultados.setOnClickListener {
            vmEstadisticas.name = "Resultados"
            navigation.navigate(R.id.action_statsFragment_to_resultsGraphFragment) }

        mBinding.motivacion.setOnClickListener {
            vmEstadisticas.name = "Motivación"
            navigation.navigate(R.id.action_statsFragment_to_resultsGraphFragment) }

        mBinding.cansancio.setOnClickListener {
            vmEstadisticas.name = "Cansancio"
            navigation.navigate(R.id.action_statsFragment_to_resultsGraphFragment) }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun loadList(): MutableCollection<Athlet> {
            val snapshots =
                FireBaseReferencies.mAtletasRef.get()
                    .await()

            snapshots.children.forEach {
                val atleta = it.getValue(Athlet::class.java)
                if (atleta != null) {

                    if (atleta.group == "Formacion") {
                        formacion.add(atleta)
                    } else {
                        altoRendimiento.add(atleta)
                    }
                }
                datosCargados.datosCargados.postValue(true)
            }
        return altoRendimiento
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createStatsSumation(altoRendimiento: MutableCollection<Athlet>) {
        mapaSumatorio = crearHashMap()

        altoRendimiento.forEach() { atleta ->
            mapaSumatorio.keys.forEach { key ->

                if (atleta.listStats.containsKey(key)) {
                    var lista = mutableListOf<Int>()
                    for (i in 0 until atleta.listStats[key]!!.size) {
                        var final =
                            mapaSumatorio[key]!!.get(i) + atleta.listStats[key]!!.get(i).toInt()
                        lista.set(i, final)
                    }
                    mapaSumatorio.put(key, lista)
                }
            }
        }

        vmEstadisticas.mapaSumatorio.postValue(mapaSumatorio)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun crearHashMap(): HashMap<String, List<Int>> {
        val tamanio: Int = 14

        var fecha = LocalDate.of(2022, 4, 6)
        //fehcita.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        for (i in 0..tamanio) {
            val fechaInsertar = fecha.minusDays(i.toLong())

            //var fechaFormateada = fecha.format(formatters)
            mapaSumatorio[fechaInsertar.toString()] = arrayListOf(
                Random().nextInt(6 - 1) + 1,
                Random().nextInt(6 - 1) + 1,
                Random().nextInt(6 - 1) + 1,
                Random().nextInt(6 - 1) + 1,
                Random().nextInt(6 - 1) + 1
            )

        }
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