<template>
  <div>
    <div class="statistics-box row">
      <p class="statistics-title-center mt-5">{{ age }}대 {{ sex }}들의 선호 부위</p>
    </div>
    <canvas id="myChart-4" width="100%" height="100%"></canvas>
  </div>
</template>

<script>
import Chart from 'chart.js'
export default {
  props: {
    update:Boolean,
    sex: String,
    age: Number,
    people: Number,
    favorite: Array,
    labels: Array,
  },
  watch:{
    update:function(){
      this.createChart();
    }
  },
  methods: {
    createChart() {
      const ctx = document.getElementById("myChart-4")
      new Chart(ctx, {
        type: 'radar',
        data: {
          labels: this.labels,
          datasets: [{
            label: "부위",
            data: this.favorite,
            backgroundColor: 'rgba(230, 92, 46, 0.5)',
            borderColor: 'rgba(230, 92, 46, 1)',
            pointBackgroundColor: 'rgba(230, 92, 46, 1)',
          }]
        },
        options: {}
      })
    },
  },
}
</script>

<style>

</style>