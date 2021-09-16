<template>
  <div>
    <div v-for="property in pickedPropertiesForDetailedAnalysis"
         v-bind:key="property">

      <ColumnChart v-bind:propertyForDetailedAnalysis="property"
                   v-bind:allResponsesOfAllConfigurations="allResponsesOfAllConfigurations"
                   v-bind:amountOfConfigurations="amountOfConfigurations"
                   v-bind:SoftwareSystemsData="SoftwareSystemsData"
                   v-bind:pickedSoftwareSystem="pickedSoftwareSystem" />

      <!-- Donut Charts -->
      <div style="text-align: center"> Most significant Interactions on {{property}}:</div>
      <b-container fluid>
      <b-row fluid align-h="center" align-v="stretch center" class="row justify-content-center">
        <b-col fluid v-for="configuration in amountOfConfigurations" v-bind:key="configuration" align-self="center" align-v="center" class="noPadding" sm="6">
          <b-row fluid align-v="stretch" class="row justify-content-center">
          <b-col fluid v-for="type in typesOfInteraction" v-bind:key="type" align-self="center" class="noPadding" sm="6">
            <DonutChart v-bind:configuration="configuration"
                        v-bind:propertyForDetailedAnalysis="property"
                        v-bind:typeOfInteraction="type"
                        v-bind:positiveNegativeInfluences="positiveNegativeInfluences"
                        v-bind:pickedSoftwareSystem="pickedSoftwareSystem" class="DonutChartCSS"/>
          </b-col>
          </b-row>
        </b-col>
      </b-row>
      </b-container>


      <!-- HTML Code for improve property -->
      <b-row>
        <b-col v-for="configuration in amountOfConfigurations" v-bind:key="configuration" sm="6">
          <br>
          <br>
          <button class="btn-hover color-8" v-on:click="sendHTTPRequestImproveProperty(configuration,property)">Improve {{property}} of {{configuration}}</button>
          <div v-for="configs in showDetailsForImprovedProperty" v-bind:key="configs">
            <div v-if="show"></div>               <!-- this updates the html code . do not remove!!! -->
            <div v-if='configuration===configs[0] && configs[2] && property===configs[1] && improvedConfigurationList.length !== 0 '>
              <div v-for="improvedConfigs in improvedConfigurationList" v-bind:key="improvedConfigs">
                <div v-if="improvedConfigurationList[improvedConfigs.id].configurationName===configuration && improvedConfigurationList[improvedConfigs.id].propertyName === property" >
                  {{improvedConfigurationList[improvedConfigs.id].percentage}}<br>
                  <br>
                  <input type="checkbox"
                         v-model="improvedConfigurationList[improvedConfigs.id].showOptions"/>
                  <label class="form-check-label">show new Options</label>
                  <div v-if="improvedConfigurationList[improvedConfigs.id].showOptions">
                    new activatedBinaryOptions:{{improvedConfigurationList[improvedConfigs.id].improvedConfig.activatedBinaryOptions |trimBinaryOptions}}<br>
                    new numericOptionValues:{{improvedConfigurationList[improvedConfigs.id].improvedConfig.numericOptionValues|trimNumericOptions}}
                  </div>
                  <br>
                  Id for current configuration: <input type="text" v-bind:id="improvedConfigurationList[improvedConfigs.id].configurationName+'current'" :value=improvedConfigurationList[improvedConfigs.id].currentID readonly>
                  <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-clipboard button" viewBox="0 0 16 16" @click="copy(improvedConfigurationList[improvedConfigs.id].configurationName+'current')" >
                    <path d="M4 1.5H3a2 2 0 0 0-2 2V14a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V3.5a2 2 0 0 0-2-2h-1v1h1a1 1 0 0 1 1 1V14a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V3.5a1 1 0 0 1 1-1h1v-1z"/>
                    <path d="M9.5 1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-3a.5.5 0 0 1-.5-.5v-1a.5.5 0 0 1 .5-.5h3zm-3-1A1.5 1.5 0 0 0 5 1.5v1A1.5 1.5 0 0 0 6.5 4h3A1.5 1.5 0 0 0 11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3z"/>
                  </svg> <br>
                  Id for improved configuration: <input type="text" v-bind:id="improvedConfigurationList[improvedConfigs.id].configurationName+'improved'" :value=improvedConfigurationList[improvedConfigs.id].improvedID readonly>
                  <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-clipboard button" viewBox="0 0 16 16" @click="copy(improvedConfigurationList[improvedConfigs.id].configurationName+'improved')" >
                    <path d="M4 1.5H3a2 2 0 0 0-2 2V14a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V3.5a2 2 0 0 0-2-2h-1v1h1a1 1 0 0 1 1 1V14a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V3.5a1 1 0 0 1 1-1h1v-1z"/>
                    <path d="M9.5 1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-3a.5.5 0 0 1-.5-.5v-1a.5.5 0 0 1 .5-.5h3zm-3-1A1.5 1.5 0 0 0 5 1.5v1A1.5 1.5 0 0 0 6.5 4h3A1.5 1.5 0 0 0 11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3z"/>
                  </svg> <br>
                  <button class="btn-hover color-5" v-on:click="scrollWin()">Move screen to top</button>
                </div>
              </div>
            </div>
          </div>
        </b-col>
      </b-row>


      <br>
      <br>
      <br>
    </div>
  </div>
</template>

<script>
import ColumnChart from "./ColumnChart";
import DonutChart from "./DonutChart";
import axios from "axios";

export default {
  name: "detailedChartsOfOneProperty",
  components: {
    DonutChart,
    ColumnChart
  },
  props: [
    "pickedPropertiesForDetailedAnalysis",
    "allResponsesOfAllConfigurations",
    "amountOfConfigurations",
    "propertiesOfSoftwareSystem",
    "pickedSoftwareSystem",
    "binaryOptionsList",
    "numericOptionsList",
    "SoftwareSystemsData",
      "showLegend"
  ],
  data: function () {
    return {
      typesOfInteraction: ["positive", "negative"],
      improvedConfigurationList:[],
      HTTPImproveResponse:{},                   //Response from Request for improve property
      show: false,                              //helps to reload the html code, no other functionality
      saveCounter:0,                            //Counter for amount of improved configs that are already saved
      lastConfiguration:"",                     //last configuration that got improved
      originalId: "",                           //ID for the current configuration
      improvedId: "",                           //ID for the improved configuration
    }
  },
  computed: {
    positiveNegativeInfluences() {
      //positiveNegativeInfluences is an array, it's length is equal to the amount of configurations to analyse
      //example: Task = "compare", two configurations to analyse, positiveNegativeInfluences.length = 2
      //positiveNegativeInfluences[0] = configurationX_positiveNegativeInfluences
      //configurationX_positiveNegativeInfluences is an Array, it's length is equal to the number of properties of the software system
      //example: HSQLDB
      //configurationX_positiveNegativeInfluences[0] = ['cpu', positiveInfluencesForThisProperty, negativeInfluencesForThisProperty]
      //configurationX_positiveNegativeInfluences[1] = ['energy', positiveInfluencesForThisProperty, negativeInfluencesForThisProperty]
      //configurationX_positiveNegativeInfluences[2] = ['time', positiveInfluencesForThisProperty, negativeInfluencesForThisProperty]
      //positiveInfluencesForThisProperty = [values, optionNames] is again an array
      //values and optionNames are also both arrays, both have the same length
      //in values: only the numerical values are stored
      //in optionNames: names of those options, which are involved in this influence that contribute to that respective value
      let positiveNegativeInfluences = [] //this array will be returned in the end
      for (let c = 0; c < this.amountOfConfigurations.length; c++) {
        let configurationX_positiveNegativeInfluences = [] //this will be one element of the solution array
        for (let p = 0; p < this.propertiesOfSoftwareSystem.length; p++) { //Iteration though all properties
          let positiveInfluencesForThisProperty = [[], []] //first array of this array has all positive values, second array has all respective involved options
          let negativeInfluencesForThisProperty = [[], []] //same principle with negative values/involved options
          let positiveNegativeInfluencesForThisProperty = []
          let array = this.allResponsesOfAllConfigurations[c].estimation.partValues

          for (let i = 0; i < array.length; i++) { //Iteration through all partValues
            for (const [key, value] of Object.entries(array[i].values)) { //Iteration though all values of 1 specific partValue
              if (key === this.propertiesOfSoftwareSystem[p]) {
                if (value > 0) {
                  positiveInfluencesForThisProperty[0].push(value)
                  let positiveInvolvedOptions = ""
                  for (let j = 0; j < array[i].options.length; j++) { //Iteration though all the names of the involved options for this partValue
                    positiveInvolvedOptions += (array[i].options[j] + "<br>") //involved options in an influence are separated by a line break
                  }
                  positiveInfluencesForThisProperty[1].push(positiveInvolvedOptions)
                }
                if (value < 0) {
                  negativeInfluencesForThisProperty[0].push(value)
                  let negativeInvolvedOptions = ""
                  for (let k = 0; k < array[i].options.length; k++) {
                    negativeInvolvedOptions += (array[i].options[k] + "<br>")
                  }
                  negativeInfluencesForThisProperty[1].push(negativeInvolvedOptions)
                }
              }
            }
          }
          positiveNegativeInfluencesForThisProperty.push(this.propertiesOfSoftwareSystem[p])
          positiveNegativeInfluencesForThisProperty.push(positiveInfluencesForThisProperty)
          positiveNegativeInfluencesForThisProperty.push(negativeInfluencesForThisProperty)
          configurationX_positiveNegativeInfluences.push(positiveNegativeInfluencesForThisProperty)
        }
        console.log("Test: Response changed in General")
        positiveNegativeInfluences.push(configurationX_positiveNegativeInfluences)
      }
      return positiveNegativeInfluences
    },
    showDetailsForImprovedProperty() {                //needed for v-for in html  (improve property
      let tempMap={}
      for(let i =0; i<this.amountOfConfigurations.length;i++){
        tempMap[this.amountOfConfigurations[i]]= [this.amountOfConfigurations[i],null,false]      //name of configuation,name of property that got improved,boolean for v-if to show this
      }
      return tempMap
    },
  },
  methods:{
    sendHTTPRequestImproveProperty: function (configuration,property) {                             //request for improve property
      let stringifiedNumericOptions = this.allResponsesOfAllConfigurations[(parseInt(configuration.slice(-1))-1)].numericOptionValues
      for (const [key, value] of Object.entries(this.allResponsesOfAllConfigurations[(parseInt(configuration.slice(-1))-1)].numericOptionValues)){
        stringifiedNumericOptions[key]=value.toString()
      }

      let HTTPConfiguration ={activatedBinaryOptions: this.allResponsesOfAllConfigurations[(parseInt(configuration.slice(-1))-1)].activatedBinaryOptions,numericOptionValues: stringifiedNumericOptions}
      this.lastConfiguration =configuration
      axios
          .post('http://swtp7-greenc-backend.herokuapp.com/improve/' + this.pickedSoftwareSystem,
              {
                "property": property,
                "originalConfiguration": HTTPConfiguration
              })
          .then(response => (this.HTTPImproveResponse = response.data))
    },
    improveProperty:function (configuration,property){                                //updates showDetailsForImprovedProperty for current configuration
      for (const [key] of Object.entries(this.showDetailsForImprovedProperty)){
        if(key === configuration){
          this.showDetailsForImprovedProperty[configuration]=[configuration,property,true]
        }
      }
      this.show=!this.show    //updates HTML , kinda important but "hacky af"~Florian
      this.originalId = this.save(this.HTTPImproveResponse.originalConfiguration.activatedBinaryOptions ,this.HTTPImproveResponse.originalConfiguration.numericOptionValues);
      this.improvedId = this.save(this.HTTPImproveResponse.improvedConfiguration.activatedBinaryOptions , this.HTTPImproveResponse.improvedConfiguration.numericOptionValues);
      this.updateimprovedConfigurationList(configuration,property)
    },
    updateimprovedConfigurationList:function(configuration,property){     //Builds array which contains Maps with all the needed information for the Buttons. One map for each button
      let tempPercentage = this.getDeltaForProperty(property)
      if(this.improvedConfigurationList.length===0){
        this.improvedConfigurationList.push({improvedConfig:  this.HTTPImproveResponse.improvedConfiguration,configurationName: configuration,propertyName: property,showOptions: false, id: this.saveCounter,improvedID: this.improvedId,currentID:this.originalId,percentage: tempPercentage})
        this.saveCounter=this.saveCounter+1
      }
      else{
       for(let i=0;i<this.improvedConfigurationList.length;i++){
         if(this.improvedConfigurationList[i].configurationName === configuration && this.improvedConfigurationList[i].propertyName === property){
            break}
         if(i === this.improvedConfigurationList.length-1 && (this.improvedConfigurationList[i].configurationName !== configuration || this.improvedConfigurationList[i].propertyName !== property)){
           this.improvedConfigurationList.push({improvedConfig:  this.HTTPImproveResponse.improvedConfiguration,configurationName: configuration,propertyName: property,showOptions: false, id: this.saveCounter,improvedID: this.improvedId ,currentID: this.originalId ,percentage: tempPercentage})
           this.saveCounter=this.saveCounter+1
         }
       }
      }
    },
    scrollWin:function (){    //scrolls the window to the top
      window.scrollTo({
        top: 1000,
        left: 1000,
        behavior: 'smooth'
      });
    },
    getDeltaForProperty:function (property){      //Function to calculate the % difference from the current configuration to the improved configuration in regards to the chosen property
      let tempCurrentValue = 0
      let tempImprovedValue = 0
      for (const [key, value] of Object.entries(this.allResponsesOfAllConfigurations[(parseInt(this.lastConfiguration.slice(-1)) - 1)].estimation.systemValues)){
        if(key===property)
        tempCurrentValue=value
      }
      for (const [key, value] of Object.entries(this.HTTPImproveResponse.improvedConfiguration.estimation.systemValues)){
        if(key===property)
          tempImprovedValue=value
      }
      if((tempCurrentValue)/tempImprovedValue<1){
        return " " + property + " increased by " + this.trimNumbers((100-((tempCurrentValue*100)/tempImprovedValue))) + "%"
      }
      if((tempCurrentValue)/tempImprovedValue>1){
        return " " + property + " reduced by " + this.trimNumbers((((tempCurrentValue*100)/tempImprovedValue)-100)) + "%"
      }
      else {
        return "no improvment for " + property + " possible"
      }
    },
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

    },
    save: function (selectedBinary , selectedNumeric) {                     //function to get the ID for the configuration
      let configurationID = "";
      for (let i = 0; i < this.binaryOptionsList.length; i++) {
        for (let k = 0; k < selectedBinary.length; k++) {
          if (this.binaryOptionsList[i].name === selectedBinary[k]) {
            configurationID += "1";
          }
        }
        configurationID = configurationID + "0";
        if (i < configurationID.length -1) {
          configurationID = configurationID.substring(0, configurationID.length - 1);
        }
      }
      configurationID = this.pickedSoftwareSystem + ":" + parseInt(configurationID, 2) + "_" ;
      if(selectedNumeric !== null) {
        for (let i=0 ; i< this.numericOptionsList.length ; i++) {
          configurationID += selectedNumeric[this.numericOptionsList[i].name] ;
          if(i !== this.numericOptionsList.length-1){
            configurationID += "|";
          }
        }
      }
      return configurationID;
    },
    selectText: function (name) {                             //selects the text in the textbox
  const input = document.getElementById(name);
  input.focus();
  input.select();
},
    copy: function(name) {
      let copyText = document.getElementById(name);
      copyText.select();
      document.execCommand("copy");
    },
  },
  watch:{
    HTTPImproveResponse(){                      //updates the data when request in fetched

      this.improveProperty(this.lastConfiguration,this.HTTPImproveResponse.property)
    },
    allResponsesOfAllConfigurations(){
      this.HTTPImproveResponse={}
      this.originalId=""
      this.improvedId=""
      this.improvedConfigurationList=[]
      this.saveCounter=0
      this.lastConfiguration=""
    }

  },

}
</script>

<style scoped>
.noPadding{
  margin: 0px;
  padding: 0px;
}

.btn-hover {
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

.btn-hover.color-8 {
  background-image: linear-gradient(to right, #29323c, #485563, #2b5876, #4e4376);
  box-shadow: 0 4px 15px 0 rgba(45, 54, 65, 0.75);
}
.button:active {
  transform: translateY(2px);
}
</style>