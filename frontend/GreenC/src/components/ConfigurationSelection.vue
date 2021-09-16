<template>
  <div id="chooseConfig">

    <h2>{{ this.headline }}</h2>

    <div v-if="this.configuration === 'Configuration 1'">
      <!--Saved Configuration-->
      <input type="radio" class="green" id="savedConfiguration1" name="configuration1" @click="selection = 1">
      <label for="savedConfiguration1"> Load Configuration</label><br>

      <label v-if="selection === 1" for="configurationID1">Configuration-ID: <input type="text" id="configurationID1"
                                                                                   name="configurationID"
                                                                                   v-model="configurationId"></label><br>
      <button v-if="selection === 1" class="btn-hover color-8" @click=" loadconfiguration">Load configuration</button>
      <br>
      <!--<p v-if="selection === 1" id="loadidMessage1"></p>-->
      <p v-if="selection === 1">{{this.wrongId}}</p>

      <!--Set new Configuration-->
      <input type="radio" id="setConfiguration1" name="configuration1" @click="selection = 2">
      <label for="setConfiguration1"> Set Configuration</label><br>
    </div>


    <div v-if="this.configuration === 'Configuration 2'">
      <!--Saved Configuration-->
      <input type="radio" id="savedConfiguration2" name="configuration2" @click="selection = 1">
      <label for="savedConfiguration2"> Load Configuration</label><br>

      <label v-if="selection === 1" for="configurationID2">Configuration-ID: <input type="text" id="configurationID2"
                                                                                   name="configurationID"
                                                                                   v-model="configurationId"></label><br>
      <button v-if="selection === 1" class="btn-hover color-8" @click="loadconfiguration">Load configuration</button>
      <br>
      <!--<p v-if="selection === 1" id="loadidMessage2"></p>-->
      <p v-if="selection === 1">{{this.wrongId}}</p>

      <!--Set new Configuration-->
      <input type="radio" id="setConfiguration2" name="configuration2" @click="selection = 2">
      <label for="setConfiguration2"> Set Configuration</label><br>
    </div>


    <Options
             ref="options"
             v-bind:numericOptionsList="numericOptionsList"
             v-bind:binaryOptionsList="binaryOptionsList"
             v-bind:pickedSoftwareSystem="pickedSoftwareSystem"
             @selectedBinaryOptionsChanged="emitSelectedBinaryOptions"
             @selectedNumericOptionsChanged="emitSelectedNumericOptions"
             @sendConfiguration="getOptions"/>


    <div>
      <p v-if="configurationX_responseOfHTTPRequest.isValid === true">{{ this.configuration }} is valid.</p>

      <div v-if="configurationX_responseOfHTTPRequest.isValid === false">
        <p>{{ this.configuration }} is not valid.</p><br>
        <p>You can try this alternative configuration instead:</p><br>
        <p>binaryOptions: {{ configurationX_responseOfHTTPRequest.alternativeConfiguration.activatedBinaryOptions | trimBinaryOptions}}</p><br>
        <p>numericOptions: {{ configurationX_responseOfHTTPRequest.alternativeConfiguration.numericOptionValues | trimNumericOptions }}</p>
        <button class="btn-hover color-1" v-on:click="checkBoxesForAlternativeConfiguration">Use alternative configuration</button>
      </div>


        <button v-if="configurationX_responseOfHTTPRequest.activatedBinaryOptions !== []" class="btn-hover color-8" @click="save()">Save Configuration
        </button><br>
      <div v-if="this.configuration === 'Configuration 1'">
      <p v-if="showConfigurationID === true"> Configuration-ID:
        <input v-if="showConfigurationID === true" type="text" id="country1"  :value=this.savedID readonly>
        <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-clipboard button" viewBox="0 0 16 16" @click="copy('country1')" >
          <path d="M4 1.5H3a2 2 0 0 0-2 2V14a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V3.5a2 2 0 0 0-2-2h-1v1h1a1 1 0 0 1 1 1V14a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V3.5a1 1 0 0 1 1-1h1v-1z"/>
          <path d="M9.5 1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-3a.5.5 0 0 1-.5-.5v-1a.5.5 0 0 1 .5-.5h3zm-3-1A1.5 1.5 0 0 0 5 1.5v1A1.5 1.5 0 0 0 6.5 4h3A1.5 1.5 0 0 0 11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3z"/>
        </svg></p>
      </div>
      <div v-if="this.configuration === 'Configuration 2'">
        <p v-if="showConfigurationID === true"> Configuration-ID:
          <input v-if="showConfigurationID === true" type="text" id="country2"  :value=this.savedID readonly>
          <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-clipboard button" viewBox="0 0 16 16" @click="copy('country2')" >
            <path d="M4 1.5H3a2 2 0 0 0-2 2V14a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V3.5a2 2 0 0 0-2-2h-1v1h1a1 1 0 0 1 1 1V14a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V3.5a1 1 0 0 1 1-1h1v-1z"/>
            <path d="M9.5 1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-3a.5.5 0 0 1-.5-.5v-1a.5.5 0 0 1 .5-.5h3zm-3-1A1.5 1.5 0 0 0 5 1.5v1A1.5 1.5 0 0 0 6.5 4h3A1.5 1.5 0 0 0 11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3z"/>
          </svg></p>
      </div>



    </div>

  </div>
</template>

<script>
import Options from "./Options";

export default {
  name: "ConfigurationSelection",
  components: {
    Options
  },
  props: [
    "configuration",
    "pickedTask",
    "numericOptionsList", //List with numeric Options for the currently selected SoftwareSystem
    "binaryOptionsList", //List with binary Options for the currently selected SoftwareSystem
    "pickedSoftwareSystem", //the name of selected softwareSystem to be compered with the ID-input
    "allResponsesOfAllConfigurations"
  ],
  computed: {
    headline() {
      if (this.pickedTask === "evaluate") {
        return "Set up a Configuration";
      }
      if (this.pickedTask === "compare") {
        return "Set up " + this.configuration;
      }
      return "";
    },
    configurationNumber() {
      return this.configuration.slice(-1) //get the last Char of the String (example: "2" in the String "Configuration 2")
    },
    configurationX_responseOfHTTPRequest(){
      if(this.allResponsesOfAllConfigurations.length !== 0){
        return this.allResponsesOfAllConfigurations[this.configurationNumber - 1]
      }
      return {}
    }
  },
  data() {
    return {
      selection: 0,
      configurationId: "",
      savedID: "",
      wrongId: "",
      selectedBinaryOptions: [],
      selectedNumericOptions: {},
      showConfigurationID: false
    }
  },
  methods: {
    emitSelectedBinaryOptions(selectedBinaryOptions) {
      if (this.configuration === "Configuration 1") {
        this.$emit("configuration1_selectedBinaryOptionsChanged", selectedBinaryOptions)
      }
      if (this.configuration === "Configuration 2") {
        this.$emit("configuration2_selectedBinaryOptionsChanged", selectedBinaryOptions)
      }
      this.selectedBinaryOptions = selectedBinaryOptions
    },
    emitSelectedNumericOptions(selectedNumericOptions) {
      let stringifiedNumericOptions = selectedNumericOptions
      for (const [key, value] of Object.entries(selectedNumericOptions)){
        stringifiedNumericOptions[key]=value.toString()
      }

      if (this.configuration === "Configuration 1") {
        this.$emit("configuration1_selectedNumericOptionsChanged", stringifiedNumericOptions)
      }
      if (this.configuration === "Configuration 2") {
        this.$emit("configuration2_selectedNumericOptionsChanged", stringifiedNumericOptions)
      }
      this.selectedNumericOptions = selectedNumericOptions
    },
    //Function for saveing configuration and give the user his configurationID
    save: function () {
      let configurationID = "";
      for (let i = 0; i < this.binaryOptionsList.length; i++) {
        for (let k = 0; k < this.selectedBinaryOptions.length; k++) {
          if (this.binaryOptionsList[i].name === this.selectedBinaryOptions[k]) {
            configurationID += "1";
          }
        }
        configurationID = configurationID + "0";
        if (i < configurationID.length -1) {
          configurationID = configurationID.substring(0, configurationID.length - 1);
        }
      }
      configurationID = this.pickedSoftwareSystem + ":" + parseInt(configurationID, 2) + "_" ;
      if(this.selectedNumericOptions !== null) {
        for (let i=0 ; i< this.numericOptionsList.length ; i++) {
          configurationID += this.selectedNumericOptions[this.numericOptionsList[i].name] ;
          if(i !== this.numericOptionsList.length-1){
            configurationID += "|";
          }
        }
      }
      /**
      if(this.configurationNumber === "1"){
        document.getElementById("saveMessage1").innerHTML = "Configuration-ID is : " +configurationID;
      }
      if(this.configurationNumber === "2"){
        document.getElementById("saveMessage2").innerHTML = "Configuration-ID is : " +configurationID;
      }*/
      this.savedID = configurationID
      this.showConfigurationID = true
    },

    /**

    getOptions(activatedBinaryOptions, numericOptionValues) {                                         //emits activated Options
      this.$emit("getOptions", activatedBinaryOptions, numericOptionValues)
    },
    */
    checkBoxesForAlternativeConfiguration: function () {                  //checks checkboxes and sets values
      this.$refs.options.checkBoxesForAlternativeConfiguration(this.configurationX_responseOfHTTPRequest.alternativeConfiguration.activatedBinaryOptions, this.configurationX_responseOfHTTPRequest.alternativeConfiguration.numericOptionValues)
    },
    numericOptionValuesAsNumbers: function (numericOptionsValues) {               //converting values of numericOptionsMap from String to int
      const tempMap = new Map()
      for (const [key, value] of Object.entries(numericOptionsValues)) {
        tempMap[key] = parseInt(value)
      }
      return tempMap
    },

    //Function for converting the ID to list of Options and send it to the General to be loaded
    loadconfiguration() {
      if (this.configurationId.includes(":")) {
        let softwarename = this.configurationId.split(":")[0];
        let binary = (this.configurationId.split(":")[1].split("_")[0] >>> 0).toString(2);
        let numeric = this.configurationId.split(":")[1].split("_")[1]
        let accepted;
        let loadedBinaryOptions = [];
        let loadedNumericOptions = {};
        if (this.configurationId.includes(":") && binary.length === this.binaryOptionsList.length
            && this.pickedSoftwareSystem === softwarename && this.configurationId.split(":").length === 2
            && this.configurationId.split(":")[1].split("_").length === 2) {
          if (numeric !== "" && numeric.split("|").length === this.numericOptionsList.length) {
            for (let l = 0; l < this.numericOptionsList.length; l++) {
              if (numeric.split("|")[l] <= this.numericOptionsList[l].maxValue && numeric.split("|")[l] >= this.numericOptionsList[l].minValue) {
                accepted = true;
              } else {
                accepted = false;
                break;
              }
            }
          } else if (numeric !== "") {
            accepted = false;
          } else if (numeric === "" && this.numericOptionsList.length !== 0) {
            accepted = false;
          } else {
            for (var k = 0; k < binary.length; k++) {
              if (binary[k] === "1" || binary[k] === "0") {
                accepted = true;
              } else {
                accepted = false;
                break;
              }
            }
          }
        } else {
          accepted = false;
        }
        if (accepted) {
          /**
          if(this.configurationNumber === "1"){
            document.getElementById("loadidMessage1").innerHTML = "--Loading--"
          }
          if(this.configurationNumber === "2"){
            document.getElementById("loadidMessage2").innerHTML = "--Loading--"
          }*/
          this.wrongId = ""
          for (let i = 0; i < binary.length; i++) {
            if (binary[i] === "1") {
              loadedBinaryOptions.push(this.binaryOptionsList[i].name);
            }
          }
          for (let j = 0; j < this.numericOptionsList.length; j++) {
            loadedNumericOptions[this.numericOptionsList[j].name] = numeric.split("|")[j];
          }
          //this.$emit('loadConfiguration', loadedBinaryOptions, loadedNumericOptions);
          this.$refs.options.checkBoxesForAlternativeConfiguration(loadedBinaryOptions, loadedNumericOptions)
        } else {
          /**
          if(this.configurationNumber === "1"){
            document.getElementById("loadidMessage1").innerHTML = " --Wrong ID-- "
          }
          if(this.configurationNumber === "2"){
            document.getElementById("loadidMessage2").innerHTML = " --Wrong ID-- "
          }*/
          this.wrongId = "--Wrong ID--"
        }
      } else {
        /**
        if(this.configurationNumber === "1"){
          document.getElementById("loadidMessage1").innerHTML = " --Wrong ID-- "
        }
        if(this.configurationNumber === "2"){
          document.getElementById("loadidMessage2").innerHTML = " --Wrong ID-- "
        }*/
        this.wrongId = "--Wrong ID--"
      }
    },
    copy: function(name) {
  let copyText = document.getElementById(name);
  copyText.select();
  document.execCommand("copy");
},
  },
  watch:{
    selectedBinaryOptions(){
      this.showConfigurationID = false
    },
    selectedNumericOptions(){
      this.showConfigurationID = false
    },

  }

}

</script>

<style scoped>
#chooseConfig {
  margin: 2%;
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
.btn-hover.color-1 {
  background-image: linear-gradient(to right, #25aae1, #40e495, #30dd8a, #2bb673);
  box-shadow: 0 4px 15px 0 rgba(49, 196, 190, 0.75);
}
.btn-hover.color-8 {
  background-image: linear-gradient(to right, #29323c, #485563, #2b5876, #4e4376);
  box-shadow: 0 4px 15px 0 rgba(45, 54, 65, 0.75);
}

.button:active {
  transform: translateY(2px);
}
</style>