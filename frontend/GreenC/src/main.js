import Vue from 'vue';
import App from './App.vue';
import './plugins/bootstrap-vue';

Vue.config.productionTip = false

new Vue({
  render: h => h(App),
}).$mount('#app')
/*
Filter to trim down numbers to 2 non-zero digits, returns a String
 */
Vue.filter('trimNumbers', function (number) {
  number= number.toString()
    let foundDot = false
    let numbersbeforeDot = false
    let postionToCut
    let exponential=""
    for(let j =0;j<number.length; j++) {
        if (number.charAt(j) === "e")
            exponential = number.slice(j, number.length )
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
  return number.slice(0,postionToCut)+exponential
}),
    Vue.filter('trimBinaryOptions', function (options) {
        return options.toString().replace(/"/g,"")
    }
    ),
    Vue.filter('trimNumericOptions', function (options) {
        let temptext=" "
        for (const [key, value] of Object.entries(options)){
            temptext = temptext.concat(key+":"+value.toString() +" ")
        }
        if(temptext===" "){
            temptext="this softwaresystem has no numeric options"
        }
        return temptext
        }
    )