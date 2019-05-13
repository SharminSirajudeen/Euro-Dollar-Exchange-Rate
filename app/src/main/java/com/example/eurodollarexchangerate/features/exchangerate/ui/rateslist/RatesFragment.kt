
package com.example.eurodollarexchangerate.features.exchangerate.ui.rateslist

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.view.View
import com.example.eurodollarexchangerate.R
import com.example.eurodollarexchangerate.core.exception.Failure
import com.example.eurodollarexchangerate.core.exception.Failure.NetworkConnection
import com.example.eurodollarexchangerate.core.exception.Failure.ServerError
import com.example.eurodollarexchangerate.core.extension.appContext
import com.example.eurodollarexchangerate.core.extension.failure
import com.example.eurodollarexchangerate.core.extension.observe
import com.example.eurodollarexchangerate.core.extension.viewModel
import com.example.eurodollarexchangerate.core.navigation.Navigator
import com.example.eurodollarexchangerate.core.platform.BaseFragment
import com.example.eurodollarexchangerate.features.exchangerate.RatesListFailure
import com.example.eurodollarexchangerate.features.exchangerate.ui.MyMarkerView
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_rates_chart.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class RatesFragment : BaseFragment() {

    @Inject
    lateinit var navigator: Navigator

    private lateinit var ratesViewModel: RatesViewModel

    override fun layoutId() = R.layout.fragment_rates_chart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        ratesViewModel = viewModel(viewModelFactory) {
            observe(exchangeRates, ::renderExchangeRate)
            failure(failure, ::handleFailure)
        }
    }

    private fun renderExchangeRate(dateRateList: ArrayList<Pair<Date, Double>>?) {
        setData(dateRateList)
        rate_chart.visibility = View.VISIBLE
        progress.visibility = View.GONE
        hideProgress()
    }


    private fun setData(dateRateList: ArrayList<Pair<Date, Double>>?) {
        val entries = ArrayList<Entry>()
        var xAxisLabel = ArrayList<String>()
        if (dateRateList != null) {
            for (index in 0 until dateRateList.size) {
                xAxisLabel.add(index, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(dateRateList.get(index = index).first))
                entries.add(Entry(index.toFloat(), dateRateList.get(index = index).second.toFloat(), dateRateList.get(index = index).first))

            }
        }
        val xAxis = rate_chart.xAxis
        xAxis.enableGridDashedLine(10f, 10f, 0f)
        xAxis.axisMinimum = 0f
        xAxis.setDrawLimitLinesBehindData(true)
        xAxis.position = XAxis.XAxisPosition.BOTH_SIDED
        xAxis.granularity = 1f
        xAxis.valueFormatter = IAxisValueFormatter { value, axis -> xAxisLabel[value.toInt() % xAxisLabel.size] }


        val set1: LineDataSet
        if (rate_chart.data != null && rate_chart.data.dataSetCount > 0) {
            set1 = rate_chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = entries
            rate_chart.data.notifyDataChanged()
            rate_chart.notifyDataSetChanged()
        } else {
            set1 = LineDataSet(entries, "Historical Euro and USD Exchange Rate")
            set1.setDrawIcons(false)
            set1.enableDashedLine(10f, 5f, 0f)
            set1.enableDashedHighlightLine(10f, 5f, 0f)
            set1.color = Color.DKGRAY
            set1.setCircleColor(Color.DKGRAY)
            set1.lineWidth = 1f
            set1.circleRadius = 3f
            set1.setDrawCircleHole(false)
            set1.valueTextSize = 9f
            set1.setDrawFilled(true)
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f
            set1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

            if (Utils.getSDKInt() >= 18) {
                val drawable = ContextCompat.getDrawable(appContext, R.drawable.fade_red)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.DKGRAY
            }
            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1)
            val data = LineData(dataSets)

            var max = "Maximum Rate \n 1€ = ${set1.yMax} USD"
            val limitLine1 = LimitLine(set1.yMax, max)
            limitLine1.lineWidth = 4f
            limitLine1.enableDashedLine(10f, 10f, 0f)
            limitLine1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            limitLine1.textSize = 10f

            var min = "Minimum Rate \n 1€ = ${set1.yMin} USD"
            val limitLine2 = LimitLine(set1.yMin, min)
            limitLine2.lineWidth = 4f
            limitLine2.enableDashedLine(10f, 10f, 0f)
            limitLine2.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            limitLine2.textSize = 10f

            val leftAxis = rate_chart.axisLeft
            leftAxis.removeAllLimitLines()
            leftAxis.addLimitLine(limitLine1)
            leftAxis.addLimitLine(limitLine2)
            leftAxis.enableGridDashedLine(10f, 10f, 0f)
            leftAxis.setDrawZeroLine(true)
            leftAxis.setDrawLimitLinesBehindData(false)

            var description = Description()
            description.isEnabled = false
            rate_chart.description = description
            rate_chart.xAxis.axisMaximum = set1.xMax + 10
            rate_chart.axisRight.isEnabled = false
            rate_chart.axisLeft.axisMinimum = set1.yMin - 0.01f
            rate_chart.axisLeft.axisMaximum = set1.yMax + 0.1f
            rate_chart.setVisibleXRangeMaximum(100f)
            rate_chart.data = data
            rate_chart.animateX(600)
//            rate_chart.animateY(2000, Easing.EaseInCubic)
//            rate_chart.animateXY(2000, 2000)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rate_chart.setTouchEnabled(true)
        val markerView = MyMarkerView(appContext, R.layout.custom_marker_info)
        markerView.chartView = rate_chart
        rate_chart.marker = markerView
        loadRatesList()
    }


    private fun loadRatesList() {
        showProgress()
        progress.visibility = View.VISIBLE
        rate_chart.visibility = View.INVISIBLE
        ratesViewModel.loadRates("2010-01-01", "2019-05-12")
    }


    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is ServerError -> renderFailure(R.string.failure_server_error)
            is RatesListFailure.ListNotAvailable -> renderFailure(R.string.failure_rates_list_unavailable)
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        hideProgress()
        progress.visibility = View.GONE
        notifyWithAction(message, R.string.action_refresh, ::loadRatesList)
    }
}
