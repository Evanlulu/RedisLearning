<template>
  <div class="hm-footer">
    我是footer
    <HmButton></HmButton>
  </div>
</template>

<script>
import HmButton from './HmButton.vue'
export default {
    components: { HmButton },

}
</script>

<style>
.hm-footer{
    height: 100px;
    line-height: 100px;
    text-align: center;
    font-size: 30px;
    background-color: blue;
    color:white;
}

</style>