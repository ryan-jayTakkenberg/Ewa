<script>
export default {
  name: "SolarTitle",
  props: {
    pageTitle: {
      type: String,
    }
  }
}
</script>

<template>
  <h4 class="page-title">{{pageTitle}}</h4>
</template>

<style scoped>
.page-title{
  font-weight: 700;
  font-size: 2rem;
  color: #333333;
}
</style>
