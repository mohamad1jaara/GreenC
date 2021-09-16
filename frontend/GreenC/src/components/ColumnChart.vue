<template>
  <div id="chart">
    <h2 style="text-align: center">{{this.propertyForDetailedAnalysis}}  in {{findPropertiesUnit(propertyForDetailedAnalysis)}}</h2>
    <apexchart type="bar" height="350" :options="chartOptionsColumnChart" :series="seriesColumnChart"></apexchart>
  </div>
</template>



<script>
export default {
  name: "ColumnChart",
  props:[
    "amountOfConfigurations",
    "allResponsesOfAllConfigurations",
    "propertyForDetailedAnalysis",
    "pickedSoftwareSystem",
    "SoftwareSystemsData"
  ],
  methods:{
    /**
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

    },
    findPropertiesUnit: function (key) {
      let properties;
      if (this.pickedSoftwareSystem === "HSQLDB") {
        properties = this.SoftwareSystemsData[0].properties;
      } else if (this.pickedSoftwareSystem === "7-Zip"){
        properties = this.SoftwareSystemsData[1].properties;
      }
      for (let i = 0; i < properties.length ; i++){
        if(properties[i].name === key){
          return properties[i].unit
        }
      }
    },
  },
  computed: {
    seriesColumnChart() {
      let positiveInfluences = []
      let negativeInfluences = []
      let total = []
      for(let c = 0; c < this.amountOfConfigurations.length; c++){
        let sumOfPositiveInfluences = 0
        let sumOfNegativeInfluences = 0
        let array = this.allResponsesOfAllConfigurations[c].estimation.partValues
        for (let i = 0; i < array.length; i++) {
          for (const [key, value] of Object.entries(array[i].values)) {
            if (key === this.propertyForDetailedAnalysis) {
              if (value > 0) {
                sumOfPositiveInfluences += value
              }
              if (value < 0) {
                sumOfNegativeInfluences += value
              }
            }
          }
        }
        total.push(this.trimNumbers(sumOfPositiveInfluences + sumOfNegativeInfluences))
        sumOfPositiveInfluences=this.trimNumbers(sumOfPositiveInfluences)
        sumOfNegativeInfluences=(-1)*(this.trimNumbers(Math.abs(sumOfNegativeInfluences)))
        positiveInfluences.push(sumOfPositiveInfluences)
        negativeInfluences.push(sumOfNegativeInfluences)

      }
      return [{
        name: "positive influences",
        data: positiveInfluences
      }, {
        name: "negative influences",
        data: negativeInfluences
      }, {
        name: "total " + this.propertyForDetailedAnalysis,
        data: total
      }]
    },
    chartOptionsColumnChart() {
      return {
        chart: {
          type: 'bar',
          height: 350,
          toolbar: {
            show: false
          }
        },
        colors: ['#48C9B0', '#EC7063', '#3498DB'], //colors for the chart’s series
        fill:{
          colors: ['#48C9B0', '#EC7063', '#3498DB'], //colors for the 3 columns
          opacity: 1
        },
        dataLabels: { //for showing numbers inside a bar
          enabled: true,
          //textAnchor: "start", //horizontal middle
          //position: 'top',
          offsetY: 0,
          style: {
            colors: ["#304758"]
          }
        },
        tooltip: { //box, that appears when mouse hovers above a column
          enabled: false,
          enabledOnSeries: [0,1,2], //shows tooltip for pos., neg. and total
          marker: {
            show: false
          },
        },
        plotOptions: {
          bar: {
            horizontal: false,
            columnWidth: '55%',
            dataLabels: {
              position: 'center', // top, center, bottom
            },
          },
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          categories: this.amountOfConfigurations //['Configuration 1'],
        },
        yaxis: {
          decimalsInFloat: 0,
          labels:{
            show: true,
            align: 'center'
          },
          title: {
            text: this.propertyForDetailedAnalysis,
            rotate: -90,
            offsetX: 0,
            offsetY: 0
          }
        }
      }
    }
  },
}
</script>



<style scoped>

</style>