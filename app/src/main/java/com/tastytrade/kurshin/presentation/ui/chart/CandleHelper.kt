package com.tastytrade.kurshin.presentation.ui.chart

import android.graphics.Color
import android.graphics.Paint
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.tastytrade.kurshin.domain.Chart

object CandleHelper {

    fun setUpCandleChart(candleStickChart: CandleStickChart, charts: List<Chart>) {
        val candleData = prepareCandleData(charts)
        candleStickChart.data = candleData
        candleStickChart.invalidate()

        candleStickChart.setBackgroundColor(Color.WHITE)
        candleStickChart.description.isEnabled = false
        candleStickChart.setDrawGridBackground(false)
        candleStickChart.setMaxVisibleValueCount(60)
        candleStickChart.setPinchZoom(true)
        candleStickChart.setDrawGridBackground(false)

        val xAxis = candleStickChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(charts.map { it.date })


        val leftAxis = candleStickChart.axisLeft
        leftAxis.setDrawGridLines(false)

        val rightAxis = candleStickChart.axisRight
        rightAxis.isEnabled = false
    }

    private fun prepareCandleData(quotes: List<Chart>): CandleData {
        val candleEntries = quotes.mapIndexed { index, chart ->
            CandleEntry(
                index.toFloat(),
                chart.high.toFloat(),
                chart.low.toFloat(),
                chart.open.toFloat(),
                chart.close.toFloat()
            )
        }

        val candleDataSet = CandleDataSet(candleEntries, "Quotes")
        candleDataSet.color = Color.rgb(80, 80, 80)
        candleDataSet.shadowColor = Color.DKGRAY
        candleDataSet.shadowWidth = 0.7f
        candleDataSet.decreasingColor = Color.RED
        candleDataSet.decreasingPaintStyle = Paint.Style.FILL
        candleDataSet.increasingColor = Color.GREEN
        candleDataSet.increasingPaintStyle = Paint.Style.STROKE
        candleDataSet.neutralColor = Color.BLUE
        candleDataSet.setDrawValues(false)

        return CandleData(candleDataSet)
    }
}