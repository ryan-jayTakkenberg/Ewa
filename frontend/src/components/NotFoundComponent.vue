<template>
  <div class="text-center">
    <h1 class="m-3 fw-semibold">Oops! We couldn't find that in our warehouse...</h1>
    <router-link to="/" class="btn btn-secondary p-3">Click to return to the Login Page</router-link>
    <br />
    <img src="../../static/images/lost_warehouse.png" class="img-fluid mt-5" />
  </div>
</template>

<script>
export default {
  name: "NotFoundComponent"
}
</script>

<style scoped>

</style>