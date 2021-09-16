<template>
  <div>
    <h2 style="text-align: center">Radar Chart</h2>
    <p style="text-align: center">Properties in % to maximum value of that property</p>
    <div id="chart">
      <apexchart type="radar" height="500" :options="chartOptions" :series="series"></apexchart>
    </div>
  </div>

</template>

<script>

export default {
  name: "RadarChart",

  props: [
    'propertiesOfSoftwareSystem',
    'amountOfConfigurations',
    'allResponsesOfAllConfigurations',
    'maxValueData',
  ],
  computed: {
    cloneone_MaxValueData() {
      return JSON.parse(JSON.stringify(this.maxValueData))
    },
    clonetwo_MaxValueData() {
      return JSON.parse(JSON.stringify(this.maxValueData))
    },
    series() {
      let dataSeriesForRadarChart = []
      for (let i = 0; i < this.amountOfConfigurations.length; i++) {
        let propertyValuesOfConfigurationX = []
        for (let property of this.propertiesOfSoftwareSystem) {
          for (let j = 0; j < this.maxValueData.properties.length; j++) {
            if (this.maxValueData.properties[j].name === property) {
              propertyValuesOfConfigurationX.push(this.trimNumbers((100 * this.allResponsesOfAllConfigurations[i].estimation.systemValues[property]) / this.scaledMaxValueData.properties[j].maxValue))  //calculates the % of MaxValue
            }
          }
        }
        dataSeriesForRadarChart.push({"name": this.amountOfConfigurations[i], data: propertyValuesOfConfigurationX})
      }
      return dataSeriesForRadarChart
    },
    scaledMaxValueData() {
      let tempScaledDataConfigOne = this.cloneone_MaxValueData
      let tempScaledDataConfigTwo = this.clonetwo_MaxValueData
      let tempScaledDataArray = [tempScaledDataConfigOne, tempScaledDataConfigTwo]
      for (var property in this.maxValueData.properties) {
        let scaledValue = this.maxValueData.properties[property].maxValue
        if (this.maxValueData.properties[property].maxValueScalingFactors !== {}) {
          for (let k = 0; k < this.allResponsesOfAllConfigurations.length; k++) {
           for (const [keymaxValueScalingFactors, valuemaxValueScalingFactors] of Object.entries(this.maxValueData.properties[property].maxValueScalingFactors)) {
             for (const [keyallResponsesOfAllConfigurations, valueResponsesOfAllConfigurations] of Object.entries(this.allResponsesOfAllConfigurations[k].numericOptionValues)) {
               if (keymaxValueScalingFactors === keyallResponsesOfAllConfigurations) {
                 scaledValue = (valuemaxValueScalingFactors * valueResponsesOfAllConfigurations) + scaledValue
               }
             }
           }
              tempScaledDataArray[k].properties[property].maxValue = scaledValue
          }
        }
      }
      let tempScledDataArrayFinalValues = JSON.parse(JSON.stringify(tempScaledDataArray[0]))
      if (this.allResponsesOfAllConfigurations.length === 2) {
        for (var propertyTest in this.maxValueData.properties) {
            if (tempScaledDataArray[0].properties[propertyTest].maxValue <= tempScaledDataArray[1].properties[propertyTest].maxValue) {
              tempScledDataArrayFinalValues.properties[propertyTest].maxValue = tempScaledDataArray[1].properties[propertyTest].maxValue
            } else {
              tempScledDataArrayFinalValues.properties[propertyTest].maxValue = tempScaledDataArray[0].properties[propertyTest].maxValue
          }
        }
      }
      return tempScledDataArrayFinalValues
    },
    chartOptions() {
      return {
        chart: {
          id: 'RC',
          foreColor: "#304758",

          height: 500, //350 or '400px' or '100%' or 'auto'
          //parentHeightOffset: 0,
          type: 'radar',
          toolbar: {
            show: false
          },
          dropShadow: {
            enabled: true,
            blur: 1,
            left: 1,
            top: 1
          },
          offsetY: -60, //Sets the top offset for the entire chart.
        },
        legend:{
          offsetY: -100,
          height: 50
        },
        title: {
          text: '',
        },
        stroke: {
          width: 2
        },
        fill: {
          opacity: 0.1
        },
        markers: {
          size: 4 //Hover over a marker to see the value displayed
        },
        xaxis: {
          type: 'category',
          categories: this.propertiesOfSoftwareSystem,
          //tickPlacement: 'on',
          labels: {
            show: true,
          },
          //convertedCatToNumeric: false
        }
      }
    }
  },
watch:{

},
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

}
</script>


<style scoped>
</style>