<template>
  <div>

    <h2 style="text-align: center">Detailed Analysis</h2>
    <p style="text-align: center">Choose one or more properties to see their behavior in detail.</p>
    <!-- Showing all available properties for the chosen software system -->
    <div v-for="property in propertiesOfSoftwareSystem"
         v-bind:key="property">
      <label>
        <input type="checkbox"
               v-model="pickedPropertiesForDetailedAnalysis"
               :id="property"
               :value="property">
      </label>
      <label class="form-check-label" :for="property">{{ property }}</label>
    </div>


    <DetailedChartsOfOneProperty v-bind:pickedPropertiesForDetailedAnalysis="pickedPropertiesForDetailedAnalysis"
                                 v-bind:allResponsesOfAllConfigurations="allResponsesOfAllConfigurations"
                                 v-bind:amountOfConfigurations="amountOfConfigurations"
                                 v-bind:pickedSoftwareSystem="pickedSoftwareSystem"
                                 v-bind:propertiesOfSoftwareSystem="propertiesOfSoftwareSystem"
                                 v-bind:binaryOptionsList="binaryOptionsList"
                                 v-bind:numericOptionsList="numericOptionsList"
                                 v-bind:SoftwareSystemsData="SoftwareSystemsData"/>
  </div>
</template>


<script>
import DetailedChartsOfOneProperty from "./DetailedChartsOfOneProperty"

export default {
  name: "DetailedAnalysis",
  components: {
    DetailedChartsOfOneProperty,
  },
  props: [
    "pickedSoftwareSystem",
    "propertiesOfSoftwareSystem",
    "propertyForDetailedAnalysis",
    "amountOfConfigurations",
    "allResponsesOfAllConfigurations",
    "binaryOptionsList",
    "numericOptionsList",
    "SoftwareSystemsData"
  ],
  data: function () {
    return {
      pickedPropertiesForDetailedAnalysis: [] //contains those properties that are shown for the Detailed Analysis
    }
  },

}
</script>


<style scoped>

</style>