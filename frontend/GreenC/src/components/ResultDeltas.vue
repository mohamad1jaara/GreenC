<template>
  <div>
    <h3>Difference in %</h3>
    <div v-for="(value,key) in deltaData" v-bind:key="key" >
      {{value}}
 </div>
</div>
</template>

<script>
export default {
  name: "ResultDeltas",
  props: [
    'allResponsesOfAllConfigurations',
  ],

  computed: {
    deltaData() {
      let tempArray = []
      let StringBody = {}
      for (const [keyConfig1, valueConfig1] of Object.entries(this.allResponsesOfAllConfigurations[0].estimation.systemValues)) {
        for (const [keyConfig2, valueConfig2] of Object.entries(this.allResponsesOfAllConfigurations[1].estimation.systemValues)) {
          if (keyConfig1 === keyConfig2) {
            tempArray.push((parseFloat(valueConfig1)) / parseFloat(valueConfig2))
          }
        }
      }
      for (let i = 0; i < tempArray.length; i++) {
        if (tempArray[i] < 1) {
          StringBody[i]=("          "+this.trimNumbers(100 - (tempArray[i] * 100)) + "%  +  -->")
        }
        if (tempArray[i] > 1) {
          StringBody[i]=("<-- +  " + this.trimNumbers((tempArray[i] * 100) - 100) + "%      ")
        }
        if (tempArray[i] === 1) {
          StringBody[i]=("0%")
        }
      }
      return StringBody
    },
  },
  methods: {
    /**
     Filter to trim down numbers to 2 non-zero digits after ',', returns a float
     */
    trimNumbers: function (number) {
      number = number.toString()
      let foundDot = false
      let numbersbeforeDot = false
      let postionToCut = 1
      let exponent = "e+0"
      for (let j = 0; j < number.length; j++) {
        if (number.charAt(j) === "e") {
          exponent = (number.slice(j, number.length))
        }
      }
      for (let i = 0; i < number.length; i++) {
        if (number.charAt(i) !== "0" && number.charAt(i) !== "." && !foundDot) {
          numbersbeforeDot = true
        } else if (number.charAt(i) === ".") {
          foundDot = true
          if (numbersbeforeDot) {
            postionToCut = i + 3
            break
          }
        } else {
          if (foundDot) {
            if (number.charAt(i) !== "0") {
              postionToCut = i + 2
              break
            }
          }
        }
      }
      return parseFloat(number.slice(0, postionToCut) + exponent)

    },
  },
}


</script>

<style scoped>

</style>