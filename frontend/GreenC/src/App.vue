<template>
  <div class="background">
    <img class="First-Background" src="./assets/First-Background.png" >
    <img class="pic" src="./assets/logo.png">
    <img class="scroll" v-on:click="scrollWin()" src="./assets/down.png" />
    <div class="threeD">
      To Give The World More Greenness
    </div>
    <div id="app">
      <General/>
    </div>
  </div>

</template>

<script>


import General from "./components/General";
//Setup for Bootstrap in Vue:
import Vue from 'vue';
import BootstrapVue from 'bootstrap-vue';
Vue.use(BootstrapVue);
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';

//Setup for ApexCharts in Vue:
import VueApexCharts from 'vue-apexcharts'
Vue.use(VueApexCharts)
Vue.component('apexchart', VueApexCharts)

export default {
  name: 'App',
  components: {
    General,
  },
  methods: {
    scrollWin:function (){    //scrolls the window to the top
      window.scrollTo({
        top: 800,
          behavior: 'smooth'
    });
    },
  }
}
</script>

<style>

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  margin-top: 60px;
  font-size: 15pt;

}

.background {
  background: url(./assets/GreenC-Background.jpeg) no-repeat center center fixed;
  opacity: 0.9;
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  background-size: cover;
}
body{
  background: rgba(218,218,218,1)
}

.threeD {
  color: black;
  white-space: nowrap;
  position: absolute;
  top: 62%;
  left: 55%;
  transform: translate(-0%, -50%);
  font-size: 2em;
  font-family: sans-serif;
  letter-spacing: 0.1em;
  transition: 0.1s;

}

.threeD:hover {
  transition: 0.3s;
  transform: scale(1.1) translate(-0%, -50%);
}

.scroll {
  position: absolute;
  top: 90%;
  left: 50%;
  width: 5%;
  height: 5%;
}
.pic {
  position: absolute;
  top: 20%;
  left: 37%;
  width: 60%;
  height: 60%;
}
.First-Background{
  width:100%;
  height:100vh;
}
</style>
