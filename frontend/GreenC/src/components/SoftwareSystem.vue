<template>
<div id="SoftwareSystem">

  <h2>Choose a Software System</h2>
  <div v-if="softwareSystemList.length===0">
    please wait while list for softwaresystems is loading
  </div>
  <div v-for ="softwareName in softwareSystemList" v-bind:key="softwareName">
    <input type="radio" id=softwareName name="softwaresystem" value=softwareName @click="$emit('sendSoftwarename',softwareName)">
    <label for=softwareName> {{softwareName}}</label><br>
  </div>
</div>
</template>

<script>
export default {
name: "SoftwareSystem",
  props: {
    softwareSystemList:{                      //list with available Softwaresystems
      type: Array
    }
  }
}
</script>

<style scoped>
#SoftwareSystem{
  text-align: left;
  margin: 2%;
}

</style>