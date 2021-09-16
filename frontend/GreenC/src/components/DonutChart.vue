<template>
  <div id="chart">
    <apexchart type="donut" :options="chartOptionsDonutChart" :series="seriesDonutChart" v-if="this.noInfluences === false"></apexchart>
    <p class="noInfluence" v-if="this.noInfluences === true">No {{ this.typeOfInteraction }} Influences for {{ this.propertyForDetailedAnalysis }}</p>
  </div>
</template>


<script>
export default {
  name: "DonutChart",
  props: [
    "configuration",
    "typeOfInteraction",
    "positiveNegativeInfluences",
    "propertyForDetailedAnalysis"
  ],
  methods:{
    /*
    Filter to trim down numbers to 2 non-zero digits after ',', returns a float
    */
    trimNumbers: function (number){
      number= number.toString()
      let foundDot = false
      let numbersbeforeDot = false
      let postionToCut = 1
      let exponent = "e+0"
      for(let j =0;j<number.length; j++){
          if(number.charAt(j)==="e"){
             exponent =(number.slice(j,number.length))
          }
      }
      for(let i =0;i<number.length; i++){
        if(number.charAt(i)!=="0" && number.charAt(i)!=="." && !foundDot)
        {
          numbersbeforeDot= true
        }
        else if(number.charAt(i)==="."){
          foundDot = true
          if(numbersbeforeDot){
            postionToCut=i+3
            break
          }
        }
        else{
          if(foundDot){
            if(number.charAt(i)!=="0"){
              postionToCut = i+2
              break
            }
          }
        }
      }
      return parseFloat(number.slice(0,postionToCut)+exponent)

    }
  },

  computed: {
    configurationNumber() {
      return this.configuration.slice(-1) //get the last Char of the String (example: "2" in the String "Configuration 2")
    },
    configurationX_responseOfHTTPRequest() {
      if (this.allResponsesOfAllConfigurations.length !== 0) {
        return this.allResponsesOfAllConfigurations[this.configurationNumber - 1]
      }
      return {}
    },
    //the array must be cloned, otherwise the operations would change the original array in the General component
    //REFERENCES!!!
    clone_configurationX_positiveNegativeInfluences() {
      return JSON.parse(JSON.stringify(this.positiveNegativeInfluences[this.configurationNumber - 1]))
    },
    influencesForRelevantProperty() {
      //extracting only the influences of the relevant property
      let solution = []
      for (let p = 0; p < this.clone_configurationX_positiveNegativeInfluences.length; p++) {
        if (this.clone_configurationX_positiveNegativeInfluences[p][0] === this.propertyForDetailedAnalysis) {
          solution = this.clone_configurationX_positiveNegativeInfluences[p]
        }
      }
      return solution
    },
    noInfluences(){
      //for returning message: "No positive/negative Influences for this property"
      if(this.typeOfInteraction === 'positive'){
        if(this.influencesForRelevantProperty[1][0].length === 0){
          return true
        }else{
          return false
        }
      }else{
        if(this.influencesForRelevantProperty[2][0].length === 0){
          return true
        }else{
          return false
        }
      }
    },
    seriesDonutChart() {
      //donut chart can not show negative values
      //1.step: manipulate all negative values with Math.abs() function
      //2.step: configure the tooltip and plotOptions, so that negative values are displayed
      if (this.typeOfInteraction === "negative") {
        let arrayWithSeriesForDonutChart = this.influencesForRelevantProperty[2][0]
        for (let i = 0; i < arrayWithSeriesForDonutChart.length; i++) {
          arrayWithSeriesForDonutChart[i] = this.trimNumbers(Math.abs(arrayWithSeriesForDonutChart[i]))
        }
        return arrayWithSeriesForDonutChart
      }
      else{
        let arrayWithSeriesForDonutChart = this.influencesForRelevantProperty[1][0]
        for (let i = 0; i < arrayWithSeriesForDonutChart.length; i++) {
          arrayWithSeriesForDonutChart[i] = this.trimNumbers(Math.abs(arrayWithSeriesForDonutChart[i]))
        }
        return arrayWithSeriesForDonutChart
      }
    },
    chartOptionsDonutChart() {
      //configure donut chart for positive/negative cases
      let typeOfInteraction = this.typeOfInteraction //must be locally defined for sub-functions to work
      //let donutChartTitle = ""
      let total = 0.0 //numeric value in the middle of the donut
      let label = [] //involved options, will be shown in the legend
      let textCenter = "" //text in the middle of the donut
      if (this.typeOfInteraction === "positive") {
        //donutChartTitle = "Interactions with positive Influences"
        total = this.trimNumbers(this.influencesForRelevantProperty[1][0].reduce((a, b) => a + b, 0)) //this function calculates the sum of all numbers in the array
        var notEditedlabel = this.influencesForRelevantProperty[1][1];
        var editedLabelA = [];
        for (var x = 0; x < notEditedlabel.length; x++) { // this for-schleife is to change the label to be showen in one line
          editedLabelA.push(notEditedlabel[x].replace(/(<([^>]+)>)/ig, " , "));
          editedLabelA[x] = editedLabelA[x].slice(0, editedLabelA[x].length - 2);
        }
        label = editedLabelA;
        textCenter = "positive Influences"
      }
      if (this.typeOfInteraction === "negative") {
        //donutChartTitle = "Interactions with negative Influences"
        total = this.trimNumbers(this.influencesForRelevantProperty[2][0].reduce((a, b) => a + b, 0) * (-1.0))
        notEditedlabel = this.influencesForRelevantProperty[2][1];
        var editedLabelB = [];
        for (var i = 0; i < notEditedlabel.length; i++) {// this for-schleife is to change the label to be showen in one line
          editedLabelB.push(notEditedlabel[i].replace(/(<([^>]+)>)/ig, " , "));
          editedLabelB[i] = editedLabelB[i].slice(0, editedLabelB[i].length - 2);
        }
        label = editedLabelB;
        textCenter = "negative Influences"
      }

      return {
        chart: {
          //height: 400, //valid values: auto, 400, 400px, 100% (percentage value '100%', make sure to have a fixed height parent)
          type: 'donut',
          offsetY: 0, //Sets the top offset for the entire chart.
          redrawOnParentResize: true
        },
        colors: [function () { //colors for the series in the legend
          if (typeOfInteraction === "positive") {
            return '#48C9B0'
          }
          if (typeOfInteraction === "negative") {
            return '#EC7063'
          }
        }],
        dataLabels: {
          style: {
            colors: ["#304758"]
          },
          dropShadow: {
            enabled: false,
          }
        },
        labels: label,
        legend: {
          formatter: function (seriesName) {
            return seriesName
          },
          height: 100,
          offsetY: 0,
          floating: false, //takes out the legend from the chart area and make it float above the chart.

          labels: {

            useSeriesColors: true
          },
          position: 'bottom', //available: top, right, bottom, left
          horizontalAlign: 'left', //available: left, center, right
          show: false,
        },
        title: {
          //text: donutChartTitle,
          //show: false
        },
        fill: { //defines fill colors of the donut chart
          colors: [
            function () {
              if (typeOfInteraction === "positive") {
                return '#48C9B0'
              }
              if (typeOfInteraction === "negative") {
                return '#EC7063'
              }
            }
          ]
        },
        plotOptions: { //displayed content in the middle of the donut chart
          pie: {
            customScale: 1,
            donut: {
              size: '65%', //dinner donut radius (standard: 65%)
              labels: {
                show: true,
                total: { //total value displayed in the middle of the donut chart
                  label: textCenter,
                  showAlways: true,
                  show: true,
                  //shows the numeric value in the middle of the donut chart
                  //negative values became positive, so the total value for negative influences is multiplied with (-1)
                  formatter: function () {
                    if (typeOfInteraction === "positive") {
                      return total
                    }
                    if (typeOfInteraction === "negative") {
                      return total * (-1)
                    }
                  }
                },
                value: { //values of the series displayed in the middle of the donut chart
                  formatter: function (value) {
                    if (typeOfInteraction === "positive") {
                      return value
                    }
                    if (typeOfInteraction === "negative") {
                      return value * (-1)
                    }
                  }
                }
              }
            }
          }
        },
        responsive: [{
          breakpoint: 480,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: 'bottom'
            }
          }
        }],
        tooltip: { //for styling the box, that appears when the mouse hovers above the donut chart
          y: {
            formatter: function (yValue) {
              if (typeOfInteraction === "positive") {
                return [yValue]
              }
              if (typeOfInteraction === "negative") {
                return yValue * (-1)
              }
            }
          }
        }
      }
    }
  }
}


</script>


<style scoped>
.noInfluence{
  text-align:center;
  color:#373d3f;
  font-family: Helvetica, Arial, sans-serif;
  font-size: 16px
}
</style>