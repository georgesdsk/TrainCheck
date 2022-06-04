package com.finde.android.traincheck.Fragments.stats

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.ui.Tooltip
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.finde.android.traincheck.Entities.Athlet
import com.finde.android.traincheck.ViewModel.FireBaseReferencies
import com.finde.android.traincheck.ViewModel.GrupoSeleccionado
import com.finde.android.traincheck.ViewModel.TriggerDatosCargados
import com.finde.android.traincheck.ViewModel.VmEstadisticas
import com.finde.android.traincheck.databinding.ActivityChartCommon2Binding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class ResultsGraphFragment : Fragment() {

    private lateinit var mBinding: ActivityChartCommon2Binding
    private val grupoSeleccionado: GrupoSeleccionado by viewModels()
    private val datosCargados: TriggerDatosCargados by viewModels()
    private val vmEstadisticas: VmEstadisticas by viewModels()

    private lateinit var cartesian: Cartesian
    private val data: MutableList<DataEntry> = ArrayList<DataEntry>()
    private val map: MutableMap<String, List<String>> = HashMap<String, List<String>>()
    private val formacion: MutableCollection<Athlet> = arrayListOf()
    private val altoRendimiento: MutableCollection<Athlet> = arrayListOf()
    private lateinit var anyChartView: AnyChartView
    private lateinit var anyChartView2: AnyChartView
    private var mapaSumatorio = HashMap<String, List<Int>>()

    var formatters: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = ActivityChartCommon2Binding.inflate(inflater, container, false)
        cartesian = AnyChart.column()

        return mBinding.root

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        anyChartView = mBinding.anyChartView
        anyChartView.setProgressBar(mBinding.progressBar)

        val formacion: MutableCollection<Athlet> = arrayListOf()
        val altoRendimiento: MutableCollection<Athlet> = arrayListOf()
        //paintGraphics()

        setListeners()
        loadList()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadList() {
        CoroutineScope(Dispatchers.IO).launch {
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
        }
    }

    //cutrada pero no sabia cuando terminaba el metodo
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setListeners() {
        datosCargados.datosCargados.observe(viewLifecycleOwner, {
            if (it) {
                paintAltoRendimineto()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun paintAltoRendimineto() {
        //hacer media por dia,
        //insertar dos semanas de entrenamientos
        //recorre el mapa
        mapaSumatorio = crearHashMap()


        altoRendimiento.forEach() { atleta ->
            mapaSumatorio.keys.forEach { key ->

                if (atleta.listStats.containsKey(key)) {
                    var lista = mutableListOf<Int>()
                    for (i in 0 until atleta.listStats[key]!!.size) {
                        var final = mapaSumatorio[key]!!.get(i) + atleta.listStats[key]!!.get(i).toInt()
                        lista.set(i, final)
                    }
                    mapaSumatorio.put(key,lista)
                }
            }
        }

        vmEstadisticas.mapaSumatorio.postValue(mapaSumatorio)

        // el id del mapa es el dia de la medida, y cada valor es la lista es la respues a cada pregunta, si quiero saber respuesta a una pregunta, miro solo un valor de la lista

        //vamos a mostrar solo una grafica
        mapaSumatorio.forEach() { key, value ->
            val dataEntry = ValueDataEntry(key,value[1])
            data.add(dataEntry)
        }

        val column = cartesian.column(data)
        column.tooltip()
            .background("#FFF")
            .position(Position.LEFT_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)

            .fontColor("#5c5c5c")
            .fontSize(12.0)

        cartesian.animation(true)
        cartesian.yScale().minimum(0.0)
        cartesian.yScale().maximum(5.0)
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")
        cartesian.tooltip().positionMode(TooltipPositionMode.CHART)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)
        /* cartesian.xAxis(0).title("Product")
         cartesian.yAxis(0).title("Revenue")*/
        anyChartView.setChart(cartesian)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun crearHashMap(): HashMap<String, List<Int>> {
        val tamanio: Int = 14

        var fecha = LocalDate.of(2022, 4,6 )
        //fehcita.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        for (i in 0..tamanio) {
            val fechaInsertar = fecha.minusDays(i.toLong())

            //var fechaFormateada = fecha.format(formatters)
            mapaSumatorio[fechaInsertar.toString()] = arrayListOf()
            //Random().nextInt(6-1)+1,Random().nextInt(6-1)+1,Random().nextInt(6-1)+1,Random().nextInt(6-1)+1,Random().nextInt(6-1)+1
        }
        return mapaSumatorio
    }

/*
    private fun paintGraphics() {

        data.add(
            ValueDataEntry(
                "Rouge", 80540
            )
        )
        data.add(ValueDataEntry("Foundation", 94190))
        data.add(ValueDataEntry("Mascara", 102610))
        data.add(ValueDataEntry("Lip gloss", 110430))
        data.add(ValueDataEntry("Lipstick", 128000))
        data.add(ValueDataEntry("Nail polish", 143760))
        data.add(ValueDataEntry("Eyebrow pencil", 170670))
        data.add(ValueDataEntry("Eyeliner", 213210))
        data.add(ValueDataEntry("Eyeshadows", 249980))
        val column = cartesian.column(data)
        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.LEFT_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("\${%Value}{groupsSeparator: }")
            .fontColor("#5c5c5c")
        cartesian.animation(true)
        cartesian.title("Esfuerzo promedio de")
        cartesian.yScale().minimum(0.0)
        cartesian.yAxis(0).labels().format("\${%Value}{groupsSeparator: }")
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)
        *//* cartesian.xAxis(0).title("Product")
         cartesian.yAxis(0).title("Revenue")*//*
        anyChartView.setChart(cartesian)
    }*/
}




