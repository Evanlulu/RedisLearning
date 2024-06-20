<template>
  <div class="App">
    <HmHeader></HmHeader>
    <HmMain></HmMain>
    <HmFooter></HmFooter>
    
  </div>
</template>

<script>
import HmHeader from './components/HmHeader.vue'
import HmFooter from './components/HmFooter.vue'
import HmMain from './components/HmMain.vue'

export default {
  components:{
    HmHeader: HmHeader,
    HmMain,
    HmFooter
  }
}
</script>

<style>
.App{
  width: 600px;
  height: 700px;
  background-color: #87ceeb;
  margin: 0 auto;
  padding:20px;
}
</style>