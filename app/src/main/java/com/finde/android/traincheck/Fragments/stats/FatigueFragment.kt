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


class FatigueFragment : Fragment() {

    private lateinit var mBinding: ActivityChartCommon2Binding
    private val vmEstadisticas: VmEstadisticas by activityViewModels()
    private var fechas: MutableList<String> = arrayListOf()
    private lateinit var barChart: BarChart

    var formatters: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = ActivityChartCommon2Binding.inflate(inflater, container, false)


        return mBinding.root

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        barChart = mBinding.barChart
        paintAltoRendimineto()
    }

    // El dataEntry solo puede tener un key como float, y mas tarde se le puede asociar un nombre String, asi que hay que guardar todos los string en una lista

    @RequiresApi(Build.VERSION_CODES.N)
    private fun paintAltoRendimineto() {
        val data: MutableList<BarEntry> = ArrayList<BarEntry>()
        var cont: Int = 0
        vmEstadisticas.mapaSumatorio.value!!.forEach() { key, value ->
            fechas.add(key)
            cont += 1
            val dataEntry = BarEntry(cont.toFloat(), value[2].toFloat())
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


}




