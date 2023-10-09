<script>
export default {
  name: "TitleComponent",
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
  padding: 2rem 1rem;
}
</style>
