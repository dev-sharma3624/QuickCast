package com.example.quickcast.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.wavechart.model.AxisPosition
import com.example.quickcast.enum_classes.SmsTypes
import com.example.quickcast.ui.theme.individualSiteBg
import com.example.quickcast.viewModels.ChartsVM
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChartsScreen(){

    val viewModel : ChartsVM = koinViewModel()

    val tag = "ChartsScreen"

    Log.d(tag, "composable")


    Column(
        modifier = Modifier.fillMaxSize()
            .padding(24.dp)
    ) {

        val initialSetup by viewModel.initialSetup

        Log.d(tag, "$initialSetup")

        if(initialSetup){

            val siteList = viewModel.siteList.collectAsState(emptyList())
            val selectedSite = remember { mutableIntStateOf(0) }
            var isSiteDropDownExpanded by remember { mutableStateOf(false) }

            val formatList = viewModel.formatList.collectAsState(emptyList())
            val selectedFormat = remember { mutableIntStateOf(0) }
            var isFormatDropDownExpanded by remember { mutableStateOf(false) }

            val messageList = viewModel.messageList.collectAsState(emptyList())

            Column {

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(4.dp)
                        .background(
                            individualSiteBg
                        )
                        .padding(4.dp)
                        .clickable {
                            isSiteDropDownExpanded = true
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if(siteList.value.isNotEmpty()){
                            siteList.value[selectedSite.intValue].name
                        }else { "" },
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = isSiteDropDownExpanded,
                    onDismissRequest = { isSiteDropDownExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    siteList.value.forEach {
                        DropdownMenuItem(
                            text = { Text(
                                text = it.name
                            ) },
                            onClick = {
                                selectedSite.intValue = siteList.value.indexOf(it)
                                isSiteDropDownExpanded = false
                                viewModel.loadFormats(it.id)
                            },
                        )
                    }
                }
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(4.dp)
                        .background(
                            individualSiteBg
                        )
                        .padding(4.dp)
                        .clickable {
                            isFormatDropDownExpanded = true
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {


                    Text(
                        text = if(formatList.value.isNotEmpty()){
                            formatList.value[selectedFormat.intValue].taskName
                        }else { "" }
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = isFormatDropDownExpanded,
                    onDismissRequest = { isFormatDropDownExpanded = false },
                    modifier = Modifier.fillMaxWidth(0.7f)
                ) {
                    formatList.value.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.taskName) },
                            onClick = {
                                selectedFormat.intValue = formatList.value.indexOf(it)
                                isFormatDropDownExpanded = false
                                viewModel.loadMessages(it.formatId, it.siteId)
                            }
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                        .weight(1f)
                ) {
                    item {

                        Log.d(tag, "${messageList.value}")

                        if(messageList.value.isNotEmpty()){

                            val firstMsg = messageList.value.first { it.smsType == SmsTypes.CREATE_TASK }

                            val keyList = firstMsg.content.map { it.k }
                            val contactList = messageList.value.filter { it.sentBy != "SELF" }.map { it.sentBy }

                            keyList.forEach {specificKey ->

                                val maxValueForSpecificKey = messageList.value.flatMap { it.content }
                                    .filter { it.k ==  specificKey}.maxOf { it.v }

                                val yAxisData = contactList.map { contact ->

                                    val allValues = messageList.value.filter { it.sentBy == contact }
                                        .flatMap { it.content }.filter { it.k == specificKey }.map { it.v }

                                    val calculation = allValues.sum() / 8

                                    BarData(
                                        point = Point(calculation.toFloat(), contactList.indexOf(contact).toFloat()),
                                        label = contact.substring(4,9),
                                        color = Color.DarkGray
                                    )
                                }

                                Log.d(tag, "${maxValueForSpecificKey/5}")
                                Log.d(tag, "$yAxisData")

                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = specificKey,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp,
                                        color = Color.DarkGray
                                    )

                                    MyBarChart(
                                        xAxisStepSize = maxValueForSpecificKey / 5,
                                        yAxisData = yAxisData
                                    )
                                }

                            }
                        }


                    }
                }
            }

        } else{

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }

        }

    }

}

@Composable
fun MyBarChart(
    xAxisStepSize: Int,
    yAxisData: List<BarData>
) {

    val yAxis = AxisData.Builder()
        .axisStepSize(8.dp)
        .startDrawPadding(4.dp)
        .steps(yAxisData.size - 1)
        .labelData { i -> yAxisData[i].label }
        .axisLineColor(Color.Gray)
        .axisLabelColor(Color.DarkGray)
        .backgroundColor(Color.White)
        .labelAndAxisLinePadding(20.dp)
        .setDataCategoryOptions(DataCategoryOptions(
            true
        ))
        .build()

    val xAxis = AxisData.Builder()
        .steps(5)
        .axisOffset(0.dp)
        .labelData { i -> (i * xAxisStepSize).toString() }
        .axisLineColor(Color.Gray)
        .axisLabelColor(Color.Gray)
        .backgroundColor(Color.White)
        .build()

    val barChartData = BarChartData(
        chartData = yAxisData,
        xAxisData = xAxis,
        yAxisData = yAxis,
        backgroundColor = Color.White,
        barChartType = BarChartType.HORIZONTAL
    )

    BarChart(
        modifier = Modifier.height((67 * yAxisData.size).dp),
        barChartData = barChartData
    )

    /*// 1. Define the data points for your bar chart
    val barData = listOf(
        BarData(Point(0f, 40f), label = "Q1"),
        BarData(Point(1f, 90f), label = "Q2"),
        BarData(Point(2f, 20f), label = "Q3"),
        BarData(Point(3f, 60f), label = "Q4")
    )

    // 2. Configure the X-axis
    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .steps(barData.size - 1)
        .labelData { i -> barData[i].label }
        .axisLineColor(Color.Gray)
        .axisLabelColor(Color.Gray)
        .build()

    // 3. Configure the Y-axis
    val yAxisData = AxisData.Builder()
        .steps(5) // 5 labels on the Y-axis
        .labelData { i -> (i * 20).toString() } // e.g., 0, 20, 40, 60, 80, 100
        .axisLineColor(Color.Gray)
        .axisLabelColor(Color.Gray)
        .build()

    // 4. Combine all the data into a BarChartData object
    val barChartData = BarChartData(
        chartData = barData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = Color.White,
    )

    // 5. Call the BarChart composable with the data
    BarChart(
        modifier = Modifier.height(300.dp),
        barChartData = barChartData
    )*/
}