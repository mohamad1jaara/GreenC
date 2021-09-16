<template>
  <div>
    <b-container fluid id="SelectionArea">
      <b-row class="row justify-content-center optionSelector">
        <b-col sm="5" class="form configbox">
          <Task @sendTask="changeTask"></Task>
        </b-col>
        <b-col sm="5" class="form configbox">
          <SoftwareSystem v-bind:softwareSystemList="softwareSystemList"
                          @sendSoftwarename="changeCurrentSowftwarename"/>
        </b-col>
      </b-row>

      <b-row class="row justify-content-center optionSelector">
        <b-col v-for="configuration in this.amountOfConfigurations" v-bind:key="configuration" sm="5"
               class=" form configbox">
          <ConfigurationSelection v-bind:configuration="configuration"
                                  v-bind:pickedTask="pickedTask"
                                  v-bind:pickedSoftwareSystem="pickedSoftwareSystem"
                                  v-bind:binaryOptionsList="binaryOptionsList"
                                  v-bind:numericOptionsList="numericOptionsList"
                                  v-bind:allResponsesOfAllConfigurations="allResponsesOfAllConfigurations"
                                  @configuration1_selectedBinaryOptionsChanged="changeConfiguration1_binaryOptions"
                                  @configuration1_selectedNumericOptionsChanged="changeConfiguration1_NumericOptions"
                                  @configuration2_selectedBinaryOptionsChanged="changeConfiguration2_binaryOptions"
                                  @configuration2_selectedNumericOptionsChanged="changeConfiguration2_NumericOptions"
                                  @getOptions="changeResponseJsonToMockUp">
          </ConfigurationSelection>
        </b-col>
      </b-row>

      <b-row v-if="this.pickedTask !==''" class="row justify-content-center optionSelector">
        <button class="btn-hover color-5" v-on:click="sendHTTPRequest">{{ this.pickedTask }}</button>
      </b-row>
    </b-container>

    <b-container fluid v-if="validityStatusOfAllConfigurations === true" class="form1">
      <!--<b-row  class="row justify-conten
      t-center border border-dark configbox">-->
        <b-col sm="12"><h2 style="text-align:center">Estimation</h2></b-col>

      <b-row class="row justify-content-center">
        <div v-if="this.amountOfConfigurations.length===1">
          <b-row>
            <b-col class="formNoShadow border border-dark ">
              <Results v-bind:configuration="this.amountOfConfigurations[0]"
                       v-bind:allResponsesOfAllConfigurations="allResponsesOfAllConfigurations"
                       v-bind:SoftwareSystemsData="SoftwareSystemsData"
                       v-bind:pickedSoftwareSystem="pickedSoftwareSystem" />
            </b-col>
          </b-row>
        </div>
        <div v-if="this.amountOfConfigurations.length===2">
          <b-row>
            <b-col  class="formNoShadow border border-dark compare configbox">
              <Results v-bind:configuration="amountOfConfigurations[0]"
                       v-bind:allResponsesOfAllConfigurations="allResponsesOfAllConfigurations"
                       v-bind:SoftwareSystemsData="SoftwareSystemsData"
                       v-bind:pickedSoftwareSystem="pickedSoftwareSystem" />

            </b-col>
            <b-col  class="formNoShadow border border-dark compare mitte configbox">
              <ResultDeltas v-bind:allResponsesOfAllConfigurations="allResponsesOfAllConfigurations"/>

            </b-col>
            <b-col  class="formNoShadow border border-dark compare configbox">
              <Results v-bind:configuration="amountOfConfigurations[1]"
                       v-bind:allResponsesOfAllConfigurations="allResponsesOfAllConfigurations"
                       v-bind:SoftwareSystemsData="SoftwareSystemsData"
                       v-bind:pickedSoftwareSystem="pickedSoftwareSystem" />

            </b-col>
          </b-row>
        </div>
      </b-row>


        <!--<b-col sm="10" class="border border-dark restultsbox">
        -->
        <b-row class="row justify-content-center radarchart">
          <b-col sm="8">
          <RadarChart v-if="validityStatusOfAllConfigurations === true"
                      v-bind:propertiesOfSoftwareSystem="propertiesOfSoftwareSystem"
                      v-bind:maxValueData="maxValueData"
                      v-bind:amountOfConfigurations="amountOfConfigurations"
                      v-bind:allResponsesOfAllConfigurations="allResponsesOfAllConfigurations"/>
          </b-col>
        </b-row>
        <b-row class="row justify-content-center">
          <b-col sm="12">
          <DetailedAnalysis v-if="validityStatusOfAllConfigurations === true"
                            v-bind:pickedSoftwareSystem="pickedSoftwareSystem"
                            v-bind:propertiesOfSoftwareSystem="propertiesOfSoftwareSystem"
                            v-bind:amountOfConfigurations="amountOfConfigurations"
                            v-bind:allResponsesOfAllConfigurations="allResponsesOfAllConfigurations"
                            v-bind:binaryOptionsList="binaryOptionsList"
                            v-bind:numericOptionsList="numericOptionsList"
                            v-bind:SoftwareSystemsData="SoftwareSystemsData"/>
          </b-col>
        </b-row>




        <!--</b-col>
        -->



    </b-container>
  </div>

</template>


<script>
import Task from "./Task";
import SoftwareSystem from "./SoftwareSystem";
import ConfigurationSelection from "./ConfigurationSelection";
import Results from "./Results";
import axios from "axios";
import RadarChart from "./RadarChart";
import DetailedAnalysis from "./DetailedAnalysis";
//import resultJson from './result.json'
//import FalseMockupJson from "./configuration_with_alternative.json"
import new_softwaresystems from "./new_softwaresystems.json"
import ResultDeltas from "./ResultDeltas";
//import response_1_7_Zip_invalid from "./response_1_7-Zip_invalid"
//import response_1_7_Zip_valid from "./response_1_7-Zip_valid"
//import response_1_HSQLDB_invalid from "./response_1_HSQLDB_invalid"
//import response_1_HSQLDB_valid from "./response_1_HSQLDB_valid"
//import softwaresytems_properties_with_maxValues from "./softwareystems_properties_with_maxValues.json"

export default {
  name: "General",
  components: {
    ResultDeltas,
    Results,
    SoftwareSystem,
    Task,
    RadarChart,
    DetailedAnalysis,
    ConfigurationSelection,
  },
  data() {
    return {
      pickedSoftwareSystem: "",
      pickedTask: "", // is 'evaluate' or 'compare'
      maxValueData: {},   //Data for radarchartscale , contains maxVlaues for properties

      //optionsAlternativeConfiguration: [],
      //configuration1_selectedOptions: [], //contains all the options that are selected by the user
      SoftwareSystemsData: {},     //Json with Softwaresytems and their options
      binaryOptionsList: [],             //List with binary Options for the currently selected SoftwareSystem
      numericOptionsList: [],            //List with numeric Options for the currently selected SoftwareSystem

      //new defined for Issue "Compare Configurations"
      configuration1_selectedBinaryOptions: [],
      configuration1_selectedNumericOptions: {},
      configuration1_responseOfHTTPRequest: {}, //JSON for Configuration 1 from server respond is stored in here

      configuration2_selectedBinaryOptions: [],
      configuration2_selectedNumericOptions: {},
      configuration2_responseOfHTTPRequest: {}, //JSON for Configuration 2 from server respond is stored in here
    }
  },
  mounted() {
     this.sendSoftwareRequest()        //gets Data for SoftwareSystems and Options
    //this.setMockupSoftwareRequest()     //Mockup Data for SoftwareSystems and Options
  },

  computed: {
    propertiesOfSoftwareSystem(){ //List that contains all properties of a software System, e.g.: ['cpu', 'energy', 'time']
      return Object.keys(this.configuration1_responseOfHTTPRequest.estimation.systemValues)
    },
    validityStatusOfAllConfigurations() {
      if (this.pickedTask === "evaluate") {
        return this.configuration1_isValid
      }
      if (this.pickedTask === "compare") {
        return this.configuration1_isValid && this.configuration2_isValid
      }
      return false
    },
    allResponsesOfAllConfigurations() {
      if (this.pickedTask === "evaluate") {
        return [this.configuration1_responseOfHTTPRequest]
      }
      if (this.pickedTask === "compare") {
        return [this.configuration1_responseOfHTTPRequest, this.configuration2_responseOfHTTPRequest]
      }
      return []
    },
    amountOfConfigurations() {
      if (this.pickedTask === 'evaluate') {
        return ['Configuration 1']
      }
      if (this.pickedTask === 'compare') {
        return ['Configuration 1', 'Configuration 2']
      }
      return [];
    },
    configuration1_isValid() {
      return this.configuration1_responseOfHTTPRequest.isValid
    },
    configuration2_isValid() {
      return this.configuration2_responseOfHTTPRequest.isValid;
    },
    softwareSystemList() {                                                     //List with available SoftwareSystems
      let tempArray = []
      for (let i = 0; i < this.SoftwareSystemsData.length; i++) {
        tempArray.push(this.SoftwareSystemsData[i].name)
      }
      return tempArray
    },

  },
  methods: {
    changeConfiguration1_binaryOptions: function (selectedBinaryOptions) {
      this.configuration1_selectedBinaryOptions = selectedBinaryOptions;
    },
    changeConfiguration1_NumericOptions: function (selectedNumericOptions) {
      this.configuration1_selectedNumericOptions = selectedNumericOptions;
    },
    changeConfiguration2_binaryOptions: function (selectedBinaryOptions) {
      this.configuration2_selectedBinaryOptions = selectedBinaryOptions;
    },
    changeConfiguration2_NumericOptions: function (selectedNumericOptions) {
      this.configuration2_selectedNumericOptions = selectedNumericOptions;
    },
    /**
    setAlternativeConfiguration: function () {                                                         //if a suggested configuration gets chosen by the user, overwrites the old configuration with the new one
      this.configuration1_responseOfHTTPRequest = this.configuration1_responseOfHTTPRequest.alternativeConfiguration
      this.optionsAlternativeConfiguration = this.configuration1_responseOfHTTPRequest.activatedOptions
      this.configuration1_selectedOptions = this.optionsAlternativeConfiguration
      this.checkCheckboxesForAlternativeConfiguration(this.configuration1_responseOfHTTPRequest.activatedBinaryOptions, this.configuration1_responseOfHTTPRequest.numericOptionValues)
    },*/
    updateOptionsList: function (softwareSystem) {                                                       //updates the Optionslist for the selected Softwaresystem
      for (let i = 0; i < this.SoftwareSystemsData.length; i++) {
        if (softwareSystem === this.SoftwareSystemsData[i].name) {
          this.binaryOptionsList = this.SoftwareSystemsData[i].binaryOptions
          this.numericOptionsList = this.SoftwareSystemsData[i].numericOptions
        }
      }
    },

    changeCurrentSowftwarename: function (newName) {                                                  //updates the softwaresystem thats currently selected
      this.pickedSoftwareSystem = newName
      //this.clearOptionLists() watcher used
    },
    changeTask: function (newName) {                                                                 //updates the task thats currently selected
      this.pickedTask = newName
    },
    /**
    checkCheckboxesForAlternativeConfiguration: function (binaryOptionsAlternativeConfiguration, numericOptionsAlternativeConfiguration) {         //if a suggested configuration gets chosen by the user , updates the checkboxes for the new configuration
      this.$refs.configurationSelection.checkBoxesForAlternativeConfiguration(binaryOptionsAlternativeConfiguration, numericOptionsAlternativeConfiguration)
    },
    clearOptionLists: function () {                                                                   //clears activated lists when switching softwaresystems
      this.$refs.configurationSelection.clearOptionLists()
    },*/
    sendSoftwareRequest: function () {                                         //request to backend. gets json with softwaresystems and their Options
      axios
          .get('http://swtp7-greenc-backend.herokuapp.com/softwaresystems')
          .then(response => (this.SoftwareSystemsData = response.data))
    },
    setMockupSoftwareRequest: function () {                                           //loads mockupdata for SoftwareSystems
      this.SoftwareSystemsData = new_softwaresystems
    },
    sendHTTPRequest: function () {                             //request to backend . gets Json with evaluation
      axios
          .post('http://swtp7-greenc-backend.herokuapp.com/evaluate/' + this.pickedSoftwareSystem,
              {
                "activatedBinaryOptions": this.configuration1_selectedBinaryOptions,
                "numericOptionValues": this.configuration1_selectedNumericOptions
              })
          .then(response => (this.configuration1_responseOfHTTPRequest = response.data))
      if (this.pickedTask === "compare") {
        axios
            .post('http://swtp7-greenc-backend.herokuapp.com/evaluate/' + this.pickedSoftwareSystem,
                {
                  "activatedBinaryOptions": this.configuration2_selectedBinaryOptions,
                  "numericOptionValues": this.configuration2_selectedNumericOptions
                })
            .then(response => (this.configuration2_responseOfHTTPRequest = response.data))
      }
    },
    updateMaxValueData:function (){                                   //updates the maxValues of properties for the currently selected system
      for(let i =0;i<this.SoftwareSystemsData.length;i++){
        if(this.SoftwareSystemsData[i].name===this.pickedSoftwareSystem){
          this.maxValueData=this.SoftwareSystemsData[i]
        }
      }
    },
    /** Function for selecting  mock-up json
    changeResponseJsonToMockUp: function () {
      this.configuration1_responseOfHTTPRequest = response_1_HSQLDB_invalid
    },*/
  },
  watch: {
    pickedSoftwareSystem() {                                                           //updates the Optionslist when the selected SoftwareSystem gets changed
      this.updateOptionsList(this.pickedSoftwareSystem);
      this.updateMaxValueData();
      this.configuration1_selectedBinaryOptions = [];
      this.configuration1_selectedNumericOptions = {};
      this.configuration1_responseOfHTTPRequest = {}

      this.configuration2_selectedBinaryOptions = [];
      this.configuration2_selectedNumericOptions = {};
      this.configuration2_responseOfHTTPRequest = {}
    },
    pickedTask() {
      this.configuration2_selectedBinaryOptions = [];
      this.configuration2_selectedNumericOptions = {};
      this.configuration2_responseOfHTTPRequest = {}
    }
  },
}
</script>


<style scoped>
.configbox {
  background: whitesmoke;
  margin: 0.5%;
}
.radarchart{
  opacity: 1.0;
}
.form{

  background:#e6e6e6;
  border-radius:8px;
  box-shadow:0 0 40px -10px #000;
  padding:10px 30px;
 max-width:calc(100vw - 40px)
;box-sizing:border-box;
  font-family:'Montserrat'
  ,sans-serif;
  position:relative ;
  opacity: 0.9;
  word-break: break-word;

}
.form1{
  background:#e6e6e6;
  border-radius:8px;
  box-shadow:0 0 40px -10px #000;
  padding:10px 30px;
  max-width:calc(100vw - 40px)
;box-sizing:border-box;
  font-family:'Montserrat'
  ,sans-serif;
  position:relative ;
  opacity: 0.97;
}

.formNoShadow{
  background:#e6e6e6;
  border-radius:8px;
  padding:10px 30px;
  max-width:calc(100vw - 40px)
;box-sizing:border-box;
  font-family:'Montserrat'
  ,sans-serif;
  position:relative;
}

.compare {
  white-space: nowrap;
  width: 100vh;
}

.mitte {
  text-align: center;
}



.btn-hover {
  width: 100px;
  font-size: 16px;
  font-weight: 600;
  color: #fff;
  cursor: pointer;
  margin: 10px;
  height: 40px;
  text-align:center;
  border: none;
  background-size: 300% 100%;

  border-radius: 50px;
  moz-transition: all .4s ease-in-out;
  -o-transition: all .4s ease-in-out;
  -webkit-transition: all .4s ease-in-out;
  transition: all .4s ease-in-out;
}

.btn-hover:hover {
  background-position: 100% 0;
  moz-transition: all .4s ease-in-out;
  -o-transition: all .4s ease-in-out;
  -webkit-transition: all .4s ease-in-out;
  transition: all .4s ease-in-out;
}

.btn-hover:focus {
  outline: none;
}
.btn-hover.color-5 {
  background-image: linear-gradient(to right, #0ba360, #3cba92, #30dd8a, #2bb673);
  box-shadow: 0 4px 15px 0 rgba(23, 168, 108, 0.75);
}


</style>