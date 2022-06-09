package com.finde.android.traincheck.Fragments.stats

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.anychart.AnyChartView
import com.anychart.charts.Cartesian
import com.finde.android.traincheck.Entities.Athlet
import com.finde.android.traincheck.ViewModel.FireBaseReferencies
import com.finde.android.traincheck.ViewModel.GrupoSeleccionado
import com.finde.android.traincheck.ViewModel.TriggerDatosCargados
import com.finde.android.traincheck.ViewModel.VmEstadisticas
import com.finde.android.traincheck.databinding.ActivityChartCommon2Binding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class EffortFragment : Fragment() {

    private lateinit var mBinding: ActivityChartCommon2Binding
    private val grupoSeleccionado: GrupoSeleccionado by viewModels()
    private val datosCargados: TriggerDatosCargados by viewModels()
    private val vmEstadisticas: VmEstadisticas by activityViewModels()

    private lateinit var cartesian: Cartesian
    // private val data: MutableList<DataEntry> = ArrayList<DataEntry>()


    private val map: MutableMap<String, List<String>> = HashMap<String, List<String>>()
    private val formacion: MutableCollection<Athlet> = arrayListOf()
    private var fechas: MutableList<String> = arrayListOf()
    private val altoRendimiento: MutableCollection<Athlet> = arrayListOf()

    private lateinit var barChart: BarChart


    private lateinit var anyChartView2: AnyChartView
    private var mapaSumatorio = HashMap<String, List<Int>>()

    var formatters: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = ActivityChartCommon2Binding.inflate(inflater, container, false)
      //  cartesian = AnyChart.column()

        return mBinding.root

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        barChart = mBinding.barChart

        val formacion: MutableCollection<Athlet> = arrayListOf()
        val altoRendimiento: MutableCollection<Athlet> = arrayListOf()
        //paintGraphics()
        paintAltoRendimineto()
        // loadList()

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

    // El dataEntry solo puede tener un key como float, y mas tarde se le puede asociar un nombre String, asi que hay que guardar todos los string en una lista

    @RequiresApi(Build.VERSION_CODES.N)
    private fun paintAltoRendimineto() {
        val data: MutableList<BarEntry> = ArrayList<BarEntry>()
        var cont : Int =0
        vmEstadisticas.mapaSumatorio.value!!.forEach() { key, value ->
            fechas.add(key)
            cont += 1
            val dataEntry = BarEntry(cont.toFloat(), value[1].toFloat())
            data.add(dataEntry)
        }

        val barDataSet = BarDataSet(data, "")
        barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val barData = BarData(barDataSet)
        barChart.data = barData

        val xAxis = barChart.xAxis
        xAxis.setValueFormatter(IndexAxisValueFormatter(fechas));
        //hide grid lines
        xAxis.setCenterAxisLabels(true)
        // below line is to set position
        // to our x-axis to bottom.
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        // below line is to set granularity
        // to our x axis labels.
        xAxis.granularity = 1f

        // below line is to enable
        // granularity to our x axis.
        xAxis.isGranularityEnabled = true

        // below line is to make our
        // bar chart as draggable.
        barChart.isDragEnabled = true

        // below line is to make visible
        // range for our bar chart.

        // below line is to make visible
        // range for our bar chart.
        barChart.setVisibleXRangeMaximum(7f)

        // below line is to add bar
        // space to our chart.
        val barSpace = 0.1f

        // below line is use to add group
        // spacing to our bar chart.
        val groupSpace = 0.5f

        // we are setting width of
        // bar in below line.
        barData.setBarWidth(0.35f)


        // below line is to set minimum
        // axis to our chart.
        barChart.xAxis.axisMinimum = 0f


        // below line is to
        // animate our chart.
        barChart.animate()


        // below line is to group bars
        // and add spacing to it.
       // todo barChart.groupBars(0f, groupSpace, barSpace)

        //draw chart
        barChart.invalidate()

    }

/*
    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            Log.d(TAG, "getAxisLabel: index $index")
            return if (index <  vmEstadisticas.mapaSumatorio.value!!.size) {
                fechas[index]
            } else {
                ""
            }
        }
    }*/


    /* @RequiresApi(Build.VERSION_CODES.O)
    private fun paintAltoRendimineto() {

        // el id del mapa es el dia de la medida, y cada valor es la lista es la respues a cada pregunta, si quiero saber respuesta a una pregunta, miro solo un valor de la lista

        //vamos a mostrar solo una grafica
        vmEstadisticas.mapaSumatorio.value!!.forEach() { key, value ->
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
        *//* cartesian.xAxis(0).title("Product")
         cartesian.yAxis(0).title("Revenue")*//*
        anyChartView.setChart(cartesian)

    }*/


    @RequiresApi(Build.VERSION_CODES.O)
    private fun crearHashMap(): HashMap<String, List<Int>> {
        val tamanio: Int = 14

        var fecha = LocalDate.of(2022, 4, 6)
        //fehcita.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        for (i in 0..tamanio) {
            val fechaInsertar = fecha.minusDays(i.toLong())

            //var fechaFormateada = fecha.format(formatters)
            mapaSumatorio[fechaInsertar.toString()] = arrayListOf()
            //Random().nextInt(6-1)+1,Random().nextInt(6-1)+1,Random().nextInt(6-1)+1,Random().nextInt(6-1)+1,Random().nextInt(6-1)+1
        }
        return mapaSumatorio
    }

}




