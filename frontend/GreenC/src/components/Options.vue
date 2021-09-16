<template>
<div>

  <!--Input must not have an :id because 'v-for' used in General for the ConfigurationSelection-->
  <form @submit.prevent="handleSubmit">
    <div class="form-check" v-for="binaryOption in binaryOptionsList" v-bind:key="binaryOption.name">
      <label>
        <input type="checkbox"
               v-model="selectedBinaryOptions"
               :value="binaryOption.name"
               @change="$emit('selectedBinaryOptionsChanged',selectedBinaryOptions)">
      </label>
      <label class="form-check-label" :for="binaryOption.name">{{binaryOption.name}}</label>
    </div>
    <div class="form-check" v-for="numericOption in numericOptionsList" v-bind:key="numericOption.name">
      <form>
        <label>
          <input type="number"
                 v-model="selectedNumericOptions[numericOption.name]"
                 :name="numericOption.name"
                 :min="numericOption.minValue"
                 :max="numericOption.maxValue"
                 @change="$emit('selectedNumericOptionsChanged',selectedNumericOptions)">
        </label>
        <label :for="numericOption.name">{{numericOption.name}} || MinValue: {{numericOption.minValue}} MaxValue: {{numericOption.maxValue}} StepFunction: {{ numericOption.stepFunction}}</label>
      </form>
    </div>

<!--
    <div class="form-group">
      <button class="btn btn-primary" v-on:click="$emit('sendConfiguration',selectedBinaryOptions,selectedNumericOptions)">evaluate configuration</button>
    </div>
-->
  </form>
</div>
</template>

<script>
//import json from './options.json'

export default {
  name: "Options",
  data() {
    return {
      selectedBinaryOptions: [],                           //currently selected binary Options
      selectedNumericOptions: {},                          //currently selected numeric Options
    };
  },
  props:[
    "numericOptionsList",                                 //List with numeric Options for the currently selected SoftwareSystem
    "binaryOptionsList",                                  //List with binary Options for the currently selected SoftwareSystem
    "pickedSoftwareSystem"
  ],
  mounted() {
    this.selectedBinaryOptions = this.getMandatoryBinaryOptions();
    this.getMandatoryNumericOptions()
  },
  methods: {
    checkBoxesForAlternativeConfiguration:function (binaryOptionsAlternativeConfiguration,numericOptionsAlternativeConfiguration){         //if a suggested configuration gets chosen by the user , updates the checkboxes for the new configuration and sets valus
      this.selectedBinaryOptions = binaryOptionsAlternativeConfiguration
      this.selectedNumericOptions= numericOptionsAlternativeConfiguration
    },
    getMandatoryBinaryOptions: function (){
      let tempArray = []
      for(let i = 0 ;i<this.binaryOptionsList.length;i++) {
        if(!this.binaryOptionsList[i].isOptional && this.binaryOptionsList[i].excludedOptions.length === 0){
          tempArray.push(this.binaryOptionsList[i].name)

        }
      }
      return tempArray
    },
    getMandatoryNumericOptions: function (){
      for(let i = 0; i < this.numericOptionsList.length; i++) {
        this.selectedNumericOptions[this.numericOptionsList[i].name]= this.numericOptionsList[i].minValue.toString()
      }
      this.$emit('selectedNumericOptionsChanged',this.selectedNumericOptions)
    }
  },
  watch:{
    pickedSoftwareSystem(){
      this.selectedBinaryOptions = this.getMandatoryBinaryOptions();
      this.selectedNumericOptions = {};
    },
    selectedBinaryOptions(){
      this.$emit('selectedBinaryOptionsChanged',this.selectedBinaryOptions)
    },
    selectedNumericOptions(){
      this.$emit('selectedNumericOptionsChanged',this.selectedNumericOptions)
    },
   numericOptionsList(){                                                             //sets minvalues for numeric options
     for(let i = 0; i < this.numericOptionsList.length; i++) {
       this.selectedNumericOptions[this.numericOptionsList[i].name]= this.numericOptionsList[i].minValue.toString()
      }

    }
  }
}
</script>

<style scoped>

</style>
