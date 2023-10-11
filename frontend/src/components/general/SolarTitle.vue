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
  display: flex;
  font-weight: 700;
  font-size: 2rem;
  margin-right: auto;
}
</style>
