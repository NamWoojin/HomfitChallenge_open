<template>
  <dd class="bar" style="--start: 45;" @click="UpdateName(index)">
    <span class="value">{{ month.achievement_rate }}%</span>
  </dd>
</template>

<script>
export default {
  props: {
    month: Object,
    index: Number,
    monthList: Array,
  },
  data() {
    return {
      bar: "bar",
    }
  },
  mounted() {
    const bars = document.querySelectorAll(".bar")
    let i = 0
    bars.forEach(bar => {
      bar.style.setProperty('--start', (101-this.monthList[i].achievement_rate));
      i++
    });
  },
  methods: {
    UpdateName(index) {
      this.$emit("setChallengeName", this.month.challenge_title)
      const bars = document.querySelectorAll(".bar")
      let i = 0
      bars.forEach(bar => {
        if (bar.classList.length === 2) {
          bar.classList.remove("selected")
        }
        if (i === index) {
          bar.classList.add("selected")
        }
        i++
      });
    }
  }
}
</script>

