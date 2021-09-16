<template>
  <div>
    <!--
    <b-container fluid>
      -->
            <h3>{{this.configuration}}</h3>
            <div v-for="(value,key) in this.configurationX_responseOfHTTPRequest.estimation.systemValues" v-bind:key="value" >
            {{key}} : {{value | trimNumbers}} {{findPropertiesUnit(key)}}
           </div>
    <!--
      </b-container>
      -->
    </div>
  </template>

  <script>

  export default {
    name: "Results",
    props: [
      //'result',
        'configuration',
        'allResponsesOfAllConfigurations',
        'SoftwareSystemsData',
        'pickedSoftwareSystem',
    ],
    computed:{
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
    methods: {
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
      }
    }
  }
  </script>

  <style scoped>
  .estimationbox {
    text-align: center;
    background: beige;
    margin: 2%;
  }
  </style>